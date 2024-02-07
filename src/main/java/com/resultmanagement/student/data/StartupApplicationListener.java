package com.resultmanagement.student.data;

import com.resultmanagement.student.controller.ResultAddRequest;
import com.resultmanagement.student.controller.StudentAddRequest;
import com.resultmanagement.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private final StudentService studentService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        studentService.addStudent(getStudentAddRequest("name 1", 1, 1, "father 1")).block();
        studentService.addStudent(getStudentAddRequest("name 2", 1, 2, "father 2")).block();
        studentService.addStudent(getStudentAddRequest("name 3", 1, 3, "father 3")).block();

        studentService.addResult(getResultAddRequest(1, 80, 100)).block();
        studentService.addResult(getResultAddRequest(2, 70, 100)).block();
        studentService.addResult(getResultAddRequest(3, 75, 100)).block();
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
