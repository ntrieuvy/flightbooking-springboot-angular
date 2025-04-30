package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.Roles;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public User getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }

    @Override
    public User getSysAccount() {
        return userRepository.findFirstByRoles_Name(Roles.ROLE_SYS.name()).orElse(null);
    }

}
