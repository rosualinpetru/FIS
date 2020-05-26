package ro.go.redhomeserver.tom.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ro.go.redhomeserver.tom.models.IssueReq;

@Getter
@Setter
@AllArgsConstructor
public class PendingIssue {


    private int Id;
    private String depName;
    private String emplName;
    private String description;



}
