package ro.go.redhomeserver.tom.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "Department")
@Table(name = "department")
public class Department implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    public Department(String name) {
        this.name = name;
    }
}
