package com.practice.HelloWorld;
import com.practice.HelloWorld.models.users;
import com.practice.HelloWorld.models.todo;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


public interface todorepos extends JpaRepository <todo, Integer> {

}


