package ro.go.redhomeserver.tom.models;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "IssueRequest")
@Table(name = "issue_req")
public class IssueReq {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String description;

    @ManyToOne()
    @JoinColumn(name = "FK_employee")
    private Account account;

    public IssueReq() {
    }

    public IssueReq(String description, Account account) {
        this.description = description;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueReq issueReq = (IssueReq) o;
        return getId() == issueReq.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getAccount());
    }

    @Override
    public String toString() {
        return "IssueReq{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", account=" + account +
                '}';
    }
}
