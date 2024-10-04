package com.example.rqchallenge.employees.data;

import com.example.rqchallenge.employees.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRepository {
    List<Employee> findAllEmployees();
    Optional<Employee> findEmployeeById(String id);
    Optional<Employee> saveEmployee(Employee employee);
    void deleteEmployeeById(String id);
}
