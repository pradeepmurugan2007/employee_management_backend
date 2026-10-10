package com.practice.employeeapp.repository;


import com.practice.employeeapp.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name);
    
    @org.springframework.data.jpa.repository.Query("SELECT d FROM Department d WHERE (:status IS NULL OR :status = '' OR d.status = :status) AND (:search IS NULL OR :search = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%')))")
    java.util.List<Department> searchDepartments(@org.springframework.data.repository.query.Param("status") String status, @org.springframework.data.repository.query.Param("search") String search);
}
