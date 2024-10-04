package com.example.rqchallenge.employees;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getEmployeesByNameSearch(String searchString);
    Optional<Employee> getEmployeeById(String id);
    Integer getHighestSalaryOfEmployees();
    List<String> getTopTenHighestEarningEmployeeNames();
    Employee createEmployee(String name, String salary, String age);
    void deleteEmployeeById(String id);
}
