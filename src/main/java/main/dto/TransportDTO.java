package main.dto;

import java.time.LocalDate;

public class TransportDTO {
    private String startLocation;
    private String endLocation;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private String cargoDescription;
    private double cargoWeight;
    private double price;
    private boolean isPaid;

    // Конструктор
    public TransportDTO() {
    }
    public TransportDTO(String startLocation, String endLocation, LocalDate departureDate,
                        LocalDate arrivalDate, String cargoDescription, double cargoWeight, double price, boolean isPaid) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.cargoDescription = cargoDescription;
        this.cargoWeight = cargoWeight;
        this.price = price;
        this.isPaid = isPaid;
    }

    // Гетъри и сетъри
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

    @Override
    public String toString() {
        return "TransportDTO{" +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", cargoDescription='" + cargoDescription + '\'' +
                ", cargoWeight=" + cargoWeight +
                ", price=" + price +
                ", isPaid=" + isPaid +
                '}';
    }
}