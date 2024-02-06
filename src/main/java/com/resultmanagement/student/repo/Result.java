package com.resultmanagement.student.repo;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(value = "result")
public class Result {
    @Id private Integer rollNumber;
    private Integer totalMarks;
    private Integer obtainedMarks;
    private Integer grade;
    private Remarks remarks;
    private Integer positionInClass;
    private Instant createdOn;
    private Instant updatedOn;
}
