package mil.army.moda.college.Institution;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {
    private InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }
    //Post mapping
    @PostMapping
    public ResponseEntity<Institution> createInstitution(@RequestBody Institution institution) {
        return ResponseEntity.ok(institutionService.createInstitution(institution));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable Long id) {
        return ResponseEntity.ok(institutionService.getInstitutionById(id));
    }

    // Get All Mapping
    @GetMapping
    @ResponseStatus()
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        return  ResponseEntity.ok(institutionService.getAllInstitutions());
    }

    // Patch Mapping
    @PatchMapping("/{id}")
    public Institution updateInstitution(@PathVariable Long id, @RequestBody Institution institution) {
        return institutionService.updateInstitution(id, institution);
    }

    // Delete Mapping
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstitutionById(@PathVariable Long id) {
        institutionService.deleteInstitutionById(id);
        return ResponseEntity.noContent().build();
    }
}
