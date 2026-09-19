package mil.army.moda.college.Student;

import mil.army.moda.college.Institution.Institution;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService mockStudentService;

    Institution institution;

    @BeforeEach
    void setUp() {
        institution = new Institution("ACC");
        institution.setId(1L);
    }

    @Test
    void shouldCreateStudentWithInstitution() {
        Student student = new Student("Bob", institution);

        when(studentRepository.save(student)).thenReturn(student);

        Student result = mockStudentService.enrollStudent(student);

        assertThat(result).isEqualTo(student);
        verify(studentRepository).save(student);
    }

    @Test
    void shouldGetAllStudents() {
        //Arrange
        Student bob = new Student("Bob", institution);
        bob.setId(1L);
        Student fred = new Student("Fred", institution);
        fred.setId(2L);

        when(studentRepository.findAll()).thenReturn(List.of(bob, fred));

        // Act
        List<Student> result = mockStudentService.getAllStudents();

        // Assert
        Assertions.assertThat(result).isEqualTo(List.of(bob, fred));
        Assertions.assertThat(result.size()).isEqualTo(2);
        //assertEquals();
        verify(studentRepository, only()).findAll();
    }

    @Test
    void shouldGetStudentById() throws ChangeSetPersister.NotFoundException {
        //Arrange
        Student bob = new Student("Bob", institution);
        bob.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(bob));

        //Act
        Student result = mockStudentService.getStudentById(bob.getId());

        //Assert
        Assertions.assertThat(result).isEqualTo(bob);
        verify(studentRepository, only()).findById(1L);
    }

    @Test
    void shouldCallGetStudentByIdAndReturnNotFound() {
        //Arrange
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> mockStudentService.getStudentById(2L))
                .isInstanceOf(StudentService.ResourceNotFoundException.class)
                .hasMessage("Student not found: 2");

        //Assert
        verify(studentRepository).findById(2L);
    }

    @Test
    void shouldCallUpdateStudent() {
        // Arrange
        Student bob = new Student("Bob", institution);
        bob.setId(1L);

        Student updateBob = new Student("Fred", institution);
        updateBob.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(bob));
        when(studentRepository.save(any(Student.class))).thenReturn(updateBob);

        // Act
        Student result = mockStudentService.updateStudent(1L, updateBob);
        // UpdateStudent should have an id now to do a compare against
        Assertions.assertThat(result).isNotNull();

        // This looks at all values
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(updateBob);
        // OR check each value that has changed
        Assertions.assertThat(result.getName()).isEqualTo("Fred");

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository).save(updateBob);
        verify(studentRepository, times(1)).save(refEq(updateBob));
    }

    @Test
    void shouldDeleteStudentAndNotFindAStudent() {
        // Arrange
        Student updatedBob = new Student("Fred", institution);
        // when(studentRepository.findById(2L)).thenThrow(new StudentService.ResourceNotFoundExcepion("Not found"));
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(
                StudentService.ResourceNotFoundException.class,
                () -> {
                    mockStudentService.updateStudent(2L, updatedBob);
                }
        );
    }

    @Test
    void shouldDeleteStudentAndReturnNothing() {
        doNothing().when(studentRepository).deleteById(1L);
        mockStudentService.deleteStudentById(1L);
        verify(studentRepository, only()).deleteById(1L);
    }
}