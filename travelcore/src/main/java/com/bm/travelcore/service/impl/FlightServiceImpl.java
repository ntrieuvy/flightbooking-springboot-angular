package com.bm.travelcore.service.impl;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.config.RedisKeyConfig;
import com.bm.travelcore.dto.*;
import com.bm.travelcore.factory.FlightProviderFactory;
import com.bm.travelcore.model.*;
import com.bm.travelcore.repository.*;
import com.bm.travelcore.service.*;
import com.bm.travelcore.strategy.datacom.data.*;
import com.bm.travelcore.strategy.datacom.data.Passport;
import com.bm.travelcore.utils.Helper;
import com.bm.travelcore.utils.constant.AppConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.util.CollectionUtils;

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
    private final PassengerService passengerService;
    private final BaggageService baggageService;
    private final EmailService emailService;
    private final TicketService ticketService;
    private final OrderRepository orderRepository;
    private final CommissionRepository commissionRepository;
    private final RedisService redisService;
    private final Helper helper;
    private final ObjectMapper objectMapper;

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
    public BookFlightResDTO holdFlight(BookFlightReqDTO request) {
        List<PassengerReqDTO> randomPassengers = generateRandomPassengersWithValidAge(request.getListPassenger());
        request.setListPassenger(randomPassengers);
        request.setContact(generateRandomContact());

        request.setInvoice(null);

        Object result = flightProviderFactory.getStrategy(applicationProperties.getProviderFlightApiDefault())
                .bookFlights(request);

        if (result instanceof BookingResData bookingResData) {
            validateBookingSuccess(bookingResData);
            if (!bookingResData.isSuccess()) {
                return BookFlightResDTO.builder()
                        .success(false)
                        .message(AppConstant.HOLD_FLIGHT_FAILED + ": " + bookingResData.getMessage())
                        .build();
            }

            String orderId = bookingResData.getOrderId();
            redisService.save(RedisKeyConfig.getHoldFlightKey(orderId), convertToJson(bookingResData), 60);


            return BookFlightResDTO.builder()
                    .success(Boolean.TRUE)
                    .message(AppConstant.HOLD_FLIGHT_SUCCESS)
                    .orderId(orderId)
                    .build();
        }

        return BookFlightResDTO.builder()
                .success(false)
                .message(AppConstant.HOLD_FLIGHT_INVALID_RESPONSE)
                .build();
    }


    @Override
    @Transactional
    public BookFlightResDTO bookFlight(BookFlightReqDTO request) throws MessagingException {
        if (request == null || request.getContact() == null || request.getOrderId() == null) {
            throw new IllegalArgumentException("Invalid booking request");
        }

        BookingResData bookingResData = getValidBookingDataFromRedis(request.getOrderId());
        if (bookingResData == null) {
            return BookFlightResDTO.builder()
                    .success(false)
                    .message("Booking data not found")
                    .build();
        }
        updatePassengerInformation(bookingResData, request);
        updateContactInformation(bookingResData, request);
        Order order = createAndSaveOrder(request, bookingResData);
        sendConfirmationEmail(order);

        redisService.delete(RedisKeyConfig.getHoldFlightKey(request.getOrderId()));

        return BookFlightResDTO.builder()
                .success(Boolean.TRUE)
                .message("Booking success")
                .build();
    }

    @Override
    public BookFlightResDTO cancelFlights(String orderId) {
        BookingResData bookingResData = getValidBookingDataFromRedis(orderId);
        if (bookingResData == null) {
            return BookFlightResDTO.builder()
                    .success(false)
                    .message("Booking data not found")
                    .build();
        }

        bookingResData.getListBooking().forEach(booking -> {
            String[] segmentIds = booking.getListFlightFare().stream()
                    .flatMap(flightFare -> flightFare.getListFlightData().stream())
                    .flatMap(flight -> flight.getListSegment().stream())
                    .map(Segment::getSegmentId)
                    .toArray(String[]::new);

            CancelBookingReqData cancelBookingReqData = CancelBookingReqData
                    .builder()
                    .bookingCode(booking.getBookingCode())
                    .cancelAll(Boolean.TRUE)
                    .system(booking.getSystem())
                    .airline(booking.getAirline())
                    .listSegmentId(segmentIds)
                    .build();

            Object result = flightProviderFactory.getStrategy(applicationProperties.getProviderFlightApiDefault())
                    .cancelFlights(cancelBookingReqData);

            if (result instanceof CancelBookingResData cancelBookingResData) {
                if (!cancelBookingResData.isSuccess()) {
                    throw new RuntimeException("Cancel flights failed: " + cancelBookingResData.getMessage());
                }
            } else {
                throw new RuntimeException("Unexpected response type from flight provider");
            }
        });

        redisService.delete(RedisKeyConfig.getHoldFlightKey(orderId));

        return BookFlightResDTO.builder()
                .success(Boolean.TRUE)
                .message("Cancel booking success")
                .build();
    }


    private void updatePassengerInformation(BookingResData bookingResData, BookFlightReqDTO request) {
        try {
            changePassengerForOrder(bookingResData, request);
        } catch (Exception e) {

        }
    }

    private void updateContactInformation(BookingResData bookingResData, BookFlightReqDTO request) {
        ContactReqDTO contact = request.getContact();
        bookingResData.getListBooking().forEach(booking -> {
            ContactInfo guestContact = booking.getGuestContact();
            guestContact.setEmail(contact.getEmail());
            guestContact.setPhone(contact.getPhone());
            guestContact.setName(contact.getFullName());
        });
    }

    private Order createAndSaveOrder(BookFlightReqDTO request, BookingResData bookingResData) {
        User user = userService.getCurrentAccount();
        Agency agency = getAgencyForUser(user);
        Customer customer = createCustomerFromBooking(bookingResData, agency);
        Order order = createOrderForBooking(request, bookingResData, agency, customer);
        processBookingDetails(bookingResData, order);
        return orderRepository.save(order);
    }

    private void sendConfirmationEmail(Order order) {
        try {
            emailService.sendMailBooking(order);
        } catch (MessagingException e) {
        }
    }

    private BookingResData getValidBookingDataFromRedis(String orderId) {
        return getBookingDataFromRedis(orderId);
    }

    private void changePassengerForOrder(BookingResData order, BookFlightReqDTO request) {
        if (CollectionUtils.isEmpty(request.getListPassenger())) {
            throw new IllegalArgumentException("Passenger list is empty");
        }

        order.getListBooking().forEach(booking -> {
            try {
                validateAndUpdatePassengers(booking, request);
                BookingChangedReqData changedReqData = buildChangeRequest(booking);
                callFlightProviderToUpdatePassengers(changedReqData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void validateAndUpdatePassengers(Booking booking, BookFlightReqDTO request) {
        booking.getListPassenger().forEach(passenger -> {
            int index = passenger.getIndex();
            if (index < 1 || index > request.getListPassenger().size()) {
                throw new IllegalArgumentException("Invalid passenger index: " + index);
            }

            PassengerReqDTO passengerReqDTO = request.getListPassenger().get(index - 1);
            updatePassengerDetails(passenger, passengerReqDTO);
        });
    }

    private void updatePassengerDetails(PassengerRes passenger, PassengerReqDTO passengerReqDTO) {
        passenger.setSurname(passengerReqDTO.getLastName());
        passenger.setGivenName(passengerReqDTO.getFirstName());
        passenger.setGender(helper.ConvertGenderToInt(passengerReqDTO.getGender()));
        passenger.setDateOfBirth(passengerReqDTO.getDob());
        passenger.setTitle(helper.getTitleByGender(passengerReqDTO.getGender()));

        if (passengerReqDTO.getPassport() != null) {
            passenger.setPassport(buildPassport(passengerReqDTO.getPassport()));
        }
    }

    private Passport buildPassport(PassportReqDTO passportReq) {
        return Passport.builder()
                .issueCountry(passportReq.getIssueCountry())
                .documentExpiry(passportReq.getDocumentExpiry())
                .documentCode(passportReq.getDocumentCode())
                .nationality(passportReq.getNationality())
                .documentType(passportReq.getDocumentType())
                .build();
    }

    private BookingChangedReqData buildChangeRequest(Booking booking) {
        return BookingChangedReqData.builder()
                .bookingCode(booking.getBookingCode())
                .airline(booking.getAirline())
                .system(booking.getSystem())
                .listPassenger(booking.getListPassenger())
                .build();
    }

    private void callFlightProviderToUpdatePassengers(BookingChangedReqData changedReqData) {
        try {
            Object result = flightProviderFactory.getStrategy(applicationProperties.getProviderFlightApiDefault())
                    .changPassengerForOrder(changedReqData);

            if (result instanceof BookingChangedResData bookingChangedResData) {
                if (!bookingChangedResData.isSuccess()) {
                    throw new RuntimeException("Failed to change passenger for order");
                }
            } else {
                throw new RuntimeException("Unexpected response type from flight provider");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with flight provider", e);
        }
    }

    private List<PassengerReqDTO> generateRandomPassengersWithValidAge(List<PassengerReqDTO> originalPassengers) {
        List<PassengerReqDTO> randomPassengers = new ArrayList<>();
        Random random = new Random();
        DateTimeFormatter dobFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (PassengerReqDTO original : originalPassengers) {
            PassengerReqDTO randomPassenger = new PassengerReqDTO();
            BeanUtils.copyProperties(original, randomPassenger);

            randomPassenger.setFirstName(original.getType());
            randomPassenger.setLastName(String.valueOf((char)('A' + originalPassengers.indexOf(original))));
            randomPassenger.setGender("MALE");

            LocalDate dob = switch (original.getType()) {
                case "ADT" -> {
                    LocalDate dateOfBirth = LocalDate.now().minusYears(13 + random.nextInt(50));
                    String documentCode;
                    do {
                        documentCode = "0" + String.format("%011d", random.nextLong(100000000000L));
                    } while (redisService.exists(RedisKeyConfig.getDocCodeKey(documentCode)));

                    redisService.save(RedisKeyConfig.getDocCodeKey(documentCode), "used", 60);

                    PassportReqDTO passport = new PassportReqDTO();
                    passport.setDocumentType("National_id");
                    passport.setDocumentCode(documentCode);
                    passport.setDocumentExpiry(LocalDate.now().plusYears(5).toString());
                    passport.setNationality("VN");
                    passport.setIssueCountry("VN");
                    randomPassenger.setPassport(passport);
                    yield dateOfBirth;
                }
                case "CHD" -> LocalDate.now().minusYears(2 + random.nextInt(10));
                case "INF" -> LocalDate.now().minusMonths(random.nextInt(23));
                default -> throw new IllegalArgumentException("Invalid passenger type");
            };
            randomPassenger.setDob(dob.format(dobFormatter));
            randomPassengers.add(randomPassenger);
        }

        return randomPassengers;
    }

    private ContactReqDTO generateRandomContact() {
        Agency agency = userService.getSysAccount().getAgency();
        return ContactReqDTO.builder()
                .gender("MALE")
                .fullName(agency.getName())
                .phone(agency.getPhone())
                .email(userService.getSysAccount().getAgency().getEmail())
                .build();
    }

    private String convertToJson(BookingResData bookingResData) {
        try {
            return objectMapper.writeValueAsString(bookingResData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert booking response to JSON", e);
        }
    }

    private BookingResData getBookingDataFromRedis(String orderId) {
        String json = redisService.get(RedisKeyConfig.getHoldFlightKey(orderId));
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, BookingResData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert booking response from cache", e);
        }
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
            order.addOrderDetail(orderDetail);
            processFlightDetails(order, orderDetail, booking);
            processTickets(booking, orderDetail);
        });
    }

    private void processTickets(Booking booking, OrderDetail orderDetail) {
        if (booking.getListTicketData() == null || booking.getListTicketData().isEmpty()) {
            return;
        }
        ticketService.storeTicketsForBooking(booking.getListTicketData(), orderDetail);
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
                .expiryDate(Instant.parse(booking.getExpirationTime()).atZone(ZoneId.systemDefault()).toLocalDateTime())
                .session(booking.getListFlightFare().get(0).getSession())
                .build());
    }

    private void processFlightDetails(Order order, OrderDetail orderDetail, Booking booking) {
        PassengerCounts counts = calculatePassengerCountsAndFares(order, booking);
        List<Flight> processedFlights = processFlightData(booking, orderDetail);
        updateOrderDetailWithFlightInfo(orderDetail, counts, processedFlights, booking);
    }

    private PassengerCounts calculatePassengerCountsAndFares(Order order, Booking booking) {
        PassengerCounts counts = new PassengerCounts();

        for (PassengerRes passenger : booking.getListPassenger()) {
            Passenger passengerForBooking = passengerService.storePassengerForBooking(order, passenger);
            order.addPassenger(passengerForBooking);

            passenger.getListBaggage().forEach(baggage -> {
                baggageService.storeBaggageForBooking(passengerForBooking, baggage);
            });
        }

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
                OrderFlightMap orderFlightMap = createOrderFlightMapping(orderDetail, flight, fare.getLeg());
                orderDetail.addFlight(orderFlightMap);
                flight.addOrderDetail(orderFlightMap);
                flightRepository.save(flight);
                commissionService.processCommissionFeeForOrderDetail(orderDetail.getOrder().getAgency().getId(), orderDetail, flight);
            });
        });

        return processedFlights;
    }

    private Flight getOrCreateFlight(FlightData flightData, FlightFare fare) {
        String flightUniqueKey = generateFlightUniqueKey(flightData, fare.getFareInfo());
        return flightRepository.findByUniqueKey(flightUniqueKey)
                .orElseGet(() -> createAndSaveNewFlight(flightData, fare));
    }

    private Flight createAndSaveNewFlight(FlightData flightData, FlightFare fare) {
        Flight newFlight = buildFlightEntity(flightData, fare);
        return flightRepository.save(newFlight);
    }

    private OrderFlightMap createOrderFlightMapping(OrderDetail orderDetail, Flight flight, Integer leg) {
        if (!orderFlightMapRepository.existsByOrderDetailAndFlight(orderDetail, flight)) {
            OrderFlightMap mapping = OrderFlightMap.builder()
                    .orderDetail(orderDetail)
                    .flight(flight)
                    .leg(leg)
                    .build();
            return orderFlightMapRepository.save(mapping);
        }

        return null;
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
        orderDetail.setNumberFlight(processedFlights.size());
        orderDetail.setLeg(booking.getListFlightFare().size());
        orderDetail.setCommissionTotal(
                (orderDetail.getCommissionInf() + orderDetail.getSystemCommissionInf()) * counts.numberInf +
                (orderDetail.getCommissionChd() + orderDetail.getSystemCommissionChd()) * counts.numberChd +
                (orderDetail.getCommissionAdt() + orderDetail.getSystemCommissionAdt()) * counts.numberAdt
        );
        orderDetail.setTotalPrice(totalPrice + orderDetail.getCommissionTotal());

        orderDetailRepository.save(orderDetail);
    }

    private String generateFlightUniqueKey(FlightData flightData, FareInfo fareInfo) {
        return String.format("%s-%s%s%s-%s-%s",
                flightData.getAirline(),
                flightData.getFlightNumber(),
                fareInfo.getFareClass(),
                fareInfo.getCabinName().trim(),
                flightData.getDepartDate().replace(" ", ""),
                flightData.getStartPoint())
        ;
    }

    private Flight buildFlightEntity(FlightData flightData, FlightFare fare) {
        return Flight.builder()
                .code(flightData.getFlightNumber() + "-" + System.currentTimeMillis())
                .uniqueKey(generateFlightUniqueKey(flightData, fare.getFareInfo()))
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