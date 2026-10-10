package com.practice.employeeapp.controller;


import com.practice.employeeapp.models.Department;
import com.practice.employeeapp.repository.*;
import com.practice.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")

public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        List<Department> departments;
        if ((status != null && !status.isEmpty()) || (search != null && !search.isEmpty())) {
            departments = departmentRepo.searchDepartments(status, search);
        } else {
            departments = departmentRepo.findAll();
        }
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        Optional<Department> dept = departmentRepo.findById(id);
        return dept.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody Department dept) {
        if (dept.getName() == null || dept.getName().isBlank()) {
            return new ResponseEntity<>("Department name is required", HttpStatus.BAD_REQUEST);
        }
        if (departmentRepo.existsByName(dept.getName())) {
            return new ResponseEntity<>("Department name already exists", HttpStatus.CONFLICT);
        }
        if (dept.getStatus() == null) dept.setStatus("ACTIVE");
        Department saved = departmentRepo.save(dept);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable int id, @RequestBody Department dept) {
        if (!departmentRepo.existsById(id)) {
            return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
        }
        dept.setId(id);
        return new ResponseEntity<>(departmentRepo.save(dept), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @RequestBody java.util.Map<String, String> body) {
        Optional<Department> deptOpt = departmentRepo.findById(id);
        if (deptOpt.isEmpty()) {
            return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
        }
        Department dept = deptOpt.get();
        dept.setStatus(body.get("status"));
        departmentRepo.save(dept);
        return new ResponseEntity<>(dept, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable int id) {
        if (!departmentRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        departmentRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
