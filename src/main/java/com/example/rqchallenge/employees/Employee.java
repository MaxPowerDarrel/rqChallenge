package com.example.rqchallenge.employees;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Employee {
    private String id;

    @JsonProperty("employee_name")
    private String employeeName;

    @JsonProperty("employee_salary")
    private String employeeSalary;

    @JsonProperty("employee_age")
    private String employeeAge;

    @JsonProperty("profile_image")
    private String profileImage;
}
