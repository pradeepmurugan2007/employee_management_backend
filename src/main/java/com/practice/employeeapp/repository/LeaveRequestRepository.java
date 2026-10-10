package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployeeId(int employeeId);
    long countByStatus(String status);
}
