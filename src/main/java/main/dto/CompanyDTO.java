package main.dto;

import java.math.BigDecimal;

public class CompanyDTO {
    private String name;
    private String address;
    private BigDecimal revenue;

    // Конструктор
    public CompanyDTO() {
    }

    public CompanyDTO( String name, String address, BigDecimal revenue) {
        this.name = name;
        this.address = address;
        this.revenue = revenue;
    }

    // Гетъри и сетъри
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

    @Override
    public String toString() {
        return "CompanyDTO{" +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", revenue=" + revenue +
                '}';
    }
}