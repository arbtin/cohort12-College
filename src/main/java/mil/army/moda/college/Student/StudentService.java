package mil.army.moda.college.Student;

import mil.army.moda.college.institution.Institution;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    public  StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student enrollStudent(Student student) {
        return studentRepository.save(student);
    }

}
