package main.dao.validations;

import main.entity.Employee;
import main.entity.Company;
import main.entity.Qualification;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeValidationTest {

    private final Validator validator;

    public EmployeeValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidEmployee() {
        Employee employee = new Employee();
        employee.setName("ValidName");
        employee.setSalary(5000.00);
        employee.setDeleted(false);
        employee.setQualification(Set.of(Qualification.PASSENGERS_12_PLUS));
        employee.setCompany(new Company());

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid employee.");
    }

    @Test
    public void testEmployeeNameBlank() {
        Employee employee = new Employee();
        employee.setName(" ");
        employee.setSalary(5000.00);
        employee.setDeleted(false);
        employee.setQualification(Set.of(Qualification.PASSENGERS_12_PLUS));
        employee.setCompany(new Company());

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(messages.contains("Name cannot be empty"));
        assertTrue(messages.contains("Name must be between 2 and 100 characters"));
    }

    @Test
    public void testEmployeeNameTooShort() {
        Employee employee = new Employee();
        employee.setName("A"); // Too short
        employee.setSalary(5000.00);
        employee.setDeleted(false);
        employee.setQualification(Set.of(Qualification.PASSENGERS_12_PLUS));
        employee.setCompany(new Company());

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Name must be between 2 and 100 characters"));
    }

    @Test
    public void testEmployeeNameTooLong() {
        Employee employee = new Employee();
        employee.setName("A".repeat(101)); // Exceeds 100 characters
        employee.setSalary(5000.00);
        employee.setDeleted(false);
        employee.setQualification(Set.of(Qualification.PASSENGERS_12_PLUS));
        employee.setCompany(new Company());

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Name must be between 2 and 100 characters"));
    }

    @Test
    public void testEmployeeSalaryNegative() {
        Employee employee = new Employee();
        employee.setName("ValidName");
        employee.setSalary(-5000.00); // Negative salary
        employee.setDeleted(false);
        employee.setQualification(Set.of(Qualification.PASSENGERS_12_PLUS));
        employee.setCompany(new Company());

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Salary must be positive"));
    }

    @Test
    public void testEmployeeWithoutQualification() {
        Employee employee = new Employee();
        employee.setName("ValidName");
        employee.setSalary(5000.00);
        employee.setDeleted(false);
        employee.setQualification(Set.of()); // No qualifications
        employee.setCompany(new Company());

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        assertTrue(violations.isEmpty(), "No validation errors are expected for an empty qualification set.");
    }

}