package ro.go.redhomeserver.tom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Employee")
@Table(name = "employee")
public class Employee implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;
    private String address;
    private String tel;
    private int salary;
    private String email;
    private Date emp_date;

    @ManyToOne
    @JoinColumn(name = "FK_department")
    private Department department;

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;

    public Employee(String name, String address, String tel, int salary, String email, Date emp_date, Department department) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.salary = salary;
        this.email = email;
        this.emp_date = emp_date;
        this.department = department;
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    @Generated
    public int hashCode() {
        return getClass().hashCode();
    }
}
