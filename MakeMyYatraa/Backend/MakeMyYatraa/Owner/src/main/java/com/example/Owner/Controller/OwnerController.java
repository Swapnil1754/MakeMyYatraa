package com.example.Owner.Controller;

import com.example.Owner.Domain.User;
import com.example.Owner.Exceptions.OwnerNotFoundException;
import com.example.Owner.Service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {
    private OwnerService service;
    @Autowired

    public OwnerController(OwnerService service) {
        this.service = service;
    }
    @GetMapping("/fetch")
    public Mono<User> fetchUser(@PathVariable String userId) throws OwnerNotFoundException {
        return service.getUserWithAuthorities()
                .map(User::new)
                .switchIfEmpty(Mono.error(new OwnerNotFoundException()));
    }
}
