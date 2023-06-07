package com.example.Owner.Domain.Customers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "Customers")
public class User {
    @Id
    private String userId;
    private boolean isOwner;
    private boolean isActivated;
    private String email;
    private String name1;
    private String mobNo;
    private String password;
    private String city;

    public User(com.example.Owner.Domain.User user) {
    }
}
