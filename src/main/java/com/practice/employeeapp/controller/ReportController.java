package com.practice.employeeapp.controller;

import com.practice.employeeapp.models.*;
import com.practice.employeeapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private LeaveRequestRepository leaveRepo;
    @Autowired
    private DepartmentRepository departmentRepo;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getReportStats() {
        List<LeaveRequest> leaves = leaveRepo.findAll();
        long totalLeaves = leaves.size();
        long approvedLeaves = leaves.stream().filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus())).count();
        long pendingLeaves = leaves.stream().filter(l -> "PENDING".equalsIgnoreCase(l.getStatus())).count();
        long rejectedLeaves = leaves.stream().filter(l -> "REJECTED".equalsIgnoreCase(l.getStatus())).count();

        List<Employee> employees = employeeRepo.findAll();
        long totalEmps = employees.size();
        long activeEmps = employees.stream().filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus())).count();
        long inactiveEmps = employees.stream().filter(e -> "INACTIVE".equalsIgnoreCase(e.getStatus())).count();

        long totalDepts = departmentRepo.count();

        Map<String, Object> stats = new HashMap<>();
        
        Map<String, Object> leaveStats = new HashMap<>();
        leaveStats.put("total", totalLeaves);
        leaveStats.put("approved", approvedLeaves);
        leaveStats.put("pending", pendingLeaves);
        leaveStats.put("rejected", rejectedLeaves);
        stats.put("leave", leaveStats);
        
        Map<String, Object> empStats = new HashMap<>();
        empStats.put("total", totalEmps);
        empStats.put("active", activeEmps);
        empStats.put("inactive", inactiveEmps);
        stats.put("employee", empStats);

        Map<String, Object> deptStats = new HashMap<>();
        deptStats.put("total", totalDepts);
        deptStats.put("totalEmployees", totalEmps);
        stats.put("department", deptStats);

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
