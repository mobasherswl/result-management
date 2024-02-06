package com.resultmanagement.student.service;

import com.resultmanagement.student.controller.StudentAddRequest;
import com.resultmanagement.student.controller.StudentResponse;
import com.resultmanagement.student.repo.Status;
import com.resultmanagement.student.repo.Student;
import com.resultmanagement.student.repo.StudentRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudentService {
    final StudentRepository studentRepository;
    final StudentServiceMapper studentServiceMapper;

    public Mono<StudentResponse> addStudent(final StudentAddRequest request) {
        final Student student = studentServiceMapper.toStudent(request);

        student.setStatus(Status.ACTIVE);
        student.setCreatedOn(Instant.now());
        student.setUpdatedOn(student.getCreatedOn());

        return studentRepository
                .findByRollNumberAndStatus(request.getRollNumber(), Status.ACTIVE)
                .switchIfEmpty(studentRepository.save(student))
                .map(studentServiceMapper::toStudentServiceResponse);
    }
}
