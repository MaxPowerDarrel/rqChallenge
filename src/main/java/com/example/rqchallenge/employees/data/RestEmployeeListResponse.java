package com.example.rqchallenge.employees.data;

import com.example.rqchallenge.employees.Employee;
import lombok.Data;

import java.util.List;

@Data
public class RestEmployeeListResponse {
    private String status;
    private List<Employee> data;
}
