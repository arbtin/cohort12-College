package mil.army.moda.college.Student;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student enroll(@RequestBody Student student){
        return studentService.enrollStudent(student);

    }
}
