package ro.go.redhomeserver.tom.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Account")
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String username;
    private String password;
    private String salt;

    @OneToOne
    @JoinColumn(name = "FK_employee")
    private Employee employee;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<IssueReq> issueReqs= new HashSet<>();

    @OneToMany(mappedBy = "account_req", cascade = CascadeType.ALL)
    private Set<HolidayReq> sentHolidayReqs= new HashSet<>();

    @OneToMany(mappedBy = "account_tl", cascade = CascadeType.ALL)
    private Set<HolidayReq> receivedHolidayReqs= new HashSet<>();

    @OneToMany(mappedBy = "tl", cascade = CascadeType.ALL)
    private Set<Account> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "FK_tl")
    private Account tl;

    public Account() {
    }

    public Account(String username, String password, String salt, Employee employee, Account tl) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.employee = employee;
        this.tl = tl;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Set<IssueReq> getIssueReqs() {
        return issueReqs;
    }

    public Set<HolidayReq> getSentHolidayReqs() {
        return sentHolidayReqs;
    }

    public Set<HolidayReq> getReceivedHolidayReqs() {
        return receivedHolidayReqs;
    }

    public Set<Account> getMembers() {
        return members;
    }

    public Account getTl() {
        return tl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return getId() == account.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmployee(), getTl());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", employee=" + employee +
                ", tl=" + tl +
                '}';
    }
}
