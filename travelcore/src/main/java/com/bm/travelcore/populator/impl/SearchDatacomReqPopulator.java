package com.bm.travelcore.populator.impl;

import com.bm.travelcore.dto.SearchFlightReqDTO;
import com.bm.travelcore.strategy.datacom.data.Route;
import com.bm.travelcore.strategy.datacom.data.SearchReqData;
import com.bm.travelcore.populator.Populator;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchDatacomReqPopulator implements Populator<SearchFlightReqDTO, SearchReqData> {
    @Override
    public void populate(SearchFlightReqDTO source, SearchReqData target) {
        target.setSystem(source.getAirlineCode());
        target.setAdt(source.getAdults());
        target.setChd(source.getChildren());
        target.setInf(source.getInfant());
        target.setTourcode(StringUtil.EMPTY_STRING);

        List<Route> routes = new ArrayList<>();

        for (SearchFlightReqDTO.FlightInfo flight : source.getFlights()) {
            Route route = Route.builder()
                    .leg(source.getFlights().indexOf(flight))
                    .startPoint(flight.getStart())
                    .endPoint(flight.getEnd())
                    .departDate(String.valueOf(flight.getDate()))
                    .build();
            routes.add(route);
        }

        target.setListRoute(routes);
    }
}
