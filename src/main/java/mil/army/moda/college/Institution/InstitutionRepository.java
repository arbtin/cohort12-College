package mil.army.moda.college.Institution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    public Institution findByIdAndName(Long id, String name);
}
