package com.hubstaffmicroservices.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v2/control")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final FreezeConfig freezeConfig;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student saveStudent(
            @RequestBody Student student
    ) {
        return studentService.saveStudent(student);
    }

    @PostMapping("/freeze")
    public String freezeApplication() throws SQLException {
        freezeConfig.setIsFrozen(true);
        return "Application frozen";
    }

    ///
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
}
