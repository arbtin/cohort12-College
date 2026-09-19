package mil.army.moda.college.Institution;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {

    private InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public Institution createInstitution(Institution institution) {
        return institutionRepository.save(institution);
    }

    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }

    public Institution getInstitutionById(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institution not found: " + id));
    }

    public Institution getInstitutionByName(Long id, String name) {
        return institutionRepository.findByIdAndName(id, name);
    }

    public Institution getAnotherInstitution(Long id) {
        Optional<Institution> optionalInstitution = institutionRepository.findById(id);
        if (optionalInstitution.isPresent()) {
            Institution institution = optionalInstitution.get();

//         Institution institution2 = new Institution(id,
//               optionalInstitution.get().getName(),
//               optionalInstitution.get().getSex(),
//                 optionalInstitution.get().getAge(),
//                 optionalInstitution.get().getWeight(),
//                 optionalInstitution.get().isVaccination()
//         );
            return institution;
        }
        throw new ResourceNotFoundException("Institution not found: " + id);
    }

    public Institution updateInstitution(Long id, Institution updatedInstitution) {
        Optional<Institution> optionalInstitution = institutionRepository.findById(id);
        if (optionalInstitution.isPresent()) {
            updatedInstitution.setId(optionalInstitution.get().getId());
            return institutionRepository.save(updatedInstitution);
        }
        throw new ResourceNotFoundException("Institution not found: " + id);
    }

    public void deleteInstitutionById(Long id) {
        institutionRepository.deleteById(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

}
