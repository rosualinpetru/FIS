package ro.go.redhomeserver.tom.dtos;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class PendingIssue {
    private String id;
    private String departmentName;
    private String name;
    private String description;
}
