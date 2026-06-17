package com.practice.HelloWorld.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
public class todo {
    @Id
    @GeneratedValue
    Integer id;
    String title;
    String description;
    Boolean Completed;



}
