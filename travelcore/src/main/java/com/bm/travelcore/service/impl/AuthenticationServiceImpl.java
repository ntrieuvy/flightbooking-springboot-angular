package com.bm.travelcore.service.impl;

import com.bm.travelcore.constant.Roles;
import com.bm.travelcore.constant.IdentifyType;
import com.bm.travelcore.dto.RegistrationReqDTO;
import com.bm.travelcore.model.User;
import com.bm.travelcore.repository.RoleRepository;
import com.bm.travelcore.repository.UserRepository;
import com.bm.travelcore.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void register(RegistrationReqDTO reqDTO) {

        var userRole = roleRepository.findByName(String.valueOf(Roles.USER))
                .orElseThrow(() -> new IllegalArgumentException("ROLE USER WAS NOT INITIALIZED"));

        IdentifyType userType = reqDTO.getIdentifyType();

        User user = User
                .builder()
                .firstname(reqDTO.getFirstName())
                .lastname(reqDTO.getLastName())
                .email((userType == IdentifyType.EMAIL ? reqDTO.getIdentifier() : null))
                .phoneNumber((userType == IdentifyType.PHONE ? reqDTO.getIdentifier() : null))
                .password(passwordEncoder.encode(reqDTO.getPassword()))
                .accountLocked(Boolean.FALSE)
                .enabled(Boolean.FALSE)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
    }
}
