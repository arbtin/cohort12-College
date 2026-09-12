package mil.army.moda.college.institution;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
public class Institution {
    @Id
    private Long id;
    @NotNull
    private String name;

    public Institution(String name) {
        this.name = name;
    }

    public Institution() {
    }

    public Long getId() {
        return id;
    }

    public Institution setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Institution setName(String name) {
        this.name = name;
        return this;
    }
}
