package main.dao.validations;

import main.entity.Transport;
import main.entity.Company;
import main.entity.Vehicle;
import main.entity.Employee;
import main.entity.Client;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransportValidationTest {

    private final Validator validator;

    public TransportValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidTransport() {
        Transport transport = new Transport();
        transport.setStartLocation("New York");
        transport.setEndLocation("Los Angeles");
        transport.setDepartureDate(LocalDate.now().plusDays(1));
        transport.setArrivalDate(LocalDate.now().plusDays(2));
        transport.setCargoDescription("Electronics");
        transport.setCargoWeight(1000.0);
        transport.setPrice(5000.0);
        transport.setPaid(false);
        transport.setDeleted(false);
        transport.setVehicle(new Vehicle());
        transport.setDriver(new Employee());
        transport.setCompany(new Company());
        transport.setClient(new Client());

        Set<ConstraintViolation<Transport>> violations = validator.validate(transport);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid transport.");
    }

    @Test
    public void testStartLocationBlank() {
        Transport transport = new Transport();
        transport.setStartLocation(" "); // Blank start location
        transport.setEndLocation("Los Angeles");
        transport.setDepartureDate(LocalDate.now().plusDays(1));
        transport.setArrivalDate(LocalDate.now().plusDays(2));
        transport.setCargoDescription("Electronics");
        transport.setCargoWeight(1000.0);
        transport.setPrice(5000.0);
        transport.setPaid(false);
        transport.setDeleted(false);
        transport.setVehicle(new Vehicle());
        transport.setDriver(new Employee());
        transport.setCompany(new Company());
        transport.setClient(new Client());

        Set<ConstraintViolation<Transport>> violations = validator.validate(transport);

        assertTrue(violations.iterator().next().getMessage().contains("Start location cannot be blank"));
    }

    @Test
    public void testEndLocationBlank() {
        Transport transport = new Transport();
        transport.setStartLocation("New York");
        transport.setEndLocation(" "); // Blank end location
        transport.setDepartureDate(LocalDate.now().plusDays(1));
        transport.setArrivalDate(LocalDate.now().plusDays(2));
        transport.setCargoDescription("Electronics");
        transport.setCargoWeight(1000.0);
        transport.setPrice(5000.0);
        transport.setPaid(false);
        transport.setDeleted(false);
        transport.setVehicle(new Vehicle());
        transport.setDriver(new Employee());
        transport.setCompany(new Company());
        transport.setClient(new Client());

        Set<ConstraintViolation<Transport>> violations = validator.validate(transport);

        assertTrue(violations.iterator().next().getMessage().contains("End location cannot be blank"));
    }

    @Test
    public void testDepartureDateNull() {
        Transport transport = new Transport();
        transport.setStartLocation("New York");
        transport.setEndLocation("Los Angeles");
        transport.setDepartureDate(null); // Null departure date
        transport.setArrivalDate(LocalDate.now().plusDays(2));
        transport.setCargoDescription("Electronics");
        transport.setCargoWeight(1000.0);
        transport.setPrice(5000.0);
        transport.setPaid(false);
        transport.setDeleted(false);
        transport.setVehicle(new Vehicle());
        transport.setDriver(new Employee());
        transport.setCompany(new Company());
        transport.setClient(new Client());

        Set<ConstraintViolation<Transport>> violations = validator.validate(transport);
        assertTrue(violations.iterator().next().getMessage().contains("must not be null"));
    }

    @Test
    public void testNegativeCargoWeight() {
        Transport transport = new Transport();
        transport.setStartLocation("New York");
        transport.setEndLocation("Los Angeles");
        transport.setDepartureDate(LocalDate.now().plusDays(1));
        transport.setArrivalDate(LocalDate.now().plusDays(2));
        transport.setCargoDescription("Electronics");
        transport.setCargoWeight(-1000.0); // Negative cargo weight
        transport.setPrice(5000.0);
        transport.setPaid(false);
        transport.setDeleted(false);
        transport.setVehicle(new Vehicle());
        transport.setDriver(new Employee());
        transport.setCompany(new Company());
        transport.setClient(new Client());

        Set<ConstraintViolation<Transport>> violations = validator.validate(transport);
        assertTrue(violations.iterator().next().getMessage().contains("Cargo weight must be positive"));
    }

    @Test
    public void testNegativePrice() {
        Transport transport = new Transport();
        transport.setStartLocation("New York");
        transport.setEndLocation("Los Angeles");
        transport.setDepartureDate(LocalDate.now().plusDays(1));
        transport.setArrivalDate(LocalDate.now().plusDays(2));
        transport.setCargoDescription("Electronics");
        transport.setCargoWeight(1000.0);
        transport.setPrice(-5000.0); // Negative price
        transport.setPaid(false);
        transport.setDeleted(false);
        transport.setVehicle(new Vehicle());
        transport.setDriver(new Employee());
        transport.setCompany(new Company());
        transport.setClient(new Client());

        Set<ConstraintViolation<Transport>> violations = validator.validate(transport);
        assertTrue(violations.iterator().next().getMessage().contains("Price cannot be negative"));
    }
}