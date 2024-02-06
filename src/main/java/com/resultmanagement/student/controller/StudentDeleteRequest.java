package com.resultmanagement.student.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDeleteRequest {
    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private Integer rollNumber;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer grade;
}
