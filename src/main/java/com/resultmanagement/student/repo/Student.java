package com.resultmanagement.student.repo;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(value = "student")
public class Student {
    @Id private Integer rollNumber;
    private String name;
    private String fatherName;
    private Integer grade;
    private Status status;
    private Instant createdOn;
    private Instant updatedOn;
}
