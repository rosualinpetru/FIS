package ro.go.redhomeserver.tom.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PendingIssue {
    private String id;
    private String departmentName;
    private String name;
    private String description;
}
