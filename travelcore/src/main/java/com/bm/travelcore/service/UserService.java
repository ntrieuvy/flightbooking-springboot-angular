package com.bm.travelcore.service;

import com.bm.travelcore.model.User;

import java.util.Optional;

public interface UserService {
    User getCurrentAccount();

    User getSysAccount();
}
