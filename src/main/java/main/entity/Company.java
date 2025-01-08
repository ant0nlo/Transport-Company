    package main.entity;
    import java.math.BigDecimal;
    import java.util.*;
    import javax.persistence.*;
    import javax.validation.constraints.*;

    @Entity
    @Table(name = "company")
    public class Company {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "companyId", nullable = false, updatable = false)
        private Long companyId;

        @NotNull(message = "Company name cannot be null")
        @Size(min = 1, max = 20, message = "Company name must be between 1 and 100 characters")
        @Pattern(regexp = "^[A-Z].*", message = "Company name must start with a capital letter")
        @Column(name = "name", nullable = false, length = 20)
        private String name;

        @NotBlank(message = "Address cannot be blank")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        @Column(name = "address", length = 255)
        private String address;

        @DecimalMin(value = "0.00", message = "Revenue must be a positive value")
        @Digits(integer = 15, fraction = 2, message = "Revenue must have up to 15 digits and 2 decimal places")
        @Column(name = "revenue", precision = 15, scale = 2)
        private BigDecimal revenue;

        @Column(name = "is_deleted", nullable = false)
        private boolean isDeleted = false;

        // Релации с други таблици
        @OneToMany(mappedBy = "company")
        private Set<Vehicle> vehicles;

        @OneToMany(mappedBy = "company")
        private Set<Employee> employees;

        @OneToMany(mappedBy = "company")
        private Set<Transport> transports;

        @OneToMany(mappedBy = "company")
        private  Set<Client> clients;

        // Конструктори, гетъри и сетъри
        public Company() {
        }

        public Company(String name, String address, BigDecimal revenue) {
            this.name = name;
            this.address = address;
            this.revenue = revenue;
            this.isDeleted = false;
        }

        public Long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Long companyId) {
            this.companyId = companyId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }

        public Set<Vehicle> getVehicles() {
            return vehicles;
        }

        public void setVehicles(Set<Vehicle> vehicles) {
            this.vehicles = vehicles;
        }

        public Set<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(Set<Employee> employees) {
            this.employees = employees;
        }

        public Set<Transport> getTransports() {
            return transports;
        }

        public void setTransports(Set<Transport> transports) {
            this.transports = transports;
        }

        public Set<Client> getClients() {
            return clients;
        }

        public void setClients(Set<Client> clients) {
            this.clients = clients;
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
            Company company = (Company) o;
            return Objects.equals(getCompanyId(), company.getCompanyId());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getCompanyId());
        }

        @Override
        public String toString() {
            return "Company{" +
                    "companyId=" + companyId +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", revenue=" + revenue +
                    ", isDeleted=" + isDeleted +
                    '}';
        }
    }