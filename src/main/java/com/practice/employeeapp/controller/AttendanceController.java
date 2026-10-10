package com.practice.employeeapp.controller;


import com.practice.employeeapp.models.AttendanceRecord;
import com.practice.employeeapp.repository.*;
import com.practice.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")

public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @GetMapping
    public ResponseEntity<List<AttendanceRecord>> getAttendance(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        if ((status != null && !status.isEmpty()) || (search != null && !search.isEmpty())) {
            return new ResponseEntity<>(attendanceRepo.searchAttendance(status, search), HttpStatus.OK);
        } else if (from != null && to != null) {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);
            return new ResponseEntity<>(attendanceRepo.findByWorkDateBetween(fromDate, toDate), HttpStatus.OK);
        }
        return new ResponseEntity<>(attendanceRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceRecord> getById(@PathVariable int id) {
        Optional<AttendanceRecord> record = attendanceRepo.findById(id);
        return record.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceRecord record) {
        if (record.getEmployeeId() <= 0) {
            return new ResponseEntity<>("Employee ID is required", HttpStatus.BAD_REQUEST);
        }
        if (record.getWorkDate() == null) {
            record.setWorkDate(LocalDate.now());
        }

        Optional<AttendanceRecord> existing = attendanceRepo.findByEmployeeIdAndWorkDate(record.getEmployeeId(), record.getWorkDate());
        
        if ("CHECK_IN".equalsIgnoreCase(record.getAttendanceType())) {
            if (existing.isPresent() && existing.get().getCheckInAt() != null) {
                return new ResponseEntity<>("Attendance already checked in for today.", HttpStatus.BAD_REQUEST);
            }
            if (record.getCheckInAt() == null) {
                record.setCheckInAt(LocalDateTime.now());
            }
            if (record.getStatus() == null) {
                record.setStatus("PRESENT");
            }
            return new ResponseEntity<>(attendanceRepo.save(record), HttpStatus.CREATED);
        } else if ("CHECK_OUT".equalsIgnoreCase(record.getAttendanceType())) {
            if (existing.isEmpty()) {
                return new ResponseEntity<>("No check-in record found for today.", HttpStatus.BAD_REQUEST);
            }
            AttendanceRecord r = existing.get();
            if (r.getCheckOutAt() != null) {
                return new ResponseEntity<>("Already checked out for today.", HttpStatus.BAD_REQUEST);
            }
            
            LocalDateTime checkoutTime = record.getCheckOutAt() != null ? record.getCheckOutAt() : LocalDateTime.now();
            r.setCheckOutAt(checkoutTime);
            
            if (r.getCheckInAt() != null) {
                Duration duration = Duration.between(r.getCheckInAt(), checkoutTime);
                long hours = duration.toHours();
                long minutes = duration.toMinutesPart();
                r.setWorkingHours(hours + "h " + minutes + "m");
            }
            return new ResponseEntity<>(attendanceRepo.save(r), HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid attendance type", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAttendance(@PathVariable int id, @RequestBody AttendanceRecord record) {
        Optional<AttendanceRecord> existing = attendanceRepo.findById(id);
        if (existing.isEmpty()) {
            return new ResponseEntity<>("Record not found", HttpStatus.NOT_FOUND);
        }
        AttendanceRecord r = existing.get();
        if (record.getCheckOutAt() != null) {
            r.setCheckOutAt(record.getCheckOutAt());
            // Calculate working hours
            if (r.getCheckInAt() != null) {
                Duration duration = Duration.between(r.getCheckInAt(), record.getCheckOutAt());
                long hours = duration.toHours();
                long minutes = duration.toMinutesPart();
                r.setWorkingHours(hours + "h " + minutes + "m");
            }
        }
        if (record.getStatus() != null) r.setStatus(record.getStatus());
        if (record.getRemarks() != null) r.setRemarks(record.getRemarks());
        return new ResponseEntity<>(attendanceRepo.save(r), HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getSummary(@RequestParam(required = false) String date) {
        LocalDate targetDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        long present = attendanceRepo.countByWorkDateAndStatus(targetDate, "PRESENT");
        long absent = attendanceRepo.countByWorkDateAndStatus(targetDate, "ABSENT");
        long onLeave = attendanceRepo.countByWorkDateAndStatus(targetDate, "ON_LEAVE");
        long late = attendanceRepo.countByWorkDateAndStatus(targetDate, "LATE");
        return new ResponseEntity<>(Map.of(
            "present", present, "absent", absent,
            "onLeave", onLeave, "late", late
        ), HttpStatus.OK);
    }
}
