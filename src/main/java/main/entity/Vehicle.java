package main.entity;
import javax.persistence.*;
import java.util.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @NotNull(message = "Vehicle type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_qualification")
    private Qualification requiredQualification;

    @Positive(message = "Capacity must be a positive number")
    @Column(name = "capacity", nullable = false)
    private int capacity;

    @NotBlank(message = "Registration number cannot be blank")
    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // Релации с други таблици
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "vehicle")
    private Set<Transport> transports;

    // Конструктори, гетъри и сетъри
    public Vehicle() {}

    public Vehicle(Company company, VehicleType type, int capacity, String registrationNumber, Qualification requiredQualification) {
        this.company = company;
        this.type = type;
        this.capacity = capacity;
        this.registrationNumber = registrationNumber;
        this.requiredQualification = requiredQualification;
        this.isDeleted = false;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public Qualification getRequiredQualification() {
        return requiredQualification;
    }

    public void setRequiredQualification(Qualification requiredQualification) {
        this.requiredQualification = requiredQualification;
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
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(getVehicleId(), vehicle.getVehicleId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVehicleId());
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", companyId=" + (company != null ? company.getCompanyId() : null) +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", requiredQualification=" + requiredQualification +
                ", isDeleted=" + isDeleted +
                '}';
    }
}