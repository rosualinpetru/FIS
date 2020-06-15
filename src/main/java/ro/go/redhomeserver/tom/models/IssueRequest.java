package ro.go.redhomeserver.tom.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "IssueRequest")
@Table(name = "issue_request")
public class IssueRequest implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String description;

    @ManyToOne()
    @JoinColumn(name = "FK_account")
    private Account account;

    public IssueRequest(String description, Account account) {
        this.description = description;
        this.account = account;
    }

}
