package com.resultmanagement.student.controller;

import com.resultmanagement.student.repo.Status;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentResponse {
    private Integer rollNumber;
    private String name;
    private String fatherName;
    private Integer grade;
    private Status status;
    private Instant createdOn;
    private Instant updatedOn;
}
