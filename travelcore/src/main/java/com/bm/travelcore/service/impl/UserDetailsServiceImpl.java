package com.bm.travelcore.service.impl;

import com.bm.travelcore.utils.constant.ExceptionMessages;
import com.bm.travelcore.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(ExceptionMessages.USER_NOT_FOUND_WITH_EMAIL, identifier)))
                : userRepository.findByPhoneNumber(identifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(ExceptionMessages.USER_NOT_FOUND_WITH_PHONE, identifier)));
    }
}
