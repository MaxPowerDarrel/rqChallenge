package com.example.rqchallenge.employees;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController implements IEmployeeController {

    private final IEmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("getAllEmployees");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        log.info("getEmployeesByNameSearch: {}", searchString);
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("getEmployeeById: {}", id);
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("getHighestSalaryOfEmployees");
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("getTopTenHighestEarningEmployeeNames");
        return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        var name = employeeInput.get("name");
        var salary = employeeInput.get("salary");
        var age = employeeInput.get("age");
        if (!(name instanceof String && salary instanceof String && age instanceof String)) {
            log.error("error in request parameters: {} {} {}", name, salary, age);
            return ResponseEntity.badRequest().build();
        }
        log.info("createEmployee: {} {} {}", name, salary, age);

        return ResponseEntity.ok(employeeService.createEmployee(name.toString(), salary.toString(), age.toString()));
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        log.info("deleteEmployeeById: {}", id);
        return ResponseEntity.ok("deleted");
    }
}
