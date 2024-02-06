package com.resultmanagement.student.controller;

import com.resultmanagement.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public Mono<ResponseEntity<StudentResponse>> addStudent(
            @Valid @RequestBody final StudentAddRequest request) {
        return studentService.addStudent(request).map(ResponseEntity::ok);
    }

    @DeleteMapping
    public Mono<ResponseEntity<StudentDeleteResponse>> deleteStudent(
            @Valid @RequestBody final StudentDeleteRequest request) {
        return studentService.delete(request).map(ResponseEntity::ok);
    }
}
