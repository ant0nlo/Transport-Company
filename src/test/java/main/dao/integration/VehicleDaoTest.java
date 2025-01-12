package main.dao.integration;

import main.configuration.HibernateUtil;
import main.dao.CompanyDAO;
import main.dao.VehicleDAO;
import main.dto.CompanyDTO;
import main.dto.VehicleDTO;
import main.entity.VehicleType;
import main.entity.Qualification;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleDaoTest {

    private VehicleDAO vehicleDAO;
    private CompanyDAO companyDAO;
    private Long companyId;

    @BeforeAll
    public void setUp() {
        vehicleDAO = new VehicleDAO();
        companyDAO = new CompanyDAO();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            CompanyDTO testCompany = new CompanyDTO("Test Company", "123 Main Street", BigDecimal.valueOf(100000));
            companyId = companyDAO.createCompany(testCompany);

            transaction.commit();
        }
    }

    @AfterEach
    void delPrevious() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            // Обновяваме всички служители с isDeleted = false на true
            session.createQuery("UPDATE Vehicle SET isDeleted = true WHERE isDeleted = false").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testCreateVehicle() {
        VehicleDTO vehicleDTO = new VehicleDTO(VehicleType.BUS, 4, "TEST123", Qualification.PASSENGERS_12_PLUS);
        Long vehicleId = vehicleDAO.createVehicle(vehicleDTO, companyId);
        assertNotNull(vehicleId, "Vehicle should be created successfully.");
    }

    @Test
    void testGetVehicle() {
        VehicleDTO vehicleDTO = new VehicleDTO(VehicleType.BUS, 4, "TEST1231", Qualification.PASSENGERS_12_PLUS);

        Long vehicleId = vehicleDAO.createVehicle(vehicleDTO, companyId);
        VehicleDTO fetchedVehicleDTO = vehicleDAO.getVehicle(vehicleId);

        assertNotNull(fetchedVehicleDTO, "Vehicle should be fetched successfully.");
        assertEquals("TEST1231", fetchedVehicleDTO.getRegistrationNumber(), "Registration number should match.");
    }

    @Test
    void testUpdateVehicle() {
        VehicleDTO vehicleDTO = new VehicleDTO(VehicleType.BUS, 12, "1234", Qualification.PASSENGERS_12_PLUS);

        Long vehicleId = vehicleDAO.createVehicle(vehicleDTO, companyId);

        VehicleDTO updatedVehicleDTO = new VehicleDTO(VehicleType.BUS, 12, "4321", Qualification.PASSENGERS_12_PLUS);

        vehicleDAO.updateVehicle(updatedVehicleDTO, vehicleId);

        assertNotNull(updatedVehicleDTO, "Vehicle should be updated.");
        assertEquals("4321", vehicleDAO.getVehicle(vehicleId).getRegistrationNumber(), "Updated registration number should match.");
    }

    @Test
    void testDeleteVehicle() {
        VehicleDTO vehicleDTO = new VehicleDTO(VehicleType.BUS, 12, "32234", Qualification.PASSENGERS_12_PLUS);
        Long vehicleId = vehicleDAO.createVehicle(vehicleDTO, companyId);
        vehicleDAO.deleteVehicle(vehicleId);

        // Опитваме да вземем изтритото превозно средство и проверяваме дали се хвърля изключение
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vehicleDAO.getVehicle(vehicleId);
        });

        assertTrue(exception.getMessage().contains("Vehicle with ID " + vehicleId + " does not exist or is deleted"));
    }

    @Test
    void testGetAllVehicles() {
        VehicleDTO vehicleDTO1 = new VehicleDTO(VehicleType.BUS, 12, "2334", Qualification.PASSENGERS_12_PLUS);
        vehicleDAO.createVehicle(vehicleDTO1, companyId);

        VehicleDTO vehicleDTO2 = new VehicleDTO(VehicleType.CAR, 5, "3334", null);
        vehicleDAO.createVehicle(vehicleDTO2, companyId);

        List<VehicleDTO> allVehicles = vehicleDAO.getAllVehicles();
        assertEquals(2, allVehicles.size());
    }

    @Test
    void testGetVehicleByRegistrationNumber() {
        // First, create a vehicle to retrieve
        VehicleDTO newVehicleDTO = new VehicleDTO(VehicleType.BUS, 50, "BUS1234", Qualification.PASSENGERS_12_PLUS);
        vehicleDAO.createVehicle(newVehicleDTO, companyId);

        VehicleDTO retrievedVehicle = vehicleDAO.getVehicleByRegistrationNumber("BUS1234");
        assertNotNull(retrievedVehicle, "Vehicle should be retrieved successfully.");
        assertEquals("BUS1234", retrievedVehicle.getRegistrationNumber(), "Registration number should match.");

        // Test retrieving a non-existent vehicle
        VehicleDTO nullVehicle = vehicleDAO.getVehicleByRegistrationNumber("NONEXISTENT123");
        assertNull(nullVehicle, "No vehicle should be found with a non-existent registration number.");
    }

    @AfterAll
    public void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createQuery("DELETE FROM Vehicle").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();

            session.flush();
            transaction.commit();
        }
    }
}