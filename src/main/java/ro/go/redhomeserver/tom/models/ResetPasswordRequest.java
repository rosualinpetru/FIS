package ro.go.redhomeserver.tom.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "ResetPassReq")
@Table(name = "reset_password_request")
public class ResetPasswordRequest implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

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
