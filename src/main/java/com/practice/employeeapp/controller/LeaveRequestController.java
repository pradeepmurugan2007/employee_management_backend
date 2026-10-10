package com.practice.employeeapp.controller;


import com.practice.employeeapp.models.LeaveRequest;
import com.practice.employeeapp.repository.*;
import com.practice.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/leave-requests")

public class LeaveRequestController {

    @Autowired
    private LeaveRequestRepository leaveRepo;

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        List<LeaveRequest> requests;
        if ((status != null && !status.isEmpty()) || (search != null && !search.isEmpty())) {
            requests = leaveRepo.searchLeaveRequests(status, search);
        } else {
            requests = leaveRepo.findAll();
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getById(@PathVariable int id) {
        Optional<LeaveRequest> lr = leaveRepo.findById(id);
        return lr.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                 .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createLeaveRequest(@RequestBody LeaveRequest lr) {
        if (lr.getLeaveType() == null || lr.getLeaveType().isBlank()) {
            return new ResponseEntity<>("Leave type is required", HttpStatus.BAD_REQUEST);
        }
        if (lr.getStartDate() == null || lr.getEndDate() == null) {
            return new ResponseEntity<>("Start and end dates are required", HttpStatus.BAD_REQUEST);
        }
        if (lr.getEndDate().isBefore(lr.getStartDate())) {
            return new ResponseEntity<>("End date cannot be before start date", HttpStatus.BAD_REQUEST);
        }
        // Backend calculates days
        int days = (int)(lr.getEndDate().toEpochDay() - lr.getStartDate().toEpochDay()) + 1;
        lr.setRequestedDays(days);
        lr.setStatus("PENDING");
        LeaveRequest saved = leaveRepo.save(lr);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLeaveRequest(@PathVariable int id, @RequestBody LeaveRequest lr) {
        Optional<LeaveRequest> existing = leaveRepo.findById(id);
        if (existing.isEmpty()) {
            return new ResponseEntity<>("Leave request not found", HttpStatus.NOT_FOUND);
        }
        LeaveRequest existingLr = existing.get();
        if (!"PENDING".equals(existingLr.getStatus())) {
            return new ResponseEntity<>("Only pending requests can be edited", HttpStatus.BAD_REQUEST);
        }
        lr.setId(id);
        return new ResponseEntity<>(leaveRepo.save(lr), HttpStatus.OK);
    }

    @PatchMapping("/{id}/decision")
    public ResponseEntity<?> makeDecision(@PathVariable int id, @RequestBody Map<String, String> body) {
        Optional<LeaveRequest> existing = leaveRepo.findById(id);
        if (existing.isEmpty()) {
            return new ResponseEntity<>("Leave request not found", HttpStatus.NOT_FOUND);
        }
        LeaveRequest lr = existing.get();
        if (!"PENDING".equals(lr.getStatus())) {
            return new ResponseEntity<>("Only pending requests can be approved/rejected", HttpStatus.BAD_REQUEST);
        }
        String decision = body.get("decision");
        if (!"APPROVED".equals(decision) && !"REJECTED".equals(decision)) {
            return new ResponseEntity<>("Decision must be APPROVED or REJECTED", HttpStatus.BAD_REQUEST);
        }
        lr.setStatus(decision);
        lr.setReviewedBy(body.getOrDefault("reviewedBy", "Admin"));
        lr.setReviewComments(body.get("comments"));
        lr.setReviewedAt(LocalDateTime.now());
        return new ResponseEntity<>(leaveRepo.save(lr), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeaveRequest(@PathVariable int id) {
        Optional<LeaveRequest> existing = leaveRepo.findById(id);
        if (existing.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        leaveRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
