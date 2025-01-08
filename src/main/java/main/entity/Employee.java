package main.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Positive(message = "Salary must be positive")
    @Column(name = "salary", nullable = false)
    private double salary;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @ElementCollection()
    @CollectionTable(name = "employee_qualification", joinColumns = @JoinColumn(name = "employee_id"))
    @Enumerated(EnumType.STRING)
    private Set<Qualification> qualification;

    // Релации с други таблици
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "driver")
    private Set<Transport> transports;

    // Конструктори, гетъри и сетъри
    public Employee() {}

    public Employee(Company company, String name,  Set<Qualification> qualification, double salary) {
        this.company = company;
        this.name = name;
        this.qualification = qualification;
        this.salary = salary;
        this.isDeleted = false;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public Set<Qualification> getQualification() {
        return qualification;
    }

    public void setQualification(Set<Qualification> qualification) {
        this.qualification = qualification;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(getEmployeeId(), employee.getEmployeeId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmployeeId());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", companyId=" + (company != null ? company.getCompanyId() : null) +
                ", name='" + name + '\'' +
                ", qualification='" + qualification + '\'' +
                ", salary=" + salary +
                ", isDeleted=" + isDeleted +
                '}';
    }
}