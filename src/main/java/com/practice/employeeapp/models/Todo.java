package com.practice.employeeapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    Integer id;
    String title;
    String description;
    Boolean Completed;



}
