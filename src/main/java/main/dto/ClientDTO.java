package main.dto;

public class ClientDTO {
    private String name;
    private String contactInfo;

    // Конструктор
    public ClientDTO(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    // Гетъри и сетъри
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

    @Override
    public String toString() {
        return "ClientDTO{" +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}