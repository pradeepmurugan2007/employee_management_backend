package com.practice.employeeapp.controller;


import com.practice.employeeapp.repository.*;
import com.practice.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")

public class DashboardController {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private LeaveRequestRepository leaveRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam(required = false) String date) {
        LocalDate targetDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();

        long totalEmployees = employeeRepo.countByStatus("ACTIVE");
        long presentToday = attendanceRepo.countByWorkDateAndStatus(targetDate, "PRESENT");
        long onLeave = leaveRepo.countByStatus("APPROVED");
        long pendingLeaves = leaveRepo.countByStatus("PENDING");

        // New employees this month
        long newEmployees = employeeRepo.findAll().stream()
            .filter(e -> e.getJoiningDate() != null &&
                         e.getJoiningDate().getYear() == targetDate.getYear() &&
                         e.getJoiningDate().getMonth() == targetDate.getMonth())
            .count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalEmployees", totalEmployees);
        summary.put("presentToday", presentToday);
        summary.put("onLeave", onLeave);
        summary.put("newEmployees", newEmployees);
        summary.put("pendingLeaves", pendingLeaves);
        summary.put("date", targetDate.toString());

        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    @GetMapping("/recent-employees")
    public ResponseEntity<?> getRecentEmployees(@RequestParam(defaultValue = "5") int limit) {
        List<?> all = employeeRepo.findAll();
        int size = Math.min(limit, all.size());
        return new ResponseEntity<>(all.subList(Math.max(0, all.size() - size), all.size()), HttpStatus.OK);
    }

    @GetMapping("/pending-leaves")
    public ResponseEntity<?> getPendingLeaves(@RequestParam(defaultValue = "5") int limit) {
        List<?> pending = leaveRepo.findByStatus("PENDING");
        int size = Math.min(limit, pending.size());
        return new ResponseEntity<>(pending.subList(0, size), HttpStatus.OK);
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getDepartmentStats() {
        return new ResponseEntity<>(departmentRepo.findAll(), HttpStatus.OK);
    }
}
