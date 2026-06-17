package com.practice.HelloWorld;

import com.practice.HelloWorld.models.users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userrepos
        extends JpaRepository<users, Integer> {

    users findByUsername(String username);
}