package com.resultmanagement.student.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentAddRequest {
    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private Integer rollNumber;

    @NotBlank private String name;
    @NotBlank private String fatherName;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer grade;
}
