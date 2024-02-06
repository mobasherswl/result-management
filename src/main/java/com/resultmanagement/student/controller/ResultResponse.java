package com.resultmanagement.student.controller;

import com.resultmanagement.student.repo.Remarks;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultResponse {
    private Integer rollNumber;
    private Integer totalMarks;
    private Integer obtainedMarks;
    private Integer grade;
    private Remarks remarks;
    private Integer positionInClass;
    private Instant createdOn;
    private Instant updatedOn;
}
