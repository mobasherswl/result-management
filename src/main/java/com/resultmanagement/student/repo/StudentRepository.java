package com.resultmanagement.student.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, Integer> {
    Mono<Student> findByRollNumberAndStatus(int rollNumber, Status status);

    Mono<Student> findByRollNumber(int rollNumber);
}
