package ro.go.redhomeserver.tom.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PendingIssue {
    private int id;
    private String depName;
    private String name;
    private String description;
}
