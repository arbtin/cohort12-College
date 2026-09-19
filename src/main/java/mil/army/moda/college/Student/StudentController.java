package mil.army.moda.college.Student;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Post
    @PostMapping
    public Student enroll(@RequestBody Student student){
        return studentService.enrollStudent(student);
    }

    // Get by Id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // Get All Mapping
    @GetMapping
    @ResponseStatus()
    public ResponseEntity<List<Student>> getAllStudents() {
        return  ResponseEntity.ok(studentService.getAllStudents());
    }

    // Patch Mapping
    @PatchMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    // Delete Mapping
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }

}
