package main.entity;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.*;

@Entity
@Table(name = "transport")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportId;

    @NotBlank(message = "Start location cannot be blank")
    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @NotBlank(message = "End location cannot be blank")
    @Column(name = "end_location", nullable = false)
    private String endLocation;

    @NotNull
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @NotNull
    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

    @NotBlank(message = "Cargo description cannot be blank")
    @Column(name = "cargo_description")
    private String cargoDescription;

    @Positive(message = "Cargo weight must be positive")
    @Column(name = "cargo_weight")
    private double cargoWeight;

    @PositiveOrZero(message = "Price cannot be negative")
    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    // Релации с други таблици
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee driver;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Конструктори, гетъри и сетъри
    public Transport() {}

    public Transport(Vehicle vehicle, Employee driver, Company company, Client client, String startLocation,
                     String endLocation, LocalDate departureDate, LocalDate arrivalDate,
                     String cargoDescription, double cargoWeight, double price, boolean isPaid) {
        this.vehicle = vehicle;
        this.driver = driver;
        this.company = company;
        this.client = client;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.cargoDescription = cargoDescription;
        this.cargoWeight = cargoWeight;
        this.price = price;
        this.isPaid = isPaid;
        this.isDeleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transport transport = (Transport) o;
        return Objects.equals(getTransportId(), transport.getTransportId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTransportId());
    }

    public Long getTransportId() {
        return transportId;
    }

    public void setTransportId(Long transportId) {
        this.transportId = transportId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public double getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(double cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "transportId=" + transportId +
                ", vehicle=" + vehicle.getRegistrationNumber() +
                ", driver=" + driver.getName() +
                ", company=" + company.getName() +
                ", client=" + client.getName() +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", cargoDescription='" + cargoDescription + '\'' +
                ", cargoWeight=" + cargoWeight +
                ", price=" + price +
                ", isPaid=" + isPaid +
                ", isDeleted=" + isDeleted +
                '}';
    }
}