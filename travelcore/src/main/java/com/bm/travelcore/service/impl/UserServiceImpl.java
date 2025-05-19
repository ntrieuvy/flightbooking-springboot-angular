package com.bm.travelcore.service.impl;

import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.LoginProvider;
import com.bm.travelcore.model.enums.Roles;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.UserService;
import com.bm.travelcore.utils.constant.ExceptionMessages;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Boolean isSysUser(User user) {
        String sysRoleName = Roles.ROLE_SYS.name();

        return user.getRoles().stream()
                .anyMatch(role -> sysRoleName.equals(role.getName()));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String identifier, LoginProvider provider) throws UsernameNotFoundException {
        if (identifier.contains("@")) {
            return userRepository.findByEmailAndLoginProvider(identifier, provider)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format(ExceptionMessages.USER_NOT_FOUND_WITH_EMAIL, identifier)));
        } else {
            return userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format(ExceptionMessages.USER_NOT_FOUND_WITH_PHONE, identifier)));
        }
    }

}
