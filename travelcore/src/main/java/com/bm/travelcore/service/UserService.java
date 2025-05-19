package com.bm.travelcore.service;

import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.LoginProvider;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    User getCurrentAccount();

    User getSysAccount();

    Boolean isSysUser(User user);

    public UserDetails loadUserByUsername(String identifier, LoginProvider provider);
}
