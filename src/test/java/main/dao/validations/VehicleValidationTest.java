package main.dao.validations;

import main.entity.Vehicle;
import main.entity.Company;
import main.entity.Qualification;
import main.entity.VehicleType;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleValidationTest {

    private final Validator validator;

    public VehicleValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.MILITARY_VEHICLE); // Assuming TRUCK is a valid enum value
        vehicle.setRequiredQualification(Qualification.MILITARY_CARGO);
        vehicle.setCapacity(10);
        vehicle.setRegistrationNumber("ABC-123");
        vehicle.setDeleted(false);
        vehicle.setCompany(new Company());

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid vehicle.");
    }

    @Test
    public void testVehicleTypeNull() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(null); // Null type
        vehicle.setRequiredQualification(Qualification.PASSENGERS_12_PLUS);
        vehicle.setCapacity(10);
        vehicle.setRegistrationNumber("ABC-123");
        vehicle.setDeleted(false);
        vehicle.setCompany(new Company());

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Vehicle type cannot be null"));
    }

    @Test
    public void testCapacityNegative() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.VAN);
        vehicle.setRequiredQualification(Qualification.PASSENGERS_12_PLUS);
        vehicle.setCapacity(-10); // Negative capacity
        vehicle.setRegistrationNumber("ABC-123");
        vehicle.setDeleted(false);
        vehicle.setCompany(new Company());

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Capacity must be a positive number"));
    }

    @Test
    public void testRegistrationNumberBlank() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.CAR);
        vehicle.setRequiredQualification(null);
        vehicle.setCapacity(100);
        vehicle.setRegistrationNumber(" "); // Blank registration number
        vehicle.setDeleted(false);
        vehicle.setCompany(new Company());

        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Registration number cannot be blank"));
    }

    @Test
    public void testDuplicateRegistrationNumber() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setType(VehicleType.BUS);
        vehicle1.setRequiredQualification(Qualification.PASSENGERS_12_PLUS);
        vehicle1.setCapacity(50);
        vehicle1.setRegistrationNumber("1234");
        vehicle1.setDeleted(false);
        vehicle1.setCompany(new Company());

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setType(VehicleType.BUS);
        vehicle2.setRequiredQualification(Qualification.PASSENGERS_12_PLUS);
        vehicle2.setCapacity(50);
        vehicle2.setRegistrationNumber("1234"); // Duplicate registration number
        vehicle2.setDeleted(false);
        vehicle2.setCompany(new Company());

        Set<ConstraintViolation<Vehicle>> violations1 = validator.validate(vehicle1);
        Set<ConstraintViolation<Vehicle>> violations2 = validator.validate(vehicle2);

        assertTrue(violations1.isEmpty(), "Expected no validation errors for the first vehicle.");
        assertTrue(violations2.isEmpty(), "Validation does not detect duplicates without database checks.");
    }
}