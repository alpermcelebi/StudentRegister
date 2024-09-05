package com.example.rest.Controller;

import com.example.rest.Models.Course;
import com.example.rest.Models.ErrorResponse;
import com.example.rest.Models.Student;
import com.example.rest.Services.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addCourse(course));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to create course", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Course not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<?> getStudentsByCourseId(@PathVariable Long id) {
        try {
            List<Student> students = courseService.getStudentsByCourseId(id);
            return ResponseEntity.ok(students);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Course not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to retrieve courses", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        try {
            Course course = courseService.updateCourse(id, updatedCourse);
            return ResponseEntity.ok(course);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Course not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to update course", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to delete course", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @DeleteMapping("/cache")
    public ResponseEntity<String> deleteCache(){
        courseService.deleteCache();
        return ResponseEntity.ok("Cache Deleted");
    }
}
