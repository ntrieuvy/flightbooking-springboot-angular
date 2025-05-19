package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.enums.LoginProvider;
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
        if (identifier.contains(":")) {
            String[] parts = identifier.split(":");
            String email = parts[0];
            LoginProvider provider = LoginProvider.valueOf(parts[1]);
            return userRepository.findByEmailAndLoginProvider(email, provider)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format(ExceptionMessages.USER_NOT_FOUND_WITH_EMAIL, email)));
        } else {
            return userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format(ExceptionMessages.USER_NOT_FOUND_WITH_PHONE, identifier)));
        }
    }
}
