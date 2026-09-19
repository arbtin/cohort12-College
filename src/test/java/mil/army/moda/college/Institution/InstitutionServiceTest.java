package mil.army.moda.college.Institution;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private InstitutionService mockInstitutionService;

    @Test
    void shouldCreateInstitution() {
        //Arrange
        Institution airSchool = new Institution("Aircraft School");
        airSchool.setId(1L);
        when(institutionRepository.save(airSchool)).thenReturn(airSchool);

        //Act
        Institution result = mockInstitutionService.createInstitution(airSchool);

        //Assert
        assertThat(result).isEqualTo(airSchool);
        verify(institutionRepository).save(airSchool);
    }

    @Test
    void shouldGetAllInstitutions() {
        //Arrange
        Institution airSchool = new Institution("Aircraft School");
        airSchool.setId(1L);
        Institution groundSchool = new Institution("Aircraft School");
        groundSchool.setId(2L);

        when(institutionRepository.findAll()).thenReturn(List.of(airSchool, groundSchool));

        // Act
        List<Institution> result = mockInstitutionService.getAllInstitutions();

        // Assert
        Assertions.assertThat(result).isEqualTo(List.of(airSchool, groundSchool));
        Assertions.assertThat(result.size()).isEqualTo(2);
        //assertEquals();
        verify(institutionRepository, only()).findAll();
    }

    @Test
    void shouldGetInstitutionById() throws ChangeSetPersister.NotFoundException {
        //Arrange
        Institution airSchool = new Institution("Aircraft School");
        airSchool.setId(1L);
        when(institutionRepository.findById(1L)).thenReturn(Optional.of(airSchool));

        //Act
        Institution result = mockInstitutionService.getInstitutionById(airSchool.getId());

        //Assert
        Assertions.assertThat(result).isEqualTo(airSchool);
        verify(institutionRepository, only()).findById(1L);
    }

    @Test
    void shouldCallGetInstitutionByIdReturnNotFound() {
        //Arrange
        when(institutionRepository.findById(2L)).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> mockInstitutionService.getInstitutionById(2L))
                .isInstanceOf(InstitutionService.ResourceNotFoundException.class)
                .hasMessage("Institution not found: 2");

        //Assert
        verify(institutionRepository).findById(2L);
    }

    @Test
    void shouldCallUpdateInstitution() {
        // Arrange
        Institution airSchool = new Institution("Aircraft School");
        Institution updateAirSchool = new Institution("Helo School");
        updateAirSchool.setId(1L);

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(airSchool));
        when(institutionRepository.save(any(Institution.class))).thenReturn(updateAirSchool);

        // Act
        Institution result = mockInstitutionService.updateInstitution(1L, updateAirSchool);
        // UpdateInstitution should have an id now to do a compare against

        Assertions.assertThat(result).isNotNull();

        // This looks at all values
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(updateAirSchool);
        // OR check each value that has changed
        Assertions.assertThat(result.getName()).isEqualTo("Helo School");

        verify(institutionRepository, times(1)).findById(1L);
        verify(institutionRepository).save(updateAirSchool);
        verify(institutionRepository, times(1)).save(refEq(updateAirSchool));
    }

    @Test
    void shouldCallUpdateInstitutionAndNotFindAInstitution() {
        // Arrange
        Institution updatedFluffy = new Institution("Delete Institution");
        // when(institutionRepository.findById(2L)).thenThrow(new InstitutionService.ResourceNotFoundException("Not found"));
        when(institutionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(
                InstitutionService.ResourceNotFoundException.class,
                () -> {
                    mockInstitutionService.updateInstitution(2L, updatedFluffy);
                }
        );
    }

    @Test
    void shouldCallDeleteInstitutionAndReturnNothing() {
        doNothing().when(institutionRepository).deleteById(1L);
        mockInstitutionService.deleteInstitutionById(1L);
        verify(institutionRepository, only()).deleteById(1L);
    }
}