package com.example.rqchallenge.employees.data;

import com.example.rqchallenge.data.DataRetrievalException;
import com.example.rqchallenge.employees.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HttpEmployeeRepository implements IEmployeeRepository {
    public static final String RETURNED_A_4_XX_ERROR = "Error retrieving data from dummyApi. Returned a 4xx error";
    public static final String RETURNED_A_5_XX_ERROR = "Error retrieving data from dummyApi. Returned a 5xx error";
    private final RestTemplate dummyApiRestTemplate;

    private static final String EMPLOYEES_URL = "employees";

    @Override
    public List<Employee> findAllEmployees() {
        var response = dummyApiRestTemplate.getForEntity(EMPLOYEES_URL, RestEmployeeListResponse.class);
        return handleEmployeeListResponse(response);
    }

    @Override
    public Optional<Employee> findEmployeeById(String id) {
        var response = dummyApiRestTemplate.getForEntity(EMPLOYEES_URL + "/" + id, RestEmployeeResponse.class);
        return handleEmployeeResponse(response);
    }

    @Override
    public Optional<Employee> saveEmployee(Employee employee) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(3);
        map.add("name", employee.getEmployeeName());
        map.add("age", employee.getEmployeeAge());
        map.add("salary", employee.getEmployeeSalary());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        var response = dummyApiRestTemplate.exchange("create", HttpMethod.POST, entity, RestEmployeeResponse.class);
        return handleEmployeeResponse(response);
    }

    @Override
    public void deleteEmployeeById(String id) {
        dummyApiRestTemplate.delete("delete" + "/" + id);
    }

    private List<Employee> handleEmployeeListResponse(ResponseEntity<RestEmployeeListResponse> response) {
        switch (response.getStatusCode().series()) {
            case SUCCESSFUL -> {
                var body = response.getBody();
                if (body != null && "success".equals(body.getStatus())) {
                    return body.getData();
                } else {
                    throw new DataRetrievalException("Error retrieving data from dummyApi. Returned status: " + (body != null ? body.getStatus() : "null"));
                }
            }
            case CLIENT_ERROR ->
                    throw new DataRetrievalException(RETURNED_A_4_XX_ERROR);
            case SERVER_ERROR ->
                    throw new DataRetrievalException(RETURNED_A_5_XX_ERROR);
            default -> {
                return List.of();
            }
        }
    }

    private Optional<Employee> handleEmployeeResponse(ResponseEntity<RestEmployeeResponse> response) {
        switch (response.getStatusCode().series()) {
            case SUCCESSFUL -> {
                var body = response.getBody();
                if (body != null && "success".equals(body.getStatus())) {
                    return Optional.of(body.getData());
                } else {
                    throw new DataRetrievalException("Error retrieving data from dummyApi. Returned status: " + (body != null ? body.getStatus() : "null"));
                }
            }
            case CLIENT_ERROR ->
                    throw new DataRetrievalException(RETURNED_A_4_XX_ERROR);
            case SERVER_ERROR ->
                    throw new DataRetrievalException(RETURNED_A_5_XX_ERROR);
            default -> {
                return Optional.empty();
            }
        }
    }
}
