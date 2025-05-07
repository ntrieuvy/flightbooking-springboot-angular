package com.bm.travelcore.service.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.dto.*;
import com.bm.travelcore.factory.FlightProviderFactory;
import com.bm.travelcore.model.*;
import com.bm.travelcore.repository.*;
import com.bm.travelcore.service.*;
import com.bm.travelcore.strategy.datacom.data.*;
import com.bm.travelcore.strategy.datacom.data.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.micrometer.core.instrument.MeterRegistry;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final ApplicationProperties applicationProperties;
    private final FlightProviderFactory flightProviderFactory;
    private final CommissionService commissionService;
    private final MeterRegistry meterRegistry;
    private final UserService userService;
    private final OrderService orderService;
    private final AgencyRepository agencyRepository;
    private final CustomerService customerService;
    private final FlightRepository flightRepository;
    private final OrderFlightMapRepository orderFlightMapRepository;
    private final AgencyService agencyService;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public SearchFlightResDTO searchFlights(SearchFlightReqDTO request) {
        SearchFlightResDTO response = new SearchFlightResDTO();
        Object result = flightProviderFactory.getStrategy(applicationProperties.getProviderFlightApiDefault())
                .searchFlights(request);

        if (result instanceof FlightSearchResData responseDatacom) {
            long startTime = System.currentTimeMillis();
            try {
                response.setData(commissionService.applyCommission(responseDatacom));
            } finally {
                meterRegistry.timer("commission.apply.time")
                        .record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
            }

            int totalItems = responseDatacom.getListGroup().size();
            PaginationMetadataDTO pagination = new PaginationMetadataDTO(
                    request.getPage(),
                    request.getPageSize(),
                    totalItems
            );

            int startIndex = (request.getPage() - 1) * request.getPageSize();
            int endIndex = Math.min(startIndex + request.getPageSize(), totalItems);

            List<Group> paginatedList = responseDatacom.getListGroup().subList(startIndex, endIndex);

            BeanUtils.copyProperties(responseDatacom, response);
            response.getData().setListGroup(paginatedList);
            response.setPaginationMetadataDTO(pagination);
        }

        return response;
    }

    @Override
    @Transactional
    public BookFlightResDTO bookFlight(BookFlightReqDTO request) {
        String contactClient = request.getContact().getEmail();
        request.getContact().setEmail(userService.getSysAccount().getAgency().getEmail());

        Object result = flightProviderFactory.getStrategy(applicationProperties.getProviderFlightApiDefault())
                .bookFlights(request);

        request.getContact().setEmail(contactClient);

        if (result instanceof BookingResData bookingResData) {
            validateBookingSuccess(bookingResData);
            bookingResData.getListBooking().get(0).getGuestContact().setEmail(contactClient);

            User user = userService.getCurrentAccount();
            Agency agency = getAgencyForUser(user);
            Customer customer = createCustomerFromBooking(bookingResData, agency);
            Order order = createOrderForBooking(request, bookingResData, agency, customer);

            processBookingDetails(bookingResData, order);
        }

        return null;
    }

    private void validateBookingSuccess(BookingResData bookingResData) {
        if (!bookingResData.isSuccess()) {
            throw new RuntimeException("Booking failed");
        }
    }

    private Agency getAgencyForUser(User user) {
        return user.getAgency() != null ? user.getAgency() :
                userService.getSysAccount().getAgency();
    }

    private Customer createCustomerFromBooking(BookingResData bookingResData, Agency agency) {
        return customerService.storeCustomer(
                bookingResData.getListBooking().get(0).getGuestContact(),
                agency
        );
    }

    private Order createOrderForBooking(BookFlightReqDTO request, BookingResData bookingResData,
                                        Agency agency, Customer customer) {
        return orderService.bookingFlight(request, bookingResData, agency, customer);
    }

    private void processBookingDetails(BookingResData bookingResData, Order order) {
        bookingResData.getListBooking().forEach(booking -> {
            OrderDetail orderDetail = createOrderDetailFromBooking(booking, order);
            processFlightDetails(orderDetail, booking);
        });
    }

    private OrderDetail createOrderDetailFromBooking(Booking booking, Order order) {
        return orderDetailRepository.save(OrderDetail.builder()
                .bookingCode(booking.getBookingCode())
                .flightValue(booking.getGdsCode())
                .bookingImage(booking.getBookingImage())
                .airline(booking.getAirline())
                .order(order)
                .currency(booking.getCurrency())
                .totalPrice(booking.getTotalPrice())
                .expiryDate(LocalDateTime.parse(booking.getExpirationTime()))
                .session(booking.getListFlightFare().get(0).getSession())
                .build());
    }

    private void processFlightDetails(OrderDetail orderDetail, Booking booking) {
        PassengerCounts counts = calculatePassengerCountsAndFares(booking);
        List<Flight> processedFlights = processFlightData(booking, orderDetail);
        updateOrderDetailWithFlightInfo(orderDetail, counts, processedFlights, booking);
    }

    private PassengerCounts calculatePassengerCountsAndFares(Booking booking) {
        PassengerCounts counts = new PassengerCounts();

        for (FlightFare fare : booking.getListFlightFare()) {
            FareInfo fareInfo = fare.getFareInfo();

            for (PassengerRes passenger : booking.getListPassenger()) {
                FarePax farePax = fareInfo.getListFarePax().stream()
                        .filter(p -> p.getPaxType().equals(passenger.getType()))
                        .findFirst()
                        .orElse(null);

                if (farePax != null) {
                    updateCountsForPassengerType(
                            counts,
                            passenger.getType(),
                            farePax.getBaseFare(),
                            farePax.getListTaxDetail().stream().mapToDouble(FarePax.TaxDetail::getAmount).sum()
                    );
                }
            }
        }

        return counts;
    }

    private void updateCountsForPassengerType(PassengerCounts counts, String type,
                                              double baseFare, double taxes) {
        switch (type) {
            case "ADT":
                counts.numberAdt++;
                counts.fareAdt += baseFare;
                counts.taxAdt += taxes;
                break;
            case "CHD":
                counts.numberChd++;
                counts.fareChd += baseFare;
                counts.taxChd += taxes;
                break;
            case "INF":
                counts.numberInf++;
                counts.fareInf += baseFare;
                counts.taxInf += taxes;
                break;
        }
    }

    private List<Flight> processFlightData(Booking booking, OrderDetail orderDetail) {
        List<Flight> processedFlights = new ArrayList<>();

        booking.getListFlightFare().forEach(fare -> {
            fare.getListFlightData().forEach(flightData -> {
                Flight flight = getOrCreateFlight(flightData, fare);
                processedFlights.add(flight);
                createOrderFlightMapping(orderDetail, flight, fare.getLeg());
            });
        });

        return processedFlights;
    }

    private Flight getOrCreateFlight(FlightData flightData, FlightFare fare) {
        String flightUniqueKey = generateFlightUniqueKey(flightData);
        return flightRepository.findByUniqueKey(flightUniqueKey)
                .orElseGet(() -> createAndSaveNewFlight(flightData, fare));
    }

    private Flight createAndSaveNewFlight(FlightData flightData, FlightFare fare) {
        Flight newFlight = buildFlightEntity(flightData, fare);
        return flightRepository.save(newFlight);
    }

    private void createOrderFlightMapping(OrderDetail orderDetail, Flight flight, Integer leg) {
        if (!orderFlightMapRepository.existsByOrderDetailAndFlight(orderDetail, flight)) {
            OrderFlightMap mapping = OrderFlightMap.builder()
                    .orderDetail(orderDetail)
                    .flight(flight)
                    .leg(leg)
                    .build();
            orderFlightMapRepository.save(mapping);
        }
    }

    private void updateOrderDetailWithFlightInfo(OrderDetail orderDetail, PassengerCounts counts,
                                                 List<Flight> processedFlights, Booking booking) {
        double totalNetPrice = counts.fareAdt + counts.fareChd + counts.fareInf;
        double totalPrice = totalNetPrice + counts.taxAdt + counts.taxChd + counts.taxInf;

        orderDetail.setNumberAdt(counts.numberAdt);
        orderDetail.setNumberChd(counts.numberChd);
        orderDetail.setNumberInf(counts.numberInf);
        orderDetail.setFareAdt(counts.fareAdt);
        orderDetail.setFareChd(counts.fareChd);
        orderDetail.setFareInf(counts.fareInf);
        orderDetail.setTaxAdt(counts.taxAdt);
        orderDetail.setTaxChd(counts.taxChd);
        orderDetail.setTaxInf(counts.taxInf);
        orderDetail.setTotalNetPrice(totalNetPrice);
        orderDetail.setTotalPrice(totalPrice);
        orderDetail.setNumberFlight(processedFlights.size());
        orderDetail.setLeg(booking.getListFlightFare().size());

        orderDetailRepository.save(orderDetail);
    }

    private String generateFlightUniqueKey(FlightData flightData) {
        return String.format("%s-%s-%s-%s",
                flightData.getAirline(),
                flightData.getFlightNumber(),
                flightData.getDepartDate().replace(" ", ""),
                flightData.getStartPoint());
    }

    private Flight buildFlightEntity(FlightData flightData, FlightFare fare) {
        return Flight.builder()
                .code(flightData.getFlightNumber() + "-" + System.currentTimeMillis())
                .uniqueKey(generateFlightUniqueKey(flightData))
                .airline(flightData.getAirline())
                .departure(flightData.getStartPoint())
                .arrival(flightData.getEndPoint())
                .date(LocalDate.parse(flightData.getDepartDate().substring(0, 8),
                        DateTimeFormatter.ofPattern("ddMMyyyy")))
                .std(LocalDateTime.parse(flightData.getDepartDate(),
                        DateTimeFormatter.ofPattern("ddMMyyyy HHmm")))
                .sta(LocalDateTime.parse(flightData.getArriveDate(),
                        DateTimeFormatter.ofPattern("ddMMyyyy HHmm")))
                .number(flightData.getFlightNumber())
                .duration(flightData.getDuration())
                .groupClass(fare.getFareInfo().getCabinName())
                .fareClass(fare.getFareInfo().getFareClass())
                .handBaggage(getBaggageInfo(fare, "HandBaggage"))
                .allowanceBaggage(getBaggageInfo(fare, "FreeBaggage"))
                .operating(flightData.getOperator())
                .build();
    }

    private String getBaggageInfo(FlightFare fare, String type) {
        return fare.getFareInfo().getListFarePax().stream()
                .findFirst()
                .map(pax -> {
                    if ("HandBaggage".equals(type)) {
                        return pax.getListFareInfo().get(0).getHandBaggage();
                    } else {
                        return pax.getListFareInfo().get(0).getFreeBaggage();
                    }
                })
                .orElse(null);
    }

    private static class PassengerCounts {
        int numberAdt;
        int numberChd;
        int numberInf;
        double fareAdt;
        double fareChd;
        double fareInf;
        double taxAdt;
        double taxChd;
        double taxInf;
    }
}