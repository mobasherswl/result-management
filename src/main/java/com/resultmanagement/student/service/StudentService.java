package com.resultmanagement.student.service;

import com.resultmanagement.student.controller.ResultAddRequest;
import com.resultmanagement.student.controller.ResultResponse;
import com.resultmanagement.student.controller.StudentAddRequest;
import com.resultmanagement.student.controller.StudentDeleteRequest;
import com.resultmanagement.student.controller.StudentDeleteResponse;
import com.resultmanagement.student.controller.StudentResponse;
import com.resultmanagement.student.repo.Remarks;
import com.resultmanagement.student.repo.Result;
import com.resultmanagement.student.repo.ResultRepository;
import com.resultmanagement.student.repo.Status;
import com.resultmanagement.student.repo.Student;
import com.resultmanagement.student.repo.StudentRepository;
import java.time.Instant;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<StudentResponse> addStudent(final StudentAddRequest request) {
        final Student student = studentMapper.toStudent(request);

        student.setStatus(Status.ACTIVE);
        student.setCreatedOn(Instant.now());
        student.setUpdatedOn(student.getCreatedOn());

        return studentRepository
                .findByRollNumber(request.getRollNumber())
                .switchIfEmpty(studentRepository.save(student))
                .map(studentMapper::toStudentResponse);
    }

    public Mono<StudentDeleteResponse> delete(final StudentDeleteRequest request) {
        return studentRepository
                .findByRollNumberAndStatus(request.getRollNumber(), Status.ACTIVE)
                .filter(student -> request.getGrade() == student.getGrade())
                .flatMap(setStudentStatus(Status.DELETED))
                .flatMap(
                        student -> {
                            final StudentDeleteResponse response = new StudentDeleteResponse();
                            response.setDeleted(true);
                            return Mono.just(response);
                        })
                .defaultIfEmpty(new StudentDeleteResponse());
    }

    public Mono<ResultResponse> addResult(final ResultAddRequest request) {
        return studentRepository
                .findByRollNumberAndStatus(request.getRollNumber(), Status.ACTIVE)
                .flatMap(
                        student ->
                                calculatePositionInClass(request, student)
                                        .map(position -> Tuples.of(student, position)))
                .flatMap(saveResult(request))
                .flatMap(result -> updateClassPositions(result).then(Mono.just(result)))
                .map(resultMapper::toResultResponse);
    }

    public Flux<ResultResponse> retrieveAllResults() {
        return resultRepository.findAll().map(resultMapper::toResultResponse);
    }

    public Mono<ResultResponse> retrieveResultByRollNumber(int rollNumber) {
        return resultRepository.findById(rollNumber).map(resultMapper::toResultResponse);
    }

    private Function<Student, Mono<? extends Student>> setStudentStatus(final Status status) {
        return student -> {
            student.setUpdatedOn(Instant.now());
            student.setStatus(status);

            return studentRepository.save(student);
        };
    }

    private Mono<Integer> updateClassPositions(Result result) {
        return resultRepository.updateClassPositions(result.getObtainedMarks(), 1, Instant.now());
    }

    private Function<Tuple2<Student, Integer>, Mono<? extends Result>> saveResult(
            final ResultAddRequest request) {
        return tuple -> {
            final Result result = resultMapper.toResult(request);
            result.setGrade(tuple.getT1().getGrade());
            result.setRemarks(
                    ((request.getObtainedMarks() * 100) / request.getTotalMarks()) >= 50
                            ? Remarks.PASSED
                            : Remarks.FAILED);
            result.setCreatedOn(Instant.now());
            result.setUpdatedOn(result.getCreatedOn());
            result.setPositionInClass(tuple.getT2());
            return resultRepository.save(result);
        };
    }

    private Mono<Integer> calculatePositionInClass(
            final ResultAddRequest request, final Student student) {
        return resultRepository
                .findTopPositionInTheClass(student.getGrade(), request.getObtainedMarks())
                .switchIfEmpty(
                        resultRepository
                                .findBottomPositionInTheClass(student.getGrade())
                                .map(position -> position + 1)
                                .defaultIfEmpty(1));
    }
}
