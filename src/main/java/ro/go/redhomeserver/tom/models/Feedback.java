package ro.go.redhomeserver.tom.models;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Feedback")
@Table(name = "feedback")
public class Feedback implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "FK_request")
    private HolidayRequest request;

    @ManyToOne
    @JoinColumn(name = "FK_reporter")
    private Account reporter;

    public Feedback(HolidayRequest request, String description, Account reporter) {
        this.reporter = reporter;
        this.description = description;
        this.request = request;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return getClass().hashCode();
    }
}
