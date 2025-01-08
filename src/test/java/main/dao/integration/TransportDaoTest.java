package main.dao.integration;

import main.dao.*;
import main.dto.*;
import main.entity.Qualification;
import main.configuration.HibernateUtil;
import main.entity.VehicleType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransportDaoTest {

    private Long companyId;
    private Long clientId;
    private Long employeeId;
    private Long vehicleId;
    private TransportDAO transportDAO;

    @BeforeAll
    void setup() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            CompanyDTO companyDTO = new CompanyDTO("TestCompany", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
            CompanyDAO companyDAO = new CompanyDAO();
            companyId = companyDAO.createCompany(companyDTO);

            ClientDTO clientDTO = new ClientDTO("Ivan", "ivan@example.com");
            ClientDAO clientDAO = new ClientDAO();
            clientId = clientDAO.createClient(clientDTO, companyId);

            EmployeeDTO employeeDTO = new EmployeeDTO("D. Ivanov", Set.of(Qualification.MILITARY_CARGO), 3000);
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employeeId = employeeDAO.createEmployee(employeeDTO, companyId);

            VehicleDTO vehicleDTO = new VehicleDTO(VehicleType.MILITARY_VEHICLE, 4, "SF440205AB", Qualification.MILITARY_CARGO);
            VehicleDAO vehicleDAO = new VehicleDAO();
            vehicleId = vehicleDAO.createVehicle(vehicleDTO, companyId);

            transportDAO = new TransportDAO();
            transaction.commit();
        }
    }

    @AfterEach
    void delPrevious() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            // Обновяваме всички служители с isDeleted = false на true
            session.createQuery("UPDATE Transport SET isDeleted = true WHERE isDeleted = false").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testCreateTransport() {
        TransportDTO transportDTO = new TransportDTO(
                "Sofia", "Varna", LocalDate.of(2024, 12, 24),
                LocalDate.of(2024, 12, 25), "Table",
                2000, 200, true
        );

        Assertions.assertDoesNotThrow(() ->
                transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId)
        );
    }

    @Test
    void testGetTransport() {
        TransportDTO transportDTO = new TransportDTO(
                "Sofia", "Plovdiv", LocalDate.of(2024, 12, 26),
                LocalDate.of(2024, 12, 27), "Chairs",
                1500, 150, false
        );

        Long transportId = transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId);
        TransportDTO retrievedTransport = transportDAO.getTransport(transportId); // ID-то трябва да е съществуващо

        Assertions.assertEquals("Sofia", retrievedTransport.getStartLocation());
        Assertions.assertEquals("Plovdiv", retrievedTransport.getEndLocation());
    }

    @Test
    void testGetAllTransports() {
        TransportDTO transportDTO1 = new TransportDTO(
                "Sofia", "Burgas", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Electronics",
                3000, 300, true
        );
        Long transportId1 = transportDAO.createTransport(transportDTO1, companyId, clientId, employeeId, vehicleId);
        TransportDTO transportDTO2 = new TransportDTO(
                "Sofia", "Burgas", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Electronics",
                3000, 300, true
        );
        Long transportId2 = transportDAO.createTransport(transportDTO2, companyId, clientId, employeeId, vehicleId);

        List<TransportDTO> transports = transportDAO.getAllTransports();
        Assertions.assertEquals(2, transports.size());
    }

    @Test
    void testUpdateTransport() {
        TransportDTO updatedTransportDTO = new TransportDTO(
                "Sofia", "Burgas", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Electronics",
                3000, 300, true
        );

        Long transportId = transportDAO.createTransport(updatedTransportDTO, companyId, clientId, employeeId, vehicleId);

        transportDAO.updateTransport(transportId, updatedTransportDTO); // ID-то трябва да съществува
        TransportDTO updatedTransport = transportDAO.getTransport(transportId);

        Assertions.assertEquals("Burgas", updatedTransport.getEndLocation());
        Assertions.assertEquals("Electronics", updatedTransport.getCargoDescription());
    }

    @Test
    void testDeleteTransport() {
        TransportDTO transportDTO = new TransportDTO(
                "Sofia", "Burgas", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Electronics",
                3000, 300, true
        );
        Long transportId = transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId);
        transportDAO.deleteTransport(transportId);
        Assertions.assertThrows(IllegalArgumentException.class, () -> transportDAO.getTransport(1L));
    }

    @Test
    void testGetTransportsByDestination() {
        TransportDTO transportDTO = new TransportDTO(
                "Sofia", "Burgas", LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2), "Electronics",
                3000, 300, true
        );
        Long transportId = transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId);
        List<TransportDTO> transports = transportDAO.getTransportsByDestination("Varna");
        Assertions.assertTrue(transports.isEmpty());
    }

    @Test
    void testGetTotalRevenue() {
        TransportDTO transportDTO = new TransportDTO(
                "Sofia", "Plovdiv", LocalDate.of(2024, 12, 26),
                LocalDate.of(2024, 12, 27), "Chairs",
                1500, 150, true
        );

        Long transportId = transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId);

        double totalRevenue = transportDAO.getTotalRevenue();
        Assertions.assertEquals(150, totalRevenue );
    }

    @Test
    void testGetTotalTransports() {
        TransportDTO transportDTO = new TransportDTO(
                "Sofia", "Plovdiv", LocalDate.of(2024, 12, 26),
                LocalDate.of(2024, 12, 27), "Chairs",
                1500, 150, false
        );

        Long transportId = transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId);

        long totalTransports = transportDAO.getTotalTransports();
        Assertions.assertEquals(1, totalTransports);
    }

    @Test
    void testGetTotalRevenueByDriver() {
        TransportDTO transportDTO = new TransportDTO(
                "Sofia", "Plovdiv", LocalDate.of(2024, 12, 26),
                LocalDate.of(2024, 12, 27), "Chairs",
                1500, 150, true
        );

        Long transportId = transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId);

        double totalRevenue = transportDAO.getTotalRevenueByDriver(employeeId);
        Assertions.assertEquals(150, totalRevenue);
    }

    @AfterAll
    public void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createNativeQuery("DELETE FROM employee_qualification").executeUpdate();
            session.createQuery("DELETE FROM Transport").executeUpdate();
            session.createQuery("DELETE FROM Employee").executeUpdate();
            session.createQuery("DELETE FROM Client").executeUpdate();
            session.createQuery("DELETE FROM Vehicle").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();

            session.flush();
            transaction.commit();
        }
    }
}