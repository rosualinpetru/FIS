package ro.go.redhomeserver.tom.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "Employee")
@Table(name = "employee")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;
    private String address;
    private String tel;
    private int salary;
    private String email;
    private Date empl_date;

    @ManyToOne
    @JoinColumn(name = "FK_department")
    private Department department;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Account account;
}
