package com.resultmanagement.student.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultAddRequest {
    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private Integer rollNumber;

    @NotNull private Integer totalMarks;
    @NotNull private Integer obtainedMarks;
}
