package com.juaracoding.TA.repo;


import com.juaracoding.TA.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

}