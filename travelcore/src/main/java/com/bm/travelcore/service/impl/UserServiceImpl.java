package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.User;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User registerOAuth2User(String email, String name, String google) {
        return null;
    }
}
