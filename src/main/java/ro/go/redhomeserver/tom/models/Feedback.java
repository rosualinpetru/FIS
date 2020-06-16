package ro.go.redhomeserver.tom.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
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
}
