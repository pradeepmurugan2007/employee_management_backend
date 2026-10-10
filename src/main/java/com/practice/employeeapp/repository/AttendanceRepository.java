package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Integer> {
    List<AttendanceRecord> findByWorkDateBetween(LocalDate from, LocalDate to);
    List<AttendanceRecord> findByEmployeeId(int employeeId);
    long countByWorkDateAndStatus(LocalDate date, String status);
}
