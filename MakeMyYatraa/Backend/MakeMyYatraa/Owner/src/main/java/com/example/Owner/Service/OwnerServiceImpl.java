package com.example.Owner.Service;

import com.example.Owner.Domain.User;
import com.example.Owner.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService{
    @Autowired
    private OwnerRepository repository;
    @Override
    public User saveUser(User user) {
        if (repository.findById(user.getUserId()).isPresent()) {
            throw new RuntimeException("User Already Exists...!!!");
        } else {
            return repository.save(user);
        }
    }
}
