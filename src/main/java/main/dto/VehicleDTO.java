package main.dto;

import main.entity.Qualification;
import main.entity.VehicleType;

public class VehicleDTO {
    private VehicleType type;
    private int capacity;
    private String registrationNumber;
    private Qualification requiredQualification;

    // Конструктор
    public VehicleDTO(VehicleType type, int capacity, String registrationNumber, Qualification requiredQualification) {
        this.type = type;
        this.capacity = capacity;
        this.registrationNumber = registrationNumber;
        this.requiredQualification = requiredQualification;
    }

    // Гетъри и сетъри
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

    public Qualification getRequiredQualification() {
        return requiredQualification;
    }

    public void setRequiredQualification(Qualification requiredQualification) {
        this.requiredQualification = requiredQualification;
    }

    @Override
    public String toString() {
        return "VehicleDTO{" +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", requiredQualification=" + requiredQualification +
                '}';
    }
}