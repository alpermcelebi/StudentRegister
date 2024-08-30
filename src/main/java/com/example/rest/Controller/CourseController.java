package com.example.rest.Controller;

import com.example.rest.Models.Course;
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
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.addCourse(course));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {

        Course course = courseService.getCourseById(id);

        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

    }
    @GetMapping("/{id}/students")
    public ResponseEntity<?> getStudentsByCourseId(@PathVariable Long id) {
        try {
            List<Student> students = courseService.getStudentsByCourseId(id);
            return ResponseEntity.ok(students);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

	@PutMapping("/{id}")
	public Course updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
		return courseService.updateCourse(id, updatedCourse);
	}


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

}
