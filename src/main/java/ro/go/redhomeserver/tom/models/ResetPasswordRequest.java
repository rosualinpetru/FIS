package ro.go.redhomeserver.tom.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "ResetPassReq")
@Table(name = "reset_password_request")
public class ResetPasswordRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "FK_account")
    private Account account;
    private String token;
    private Date expirationDate;

    public ResetPasswordRequest(Account account, String token, Date expirationDate) {
        this.account = account;
        this.token = token;
        this.expirationDate = expirationDate;
    }
}
