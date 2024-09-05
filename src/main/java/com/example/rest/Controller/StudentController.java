package com.example.rest.Controller;

import com.example.rest.Models.Course;
import com.example.rest.Models.ErrorResponse;
import com.example.rest.Models.Student;
import com.example.rest.Services.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to create student", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<?> registerStudentToCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            studentService.registerStudentToCourse(studentId, courseId);
            return ResponseEntity.ok("Course registered successfully");
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<?> deregisterStudentFromCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            studentService.deregisterStudentFromCourse(studentId, courseId);
            return ResponseEntity.ok("Course deregistered successfully");
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    @GetMapping("/{studentId}/courses")
    public ResponseEntity<?> getCoursesByStudentId(@PathVariable Long studentId) {
        try {
            List<Course> courses = studentService.getCoursesByStudentId(studentId);
            return ResponseEntity.ok(courses);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        try {
            Student student = studentService.updateStudent(id, updatedStudent);
            return ResponseEntity.ok(student);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            if (studentService.deleteStudent(id)) {
                return ResponseEntity.ok("Student with ID: " + id + " deleted.");
            } else {
                ErrorResponse errorResponse = new ErrorResponse("Student with ID: " + id + " not found.", HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR    );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}