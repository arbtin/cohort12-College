package mil.army.moda.college.institution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InstitutionController.class)
class InstitutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstitutionService mockInstitutionService;

    @Autowired
    JsonMapper jsonMapper;

    @Test
    void makeNewInstitution() throws Exception {
        Institution airSchool = new Institution("Air School");
        airSchool.setId(1L);

        when(mockInstitutionService.createInstitution(any(Institution.class))).thenReturn(airSchool);

        mockMvc.perform(post("/api/institutions").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(airSchool)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(matchesPattern("Air School")));

        verify(mockInstitutionService, times(1)).createInstitution(any(Institution.class));
    }

    @Test
    void shouldGetInstitutionById() throws Exception {
        Institution airSchool = new Institution("Aircraft School");
        airSchool.setId(1L);

        when(mockInstitutionService.getInstitutionById(airSchool.getId())).thenReturn(airSchool);

        mockMvc.perform(get("/api/institutions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockInstitutionService, times(1)).getInstitutionById(airSchool.getId());
    }

    @Test
    void shouldGetAllInstitutions() throws Exception {
        Institution airSchool = new Institution("Aircraft School");
        airSchool.setId(1L);
        Institution heloSchool = new Institution("Helo School");

        when(mockInstitutionService.getAllInstitutions()).thenReturn(List.of(airSchool, heloSchool));

        mockMvc.perform(get("/api/institutions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockInstitutionService, times(1)).getAllInstitutions();
    }

    @Test
    void shouldUpdateInstitution() throws Exception {
        Institution airSchool = new Institution("Air School");
        airSchool.setId(1L);

        when(mockInstitutionService.updateInstitution(eq(1L), any(Institution.class))).thenReturn(airSchool);

        mockMvc.perform(patch("/api/institutions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(airSchool)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(matchesPattern("Air School")));

        verify(mockInstitutionService, times(1)).updateInstitution(eq(1L), any(Institution.class));
    }

    @Test
    void shouldDeleteInstitution() throws Exception {
        Institution airSchool = new Institution("Aircraft School");
        airSchool.setId(1L);

        doNothing().when(mockInstitutionService).deleteInstitutionById(anyLong());

        mockMvc.perform(delete("/api/institutions/{id}", airSchool.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(mockInstitutionService, times(1)).deleteInstitutionById(airSchool.getId());
    }

}