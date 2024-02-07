package com.resultmanagement.student.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.resultmanagement.student.controller.ResultAddRequest;
import com.resultmanagement.student.controller.ResultResponse;
import com.resultmanagement.student.controller.StudentAddRequest;
import com.resultmanagement.student.controller.StudentDeleteRequest;
import com.resultmanagement.student.controller.StudentResponse;
import com.resultmanagement.student.repo.Remarks;
import com.resultmanagement.student.repo.Result;
import com.resultmanagement.student.repo.ResultRepository;
import com.resultmanagement.student.repo.Status;
import com.resultmanagement.student.repo.Student;
import com.resultmanagement.student.repo.StudentRepository;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @InjectMocks private StudentService sut;
    @Mock private StudentRepository studentRepository;
    @Mock private StudentMapper studentMapper;
    @Mock private ResultRepository resultRepository;
    @Mock private ResultMapper resultMapper;

    @Test
    void addStudent() {
        final Student student = new Student();
        final StudentAddRequest request = new StudentAddRequest();
        final StudentResponse response = new StudentResponse();
        final ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        request.setRollNumber(1);
        request.setName("name");
        request.setGrade(1);
        request.setFatherName("father");

        when(studentMapper.toStudent(any(StudentAddRequest.class))).thenReturn(student);
        when(studentMapper.toStudentResponse(student)).thenReturn(response);
        when(studentRepository.findByRollNumber(1)).thenReturn(Mono.empty());
        when(studentRepository.save(studentArgumentCaptor.capture()))
                .thenReturn(Mono.just(student));

        sut.addStudent(request)
                .as(StepVerifier::create)
                .consumeNextWith(
                        st -> {
                            Assertions.assertThat(st).isNotNull();
                            Assertions.assertThat(studentArgumentCaptor.getValue().getCreatedOn())
                                    .isNotNull();
                        })
                .verifyComplete();
    }

    @Test
    public void deleteStudent() {
        final StudentDeleteRequest request = new StudentDeleteRequest();
        final Student student = new Student();
        final ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        request.setGrade(1);
        request.setRollNumber(1);
        student.setGrade(1);
        when(studentRepository.findByRollNumberAndStatus(1, Status.ACTIVE))
                .thenReturn(Mono.just(student));
        when(studentRepository.save(studentArgumentCaptor.capture()))
                .thenReturn(Mono.just(student));

        sut.delete(request)
                .as(StepVerifier::create)
                .consumeNextWith(
                        st -> {
                            final Student arg = studentArgumentCaptor.getValue();

                            Assertions.assertThat(st).isNotNull();
                            Assertions.assertThat(arg.getStatus()).isEqualTo(Status.DELETED);
                            Assertions.assertThat(arg.getUpdatedOn()).isNotNull();
                        })
                .verifyComplete();
    }

    @Test
    public void addResult() {
        final ResultAddRequest request = new ResultAddRequest();
        final Student student = new Student();
        final Result result = new Result();
        final ArgumentCaptor<Result> resultArgumentCaptor = ArgumentCaptor.forClass(Result.class);

        request.setRollNumber(1);
        request.setTotalMarks(100);
        request.setObtainedMarks(90);
        student.setGrade(1);
        result.setObtainedMarks(90);

        when(studentRepository.findByRollNumberAndStatus(1, Status.ACTIVE))
                .thenReturn(Mono.just(student));
        when(resultRepository.findTopPositionInTheClass(1, 90)).thenReturn(Mono.empty());
        when(resultRepository.findBottomPositionInTheClass(1)).thenReturn(Mono.empty());
        when(resultMapper.toResult(request)).thenReturn(result);
        when(resultRepository.save(resultArgumentCaptor.capture())).thenReturn(Mono.just(result));
        when(resultRepository.updateClassPositions(eq(90), eq(1), any(Instant.class)))
                .thenReturn(Mono.just(1));
        when(resultMapper.toResultResponse(result)).thenReturn(new ResultResponse());

        sut.addResult(request)
                .as(StepVerifier::create)
                .consumeNextWith(
                        res -> {
                            final Result value = resultArgumentCaptor.getValue();

                            Assertions.assertThat(res).isNotNull();
                            Assertions.assertThat(value).isNotNull();
                            Assertions.assertThat(value.getRemarks()).isEqualTo(Remarks.PASSED);
                            Assertions.assertThat(value.getPositionInClass()).isEqualTo(1);
                        })
                .verifyComplete();
    }
}
