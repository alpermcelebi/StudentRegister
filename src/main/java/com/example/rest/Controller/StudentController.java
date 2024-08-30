package com.example.rest.Controller;

import com.example.rest.Models.Course;
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
    public Student createStudent(@RequestBody Student student) {


        return studentService.addStudent(student);
    }
    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<String> registerStudentToCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            studentService.registerStudentToCourse(studentId, courseId);
            return ResponseEntity.ok("Course registered successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<String> deregisterStudentFromCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            studentService.deregisterStudentFromCourse(studentId, courseId);
            return ResponseEntity.ok("Course deregistered successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }



    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<Course>> getCoursesByStudentId(@PathVariable Long studentId) {
        List<Course> courses = studentService.getCoursesByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        if(studentService.studentExists(id)) return ResponseEntity.ok(studentService.getStudentById(id));
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with the given id not found.");
        }
    }

    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

	@PutMapping("/{id}")
	public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
		return studentService.updateStudent(id, updatedStudent);
	}


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        try {
            if (studentService.deleteStudent(id)) {
                return ResponseEntity.ok("Student with id: " + id + " deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with id: " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting student: " + e.getMessage());
        }
    }

}