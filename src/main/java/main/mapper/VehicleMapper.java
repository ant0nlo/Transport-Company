package main.mapper;

import main.dto.VehicleDTO;
import main.entity.Vehicle;

public class VehicleMapper {

    public static VehicleDTO toDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getType(),
                vehicle.getCapacity(),
                vehicle.getRegistrationNumber(),
                vehicle.getRequiredQualification()
        );
    }

    public static Vehicle toEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(dto.getType());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setRequiredQualification(dto.getRequiredQualification());
        return vehicle;
    }

    public static void updateEntityFromDTO(VehicleDTO dto, Vehicle vehicle) {
            vehicle.setType(dto.getType());
            vehicle.setCapacity(dto.getCapacity());
            vehicle.setRegistrationNumber(dto.getRegistrationNumber());
            vehicle.setRequiredQualification(dto.getRequiredQualification());
    }
}