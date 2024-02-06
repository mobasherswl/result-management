package com.resultmanagement.student.service;

import com.resultmanagement.student.controller.StudentAddRequest;
import com.resultmanagement.student.controller.StudentResponse;
import com.resultmanagement.student.repo.Student;
import org.mapstruct.Mapper;

@Mapper
public interface StudentServiceMapper {
    Student toStudent(StudentAddRequest studentServiceRequest);

    StudentResponse toStudentServiceResponse(Student student);
}
