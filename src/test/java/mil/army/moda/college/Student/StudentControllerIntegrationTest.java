package mil.army.moda.college.Student;

import jakarta.transaction.Transactional;
import mil.army.moda.college.Institution.Institution;
import mil.army.moda.college.Institution.InstitutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstitutionService institutionService;

    // Setup test objects
    Student bob;
    Institution acc;

    @BeforeEach
    void setUp() {
        acc = new Institution("ACC");
        bob = new Student("Bob", acc);
    }

    @Test
    public void shouldCreateStudent() throws Exception {
        String bobJson = jsonMapper.writeValueAsString(bob);

        MvcResult savedStudent = mockMvc.perform(
                        post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bobJson))
                .andReturn();
        String expectedType = savedStudent.getRequest().getContentType();
        Student expectedStudent = jsonMapper.readValue(savedStudent.getResponse().getContentAsString(), Student.class);

        assertEquals(expectedType, "application/json");
        assertEquals(expectedStudent.getName(), bob.getName());
        assertEquals(expectedStudent.getInstitution().getName(), bob.getInstitution().getName());
    }

    @Test
    public void shouldGetAllStudents() throws Exception {
        Institution acc = institutionService.createInstitution(new Institution("ACC"));
        Institution ut = institutionService.createInstitution(new Institution("UT"));
        studentService.enrollStudent(new Student("Bob", acc));
        studentService.enrollStudent(new Student("Fred", ut));
        // Assert
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                //.andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[1].name").value("Fred"))
                //.andExpect(jsonPath("$[0].category.id").value(1L))
                .andExpect(jsonPath("$[0].institution.name").value("ACC"))
                .andExpect(jsonPath("$[1].institution.name").value("UT"));
        //.andExpect(jsonPath("$[1].id").value(2L))
    }

    @Test
    public void shouldGetStudentById() throws Exception {
        // Arrange
        Student savedStudent = studentService.enrollStudent(new Student("Fred", new Institution("UTSA")));

        // Act
        mockMvc.perform(get("/api/student/" + savedStudent.getId()))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Fred"))
                //.andExpect(jsonPath("$.category.id").value(1L))
                .andExpect(jsonPath("$.institution.name").value("UTSA"));
    }
}