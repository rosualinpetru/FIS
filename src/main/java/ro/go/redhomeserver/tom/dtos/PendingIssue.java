package ro.go.redhomeserver.tom.dtos;


import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class PendingIssue {
    private String id;
    private String departmentName;
    private String name;
    private String description;

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PendingIssue that = (PendingIssue) o;
        return Objects.equals(id, that.id) && Objects.equals(departmentName, that.departmentName) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(id, departmentName, name, description);
    }
}
