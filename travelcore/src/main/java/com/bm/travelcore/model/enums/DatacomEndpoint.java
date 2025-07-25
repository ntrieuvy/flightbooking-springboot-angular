package com.bm.travelcore.model.enums;

import lombok.Getter;
import lombok.Setter;

public enum DatacomEndpoint {
    ADDON_SERVICE("/flights/addonservice"),
    BOOKING("/flights/book"),
    CANCEL("/flight/cancelbooking"),
    CHANGE_FLIGHT("/flights/changeFlight"),
    BAGGAGE("/flights/getbaggage"),
    ISSUE("/flights/issue"),
    PASSENGER("flight/updatepassenger"),
    VERIFY("/flights/verifyflight"),
    SEARCH("/flights/search"),
    SEARCH_MIN("/flights/searchmin");

    private final String path;

    DatacomEndpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
