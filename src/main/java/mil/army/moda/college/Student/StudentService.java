package mil.army.moda.college.Student;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student enrollStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentService.ResourceNotFoundException("Student not found: " + id));
    }

    public Student getStudentByName(Long id, String name) {
        return studentRepository.findByIdAndName(id, name);
    }

    public Student getAnotherStudent(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            return student;
        }
        throw new StudentService.ResourceNotFoundException("Student not found: " + id);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            updatedStudent.setId(optionalStudent.get().getId());
            return studentRepository.save(updatedStudent);
        }
        throw new StudentService.ResourceNotFoundException("Student not found: " + id);
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

}
