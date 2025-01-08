package main.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Contact information cannot be empty")
    @Size(max = 255, message = "Contact information cannot exceed 255 characters")
    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // Релации с други таблици
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "client")
    private Set<Transport> transports;

    // Конструктори
    public Client() {}

    public Client(String name, String contactInfo, Company company) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.company = company;
        this.isDeleted = false;
    }

    // Гетъри и сетъри
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
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
        Client client = (Client) o;
        return Objects.equals(getClientId(), client.getClientId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClientId());
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", companyId=" + (company != null ? company.getCompanyId() : null) +
                ", isDeleted=" + isDeleted +
                '}';
    }
}