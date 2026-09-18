package mil.army.moda.college.Student;

import mil.army.moda.college.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService mockStudentService;

    @Test
    void shouldCreateStudentWithInstitution() {
        Institution institution = new Institution("ACC");
        institution.setId(1L);
        Student student = new Student("Bob", institution);

        when(studentRepository.save(student)).thenReturn(student);

        Student result = mockStudentService.enrollStudent(student);

        assertThat(result).isEqualTo(student);
        verify(studentRepository).save(student);
    }

}