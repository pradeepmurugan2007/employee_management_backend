package com.practice.HelloWorld.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class users {

    @Id
    @GeneratedValue
    int id;

    String username;
    String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}