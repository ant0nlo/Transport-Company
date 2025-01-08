package main.dto;

import main.entity.Qualification;

import java.util.Set;

public class EmployeeDTO {
    private String name;
    private Set<Qualification> qualification;
    private double salary;

    // Конструктор
    public EmployeeDTO(String name, Set<Qualification> qualification, double salary) {
        this.name = name;
        this.qualification = qualification;
        this.salary = salary;
    }

    // Гетъри и сетъри
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Qualification> getQualification() {
        return qualification;
    }

    public void setQualification(Set<Qualification> qualification) {
        this.qualification = (qualification != null) ? qualification : Set.of();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                ", name='" + name + '\'' +
                ", qualification='" + qualification + '\'' +
                ", salary=" + salary +
                '}';
    }
}