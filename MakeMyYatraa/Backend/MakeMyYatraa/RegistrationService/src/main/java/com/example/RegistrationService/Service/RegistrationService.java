package com.example.RegistrationService.Service;

import com.example.RegistrationService.Domain.User;

public interface RegistrationService {
    public User registerUser(User user);
    public User findUser(String userId,String password);
}
