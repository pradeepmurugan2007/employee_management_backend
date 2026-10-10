package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Integer> {
    List<AttendanceRecord> findByWorkDateBetween(LocalDate from, LocalDate to);
    List<AttendanceRecord> findByEmployeeId(int employeeId);
    java.util.Optional<AttendanceRecord> findByEmployeeIdAndWorkDate(int employeeId, LocalDate workDate);
    long countByWorkDateAndStatus(LocalDate date, String status);
    
    @org.springframework.data.jpa.repository.Query("SELECT a FROM AttendanceRecord a WHERE (:status IS NULL OR :status = '' OR a.status = :status) AND (:search IS NULL OR :search = '' OR LOWER(a.employeeName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.employeeCode) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<AttendanceRecord> searchAttendance(@org.springframework.data.repository.query.Param("status") String status, @org.springframework.data.repository.query.Param("search") String search);
}
