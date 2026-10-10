package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployeeId(int employeeId);
    long countByStatus(String status);
    
    @org.springframework.data.jpa.repository.Query("SELECT l FROM LeaveRequest l WHERE (:status IS NULL OR :status = '' OR l.status = :status) AND (:search IS NULL OR :search = '' OR LOWER(l.employeeName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(l.leaveType) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(l.reason) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<LeaveRequest> searchLeaveRequests(@org.springframework.data.repository.query.Param("status") String status, @org.springframework.data.repository.query.Param("search") String search);
}
