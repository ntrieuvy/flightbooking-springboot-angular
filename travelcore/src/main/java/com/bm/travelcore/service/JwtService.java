package com.bm.travelcore.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JwtService {
    public String generateToken(UserDetails userDetails);
    public String generateToken(Map<String, Object> claims, UserDetails userDetails);
    public String extractUsername(String token);
    public Date extractExpirationToken(String token);
    public boolean isTokenExprired(String token);
    public Boolean validateToken(String token, UserDetails userDetails);
}
