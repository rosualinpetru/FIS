package ro.go.redhomeserver.tom.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "ResetPassReq")
@Table(name = "reset_pass_req")
public class ResetPassReq {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "FK_account_id")
    private Account account;
    private String token;
    private Date expirationDate;

    public ResetPassReq(Account account, String token, Date expirationDate) {
        this.account = account;
        this.token = token;
        this.expirationDate = expirationDate;
    }
}
