package com.bm.travelcore.service.impl;

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + identifier))
                : userRepository.findByPhoneNumber(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + identifier));
    }
}
