package com.bm.travelcore.strategy.datacom;

import com.bm.travelcore.config.ApplicationProperties;
import com.bm.travelcore.dto.BookFlightReqDTO;
import com.bm.travelcore.strategy.datacom.data.*;
import com.bm.travelcore.dto.SearchFlightReqDTO;
import com.bm.travelcore.model.enums.DatacomEndpoint;
import com.bm.travelcore.populator.Populator;
import com.bm.travelcore.strategy.FlightProviderStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("datacom")
@RequiredArgsConstructor
public class DatacomProvider implements FlightProviderStrategy {

    private final ApplicationProperties properties;
    private final RestTemplate restTemplate;
    private final Populator<SearchFlightReqDTO, SearchReqData> searchDatacomReqPopulator;
    private final Populator<BookFlightReqDTO, BookingReqData> bookingDatacomReqPopulator;

    @Override
    public FlightSearchResData searchFlights(SearchFlightReqDTO requestDTO) {
        try {
            SearchReqData requestData = new SearchReqData();
            requestData.setRequestInfo(buildRequestInfo());

            searchDatacomReqPopulator.populate(requestDTO, requestData);

            String apiUrl = properties.getDcEndpoint().concat(DatacomEndpoint.SEARCH.getPath());

            return restTemplate.postForEntity(
                    apiUrl,
                    requestData,
                    FlightSearchResData.class
            ).getBody();
        } catch (Exception e) {
            log.error("Error calling Datacom flight provider for request: {}", requestDTO, e);
            return null;
        }
    }

    @Override
    public BookingResData bookFlights(BookFlightReqDTO requestDTO) {
        try {
            BookingReqData requestData = new BookingReqData();
            requestData.setRequestInfo(buildRequestInfo());
            bookingDatacomReqPopulator.populate(requestDTO, requestData);
            String apiUrl = properties.getDcEndpoint().concat(DatacomEndpoint.BOOKING.getPath());

            return restTemplate.postForEntity(
                    apiUrl,
                    requestData,
                    BookingResData.class
            ).getBody();

        } catch (Exception e) {
            log.error("Error calling Datacom flight provider for request: {}", requestDTO, e);
            return null;
        }
    }

    @Override
    public Object changPassengerForOrder(BookingChangedReqData changedReqData) {
        try {
            changedReqData.setRequestInfo(buildRequestInfo());
            String apiUrl = properties.getDcEndpoint().concat(DatacomEndpoint.PASSENGER.getPath());

            return restTemplate.postForEntity(
                    apiUrl,
                    changedReqData,
                    BookingChangedResData.class
            ).getBody();

        } catch (Exception e) {
            log.error("Error calling Datacom flight provider for request: {}", changedReqData, e);
            return null;
        }
    }

    @Override
    public Object cancelFlights(CancelBookingReqData cancelBookingReqData) {
        try {
            cancelBookingReqData.setRequestInfo(buildRequestInfo());
            String apiUrl = properties.getDcEndpoint().concat(DatacomEndpoint.CANCEL.getPath());

            return restTemplate.postForEntity(
                    apiUrl,
                    cancelBookingReqData,
                    CancelBookingResData.class
            ).getBody();

        } catch (Exception e) {
            log.error("Error calling Datacom flight provider for request: {}", cancelBookingReqData, e);
            return null;
        }
    }

    private RequestInfo buildRequestInfo() {
        return RequestInfo.builder()
                .privateKey(properties.getDcPrivateKey())
                .apiAccount(properties.getDcApiAccount())
                .apiPassword(properties.getDcApiPassword())
                .currency(properties.getDcCurrency())
                .ipAddress(properties.getDcIpAddress())
                .language(properties.getDcLanguage())
                .build();
    }
}