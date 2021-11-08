package ro.go.redhomeserver.tom.models;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
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

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResetPasswordRequest that = (ResetPasswordRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return getClass().hashCode();
    }
}
