package com.bm.travelcore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SearchFlightReqDTO {
    @NotNull
    @Min(1)
    private Integer adults;

    @NotNull
    @Min(0)
    private Integer children;

    @NotNull
    @Min(0)
    @Max(2)
    private Integer infant;

    private String airlineCode;

    @NotNull
    @Size(min = 1)
    private List<FlightInfo> flights;

    @AssertTrue(message = "The total number of passengers must not be greater than 9.")
    public boolean isTotalPassengersValid() {
        if (adults == null || children == null || infant == null) {
            return true;
        }
        return adults + children + infant <= 9;
    }

    @Data
    public static class FlightInfo {

        private String start;

        private String end;

        private String date;
    }

    private Integer page = 1;
    private Integer pageSize = 10;

    public void setPage(Integer page) {
        this.page = (page == null || page < 1) ? 1 : page;
    }
}
