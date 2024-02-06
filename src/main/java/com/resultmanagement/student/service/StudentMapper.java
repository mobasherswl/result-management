package com.resultmanagement.student.service;

import com.resultmanagement.student.controller.StudentAddRequest;
import com.resultmanagement.student.controller.StudentResponse;
import com.resultmanagement.student.repo.Student;
import org.mapstruct.Mapper;

@Mapper
public interface StudentMapper {
    Student toStudent(StudentAddRequest studentServiceRequest);

    StudentResponse toStudentResponse(Student student);
}
