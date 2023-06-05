package com.example.Owner.Service;

import com.example.Owner.Domain.User;
import com.example.Owner.Repository.OwnerRepository;
import com.example.Owner.Utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OwnerServiceImpl implements OwnerService{
    @Autowired
    private OwnerRepository repository;
    @Override
    public User saveUser(User user) {
//        if (repository.findOneByUserId(user.getUserId()).block().) {
//            throw new RuntimeException("User Already Exists...!!!");
//        } else {
            return repository.save(user).block();
//        }
    }

    @Override
    public Mono<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(repository::findOneByUserId);
    }
}
