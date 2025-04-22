package com.bm.travelcore.service;

import com.bm.travelcore.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    User registerOAuth2User(String email, String name, String google);
}
