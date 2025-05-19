package com.bm.travelcore.utils;

import com.bm.travelcore.model.enums.Title;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    public String getGenderByTitle(String title) {
        if (title == null) {
            return "male";
        }

        title = title.trim().toUpperCase();
        return switch (title) {
            case "MRS" -> "female";
            default -> "male";
        };
    }

    public String getTitleByGender(String gender) {
        if (gender == null) {
            return Title.MR.name();
        }

        gender = gender.trim().toLowerCase();
        return switch (gender) {
            case "female", "f", "1" -> Title.MRS.name();
            default -> Title.MR.name();
        };
    }

    public String ConvertGenderToString(int gender) {
        return gender == 0 ? "Male" : "Female";
    }

    public int ConvertGenderToInt(String gender) {
        return gender.equals("Male") ? 1 : 0;
    }
}
