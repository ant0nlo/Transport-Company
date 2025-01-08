package main.dao.validations;

import main.entity.Client;
import main.entity.Company;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientValidationTest {

    private final Validator validator;

    public ClientValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidClient() {
        Client client = new Client();
        client.setName("ValidName");
        client.setContactInfo("valid@example.com");
        client.setDeleted(false);
        client.setCompany(new Company());

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid client.");
    }

    @Test
    public void testClientNameBlank() {
        Client client = new Client();
        client.setName(" ");
        client.setContactInfo("valid@example.com");
        client.setDeleted(false);
        client.setCompany(new Company());

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        assertTrue(violations.iterator().next().getMessage().contains("Name cannot be empty"));
    }

    @Test
    public void testClientNameTooShort() {
        Client client = new Client();
        client.setName("A");
        client.setContactInfo("valid@example.com");
        client.setDeleted(false);
        client.setCompany(new Company());

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Name must be between 2 and 100 characters"));
    }

    @Test
    public void testClientNameTooLong() {
        Client client = new Client();
        client.setName("A".repeat(101));
        client.setContactInfo("valid@example.com");
        client.setDeleted(false);
        client.setCompany(new Company());

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Name must be between 2 and 100 characters"));
    }

    @Test
    public void testContactInfoBlank() {
        Client client = new Client();
        client.setName("ValidName");
        client.setContactInfo(" ");
        client.setDeleted(false);
        client.setCompany(new Company());

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Contact information cannot be empty"));
    }

    @Test
    public void testContactInfoTooLong() {
        Client client = new Client();
        client.setName("ValidName");
        client.setContactInfo("A".repeat(256));
        client.setDeleted(false);
        client.setCompany(new Company());

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Contact information cannot exceed 255 characters"));
    }

}