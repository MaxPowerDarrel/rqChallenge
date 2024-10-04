package com.example.rqchallenge.employees;

import com.example.rqchallenge.data.DataRetrievalException;
import com.example.rqchallenge.employees.data.IEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultEmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        return getAllEmployees()
                .stream()
                .filter(employee -> employee.getEmployeeName().contains(searchString))
                .toList();
    }

    @Override
    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findEmployeeById(id);
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        return getAllEmployees()
                .stream()
                .filter(employee -> employee.getEmployeeSalary() != null && !employee.getEmployeeSalary().isBlank() && employee.getEmployeeSalary().matches("^\\d+$"))
                .map(employee -> Integer.parseInt(employee.getEmployeeSalary()))
                .max(Integer::compareTo)
                .orElse(0);
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        return getAllEmployees()
                .stream()
                .filter(employee -> employee.getEmployeeSalary() != null && !employee.getEmployeeSalary().isBlank() && employee.getEmployeeSalary().matches("^\\d+$"))
                .sorted((e1, e2) -> Integer.parseInt(e2.getEmployeeSalary()) - Integer.parseInt(e1.getEmployeeSalary())) //order is important to ensure the list is in descending order
                .limit(10)
                .map(Employee::getEmployeeName)
                .toList();
    }

    @Override
    public Employee createEmployee(String name, String salary, String age) {
        var employee = new Employee();
        employee.setEmployeeName(name);
        employee.setEmployeeSalary(salary);
        employee.setEmployeeAge(age);
        return employeeRepository.saveEmployee(employee)
                .orElseThrow(() -> new DataRetrievalException("Unable to save employee record"));
    }

    @Override
    public void deleteEmployeeById(String id) {
        employeeRepository.deleteEmployeeById(id);
    }
}
