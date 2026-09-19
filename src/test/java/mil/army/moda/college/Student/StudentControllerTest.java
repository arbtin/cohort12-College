package mil.army.moda.college.Student;

import mil.army.moda.college.Institution.Institution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {

    @MockitoBean
    private StudentService mockStudentService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void shouldEnrollStudentInInstitution() throws Exception {
        Institution institution = new Institution("ACC");
        institution.setId(1L);
        Student newStudent = new Student("Bob", institution);

        when(mockStudentService.enrollStudent(any(Student.class))).thenReturn(newStudent);

        mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(matchesPattern("Bob")))
        .andExpect(jsonPath("$.institution.name").value("ACC"));

        verify(mockStudentService).enrollStudent(any(Student.class));
    }

    @Test
    void shouldGetStudentById() throws Exception {
        Institution institution = new Institution("ACC");
        institution.setId(1L);
        Student newStudent = new Student("Bob", institution);
        newStudent.setId(1L);

        when(mockStudentService.getStudentById(newStudent.getId())).thenReturn(newStudent);

        mockMvc.perform(get("/api/students/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockStudentService, times(1)).getStudentById(newStudent.getId());
    }

    @Test
    void shouldGetAllStudents() throws Exception {
        Institution institution = new Institution("Air School");
        institution.setId(1L);

        Student newStudent = new Student("Aircraft School", institution);
        newStudent.setId(1L);
        Student heloSchool = new Student("Bob", institution);

        when(mockStudentService.getAllStudents()).thenReturn(List.of(newStudent, heloSchool));

        mockMvc.perform(get("/api/students").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockStudentService, times(1)).getAllStudents();
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        Institution institution = new Institution("Air School");
        institution.setId(1L);

        Student newStudent = new Student("Bob", institution);
        newStudent.setId(1L);

        when(mockStudentService.updateStudent(eq(1L), any(Student.class))).thenReturn(newStudent);

        mockMvc.perform(patch("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(matchesPattern("Bob")));

        verify(mockStudentService, times(1)).updateStudent(eq(1L), any(Student.class));
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        Institution institution = new Institution("Air School");
        institution.setId(1L);

        Student newStudent = new Student("Bob", institution);
        newStudent.setId(1L);

        doNothing().when(mockStudentService).deleteStudentById(anyLong());

        mockMvc.perform(delete("/api/students/{id}", newStudent.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(mockStudentService, times(1)).deleteStudentById(newStudent.getId());
    }
}