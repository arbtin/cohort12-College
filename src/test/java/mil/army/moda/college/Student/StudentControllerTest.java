package mil.army.moda.college.Student;

import mil.army.moda.college.institution.Institution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}