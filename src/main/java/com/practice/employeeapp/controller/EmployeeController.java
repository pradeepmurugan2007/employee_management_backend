package com.practice.employeeapp.controller;


import com.practice.employeeapp.models.Employee;
import com.practice.employeeapp.repository.*;
import com.practice.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")

public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        List<Employee> employees;
        if ((status != null && !status.isEmpty()) || (search != null && !search.isEmpty())) {
            employees = employeeRepo.searchEmployees(status, search);
        } else {
            employees = employeeRepo.findAll();
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        Optional<Employee> emp = employeeRepo.findById(id);
        return emp.map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee emp) {
        // Validate required fields
        if (emp.getFirstName() == null || emp.getFirstName().isBlank()) {
            return new ResponseEntity<>("First name is required", HttpStatus.BAD_REQUEST);
        }
        if (emp.getEmail() == null || emp.getEmail().isBlank()) {
            return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
        }
        // Check email uniqueness
        if (employeeRepo.existsByEmail(emp.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }
        // Auto-generate Employee code
        if (emp.getEmployeeCode() == null || emp.getEmployeeCode().isBlank()) {
            long count = employeeRepo.count() + 1;
            emp.setEmployeeCode("EMP" + String.format("%03d", count));
        }
        if (emp.getStatus() == null) emp.setStatus("ACTIVE");
        Employee saved = employeeRepo.save(emp);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestBody Employee emp) {
        if (!employeeRepo.existsById(id)) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
        emp.setId(id);
        Employee updated = employeeRepo.save(emp);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @RequestBody java.util.Map<String, String> body) {
        Optional<Employee> empOpt = employeeRepo.findById(id);
        if (empOpt.isEmpty()) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
        Employee emp = empOpt.get();
        emp.setStatus(body.get("status"));
        employeeRepo.save(emp);
        return new ResponseEntity<>(emp, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        if (!employeeRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        employeeRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
