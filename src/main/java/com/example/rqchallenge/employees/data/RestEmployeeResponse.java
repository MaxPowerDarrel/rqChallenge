package com.example.rqchallenge.employees.data;

import com.example.rqchallenge.employees.Employee;
import lombok.Data;

@Data
public class RestEmployeeResponse {
    private String status;
    private Employee data;
}
