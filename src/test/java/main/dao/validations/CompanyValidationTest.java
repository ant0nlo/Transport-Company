package main.dao.validations;

import main.entity.Company;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyValidationTest {

    private final Validator validator;

    public CompanyValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidCompany() {
        Company company = new Company();
        company.setName("ValidName");
        company.setAddress("123 Valid Address");
        company.setRevenue(BigDecimal.valueOf(100000.00));
        company.setDeleted(false);

        Set<ConstraintViolation<Company>> violations = validator.validate(company);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid company.");
    }

    @Test
    public void testCompanyNameNull() {
        Company company = new Company();
        company.setName(null); // Invalid name
        company.setAddress("123 Valid Address");
        company.setRevenue(BigDecimal.valueOf(100000.00));
        company.setDeleted(false);

        Set<ConstraintViolation<Company>> violations = validator.validate(company);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Company name cannot be null"));
    }

    @Test
    public void testCompanyNameNotStartingWithCapital() {
        Company company = new Company();
        company.setName("invalidName"); // Name does not start with capital letter
        company.setAddress("123 Valid Address");
        company.setRevenue(BigDecimal.valueOf(100000.00));
        company.setDeleted(false);

        Set<ConstraintViolation<Company>> violations = validator.validate(company);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("must start with a capital letter"));
    }

    @Test
    public void testRevenueNegative() {
        Company company = new Company();
        company.setName("ValidName");
        company.setAddress("123 Valid Address");
        company.setRevenue(BigDecimal.valueOf(-100000.00)); // Negative revenue
        company.setDeleted(false);

        Set<ConstraintViolation<Company>> violations = validator.validate(company);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Revenue must be a positive value"));
    }

    @Test
    public void testAddressBlank() {
        Company company = new Company();
        company.setName("ValidName");
        company.setAddress(" "); // Blank address
        company.setRevenue(BigDecimal.valueOf(100000.00));
        company.setDeleted(false);

        Set<ConstraintViolation<Company>> violations = validator.validate(company);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Address cannot be blank"));
    }

    @Test
    public void testAddressTooLong() {
        Company company = new Company();
        company.setName("ValidName");
        company.setAddress("A".repeat(256));
        company.setRevenue(BigDecimal.valueOf(100000.00));
        company.setDeleted(false);

        Set<ConstraintViolation<Company>> violations = validator.validate(company);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("must not exceed 255 characters"));
    }
}
