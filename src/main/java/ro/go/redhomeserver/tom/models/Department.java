package ro.go.redhomeserver.tom.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "Department")
@Table(name = "department")
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;
}
