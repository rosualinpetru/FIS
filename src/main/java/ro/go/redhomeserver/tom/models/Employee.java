package ro.go.redhomeserver.tom.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public Employee() {
    }


    public Employee(String name, String address, String tel, int salary, String email, Date empl_date, Department department) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.salary = salary;
        this.email = email;
        this.empl_date = empl_date;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public int getSalary() {
        return salary;
    }

    public String getEmail() {
        return email;
    }

    public Date getEmpl_date() {
        return empl_date;
    }

    public Department getDepartment() {
        return department;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", salary=" + salary +
                ", email='" + email + '\'' +
                ", empl_date=" + empl_date +
                ", department=" + department +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getId() == employee.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAddress(), getTel(), getSalary(), getEmail(), getEmpl_date(), getDepartment());
    }
}
