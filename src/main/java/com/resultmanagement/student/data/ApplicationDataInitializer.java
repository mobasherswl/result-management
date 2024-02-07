package com.resultmanagement.student.data;

import com.resultmanagement.security.User;
import com.resultmanagement.security.UserRepository;
import com.resultmanagement.student.controller.ResultAddRequest;
import com.resultmanagement.student.controller.StudentAddRequest;
import com.resultmanagement.student.service.StudentService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationDataInitializer {
    private final StudentService studentService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        var students =
                studentService
                        .deleteAllStudents()
                        .thenMany(
                                Flux.just(
                                                getStudentAddRequest("name 1", 1, 1, "father 1"),
                                                getStudentAddRequest("name 2", 1, 2, "father 2"),
                                                getStudentAddRequest("name 3", 1, 3, "father 3"))
                                        .flatMap(request -> studentService.addStudent(request)));

        var results =
                studentService
                        .deleteAllResults()
                        .thenMany(
                                Flux.just(
                                                getResultAddRequest(1, 80, 100),
                                                getResultAddRequest(2, 70, 100),
                                                getResultAddRequest(3, 75, 100))
                                        .flatMap(request -> studentService.addResult(request)));

        var users = this.userRepository.deleteAll().thenMany(createUsers());

        students.doOnSubscribe(subscription -> log.info("Data init started"))
                .thenMany(results)
                .thenMany(users)
                .subscribe(
                        user -> log.info("User created" + user.getUsername()),
                        throwable -> log.error("", throwable),
                        () -> log.info("Data init completed"));
    }

    private Flux<User> createUsers() {
        return Flux.just("user", "admin")
                .flatMap(
                        username -> {
                            List<String> roles =
                                    "user".equals(username)
                                            ? Arrays.asList("ROLE_USER")
                                            : Arrays.asList("ROLE_USER", "ROLE_ADMIN");

                            User user =
                                    User.builder()
                                            .roles(roles)
                                            .username(username)
                                            .password(passwordEncoder.encode("password"))
                                            .email(username + "@example.com")
                                            .build();

                            return this.userRepository.save(user);
                        });
    }

    private ResultAddRequest getResultAddRequest(
            final int rollNumber, final int obtainedMarks, final int totalMarks) {
        final ResultAddRequest request = new ResultAddRequest();

        request.setRollNumber(rollNumber);
        request.setObtainedMarks(obtainedMarks);
        request.setTotalMarks(totalMarks);

        return request;
    }

    private StudentAddRequest getStudentAddRequest(
            final String name, final int grade, final int rollNumber, final String fatherName) {
        final StudentAddRequest request = new StudentAddRequest();

        request.setName(name);
        request.setGrade(grade);
        request.setFatherName(fatherName);
        request.setRollNumber(rollNumber);

        return request;
    }
}
