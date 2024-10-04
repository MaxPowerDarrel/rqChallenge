package com.example.rqchallenge.employees;

import com.example.rqchallenge.data.DataRetrievalException;
import com.example.rqchallenge.employees.data.IEmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultEmployeeServiceTest {

    @Mock
    private IEmployeeRepository employeeRepository;
    private DefaultEmployeeService defaultEmployeeService;

    @BeforeEach
    void setUp() {
        defaultEmployeeService = new DefaultEmployeeService(employeeRepository);
    }

    @Test
    void getAllEmployees() {
        when(employeeRepository.findAllEmployees())
                .thenReturn(generateListOfEmployees());
        var employees = defaultEmployeeService.getAllEmployees();
        assertEquals(20, employees.size());
    }

    @Test
    void getEmployeesByNameSearch() {
        when(employeeRepository.findAllEmployees())
                .thenReturn(generateListOfEmployees());
        var employees = defaultEmployeeService.getEmployeesByNameSearch("ar");
        assertEquals(6, employees.size());
    }

    @Test
    void getEmployeeById() {
        var employee = generateListOfEmployees().get(0);
        when(employeeRepository.findEmployeeById(eq("1")))
                .thenReturn(Optional.of(employee));
        assertTrue(defaultEmployeeService.getEmployeeById("1").isPresent());
        assertEquals(employee, defaultEmployeeService.getEmployeeById("1").get());
    }

    @Test
    void getHighestSalaryOfEmployees() {
        when(employeeRepository.findAllEmployees())
                .thenReturn(generateListOfEmployees());
        var highestSalary = defaultEmployeeService.getHighestSalaryOfEmployees();
        assertEquals(49000, highestSalary);
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() {
        when(employeeRepository.findAllEmployees())
                .thenReturn(generateListOfEmployees());
        var employeeNames = defaultEmployeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(10, employeeNames.size());
        assertEquals("Emma Lewis", employeeNames.get(0));
    }

    @Test
    void createEmployee_allGood() {
        var employee = new Employee();
        employee.setEmployeeName("John Doe");
        employee.setEmployeeSalary("30000");
        employee.setEmployeeAge("25");
        when(employeeRepository.saveEmployee(eq(employee)))
                .thenReturn(Optional.of(employee));
        assertEquals(employee, defaultEmployeeService.createEmployee("John Doe", "30000", "25"));
    }

    @Test
    void createEmployee_error() {
        var employee = new Employee();
        employee.setEmployeeName("John Doe");
        employee.setEmployeeSalary("30000");
        employee.setEmployeeAge("25");
        when(employeeRepository.saveEmployee(eq(employee)))
                .thenReturn(Optional.empty());
        assertThrows(DataRetrievalException.class, () -> defaultEmployeeService.createEmployee("John Doe", "30000", "25"));
    }


    @Test
    void deleteEmployeeById() {
        doNothing().when(employeeRepository).deleteEmployeeById(anyString());
        defaultEmployeeService.deleteEmployeeById("1");
        verify(employeeRepository, times(1)).deleteEmployeeById("1");
    }

    private List<Employee> generateListOfEmployees() {
        try (var inputStream = this.getClass().getClassLoader().getResourceAsStream("mock/employees.json")) {
            return new ObjectMapper().readValue(inputStream, new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Employee.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
