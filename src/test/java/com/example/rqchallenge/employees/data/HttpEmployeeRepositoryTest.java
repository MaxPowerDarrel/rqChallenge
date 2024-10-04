package com.example.rqchallenge.employees.data;

import com.example.rqchallenge.data.DataRetrievalException;
import com.example.rqchallenge.employees.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static com.example.rqchallenge.employees.data.HttpEmployeeRepository.RETURNED_A_4_XX_ERROR;
import static com.example.rqchallenge.employees.data.HttpEmployeeRepository.RETURNED_A_5_XX_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpEmployeeRepositoryTest {

    @Mock
    private RestTemplate restTemplate;
    private HttpEmployeeRepository httpEmployeeRepository;

    @BeforeEach
    void setUp() {
        httpEmployeeRepository = new HttpEmployeeRepository(restTemplate);
    }

    @Test
    void findAllEmployees_allGood() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeListResponse.class)))
        .thenReturn(generateEmployeeListResponse(generateListOfEmployees(), "success"));

        var employees = httpEmployeeRepository.findAllEmployees();
        assertEquals(employees.size(), 20);
    }

    @Test
    void findAllEmployees_4xxException() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeListResponse.class)))
                .thenReturn(ResponseEntity.status(400).build());

        assertThrows(DataRetrievalException.class, () -> httpEmployeeRepository.findAllEmployees(), RETURNED_A_4_XX_ERROR);
    }

    @Test
    void findAllEmployees_5xxException() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeListResponse.class)))
                .thenReturn(ResponseEntity.status(500).build());

        assertThrows(DataRetrievalException.class, () -> httpEmployeeRepository.findAllEmployees(), RETURNED_A_5_XX_ERROR);
    }

    @Test
    void findAllEmployees_3xxResponse() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeListResponse.class)))
                .thenReturn(ResponseEntity.status(300).build());

        var employees = httpEmployeeRepository.findAllEmployees();
        assertEquals(employees.size(), 0);
    }

    @Test
    void findAllEmployees_2xxResponseButError() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeListResponse.class)))
                .thenReturn(generateEmployeeListResponse(List.of(), "error"));

        assertThrows(DataRetrievalException.class, () -> httpEmployeeRepository.findAllEmployees(), "Error retrieving data from dummyApi. Returned status: error");
    }

    @Test
    void findEmployeeById_allGood() {
        var employee = generateListOfEmployees().get(0);
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeResponse.class)))
                .thenReturn(generateEmployeeResponse(employee, "success"));

        var employeeFound = httpEmployeeRepository.findEmployeeById("1");
        assertTrue(employeeFound.isPresent());
        assertEquals(employeeFound.get(), employee);
    }

    @Test
    void findEmployeeById_2xxResponseButError() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeResponse.class)))
                .thenReturn(generateEmployeeResponse(null, "error"));

        assertThrows(DataRetrievalException.class, () -> httpEmployeeRepository.findEmployeeById("1"), "Error retrieving data from dummyApi. Returned status: error");
    }

    @Test
    void findEmployeeById_3xxResponse() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeResponse.class)))
                .thenReturn(ResponseEntity.status(300).build());

        var employee = httpEmployeeRepository.findEmployeeById("1");
        assertTrue(employee.isEmpty());
    }

    @Test
    void findEmployeeById_4xxException() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeResponse.class)))
                .thenReturn(ResponseEntity.status(400).build());

        assertThrows(DataRetrievalException.class, () -> httpEmployeeRepository.findEmployeeById("1"), RETURNED_A_4_XX_ERROR);
    }

    @Test
    void findEmployeeById_5xxException() {
        when(restTemplate.getForEntity(anyString(), eq(RestEmployeeResponse.class)))
                .thenReturn(ResponseEntity.status(500).build());

        assertThrows(DataRetrievalException.class, () -> httpEmployeeRepository.findEmployeeById("1"), RETURNED_A_5_XX_ERROR);
    }

    @Test
    void saveEmployee() {
        var employee = generateListOfEmployees().get(0);
        when(restTemplate.exchange(eq("create"), any(), any(), eq(RestEmployeeResponse.class)))
                .thenReturn(generateEmployeeResponse(employee, "success"));

        assertTrue(httpEmployeeRepository.saveEmployee(employee).isPresent());
    }

    @Test
    void deleteEmployeeById() {
        doNothing().when(restTemplate).delete(anyString());
        httpEmployeeRepository.deleteEmployeeById("1");
        verify(restTemplate, times(1)).delete("delete/1");
    }

    private ResponseEntity<RestEmployeeResponse> generateEmployeeResponse(Employee employee, String status) {
        var employeeResponse = new RestEmployeeResponse();
        employeeResponse.setStatus(status);
        employeeResponse.setData(employee);
        return ResponseEntity.ok(employeeResponse);
    }

    private ResponseEntity<RestEmployeeListResponse> generateEmployeeListResponse(List<Employee> employees, String status) {
        var employeeResponse = new RestEmployeeListResponse();
        employeeResponse.setStatus(status);
        employeeResponse.setData(employees);
        return ResponseEntity.ok(employeeResponse);
    }

    private List<Employee> generateListOfEmployees() {
        try (var inputStream = this.getClass().getClassLoader().getResourceAsStream("mock/employees.json")) {
            return new ObjectMapper().readValue(inputStream, new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Employee.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
