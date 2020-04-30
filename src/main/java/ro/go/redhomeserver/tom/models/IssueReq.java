package ro.go.redhomeserver.tom.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "IssueRequest")
@Table(name = "issue_req")
public class IssueReq implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String description;

    @ManyToOne()
    @JoinColumn(name = "FK_employee")
    private Account account;

    public IssueReq(String description, Account account) {
        this.description = description;
        this.account = account;
    }

}
