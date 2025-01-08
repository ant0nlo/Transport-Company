package main.dao.integration;

import main.configuration.HibernateUtil;
import main.dao.ClientDAO;
import main.dao.CompanyDAO;
import main.dao.EmployeeDAO;
import main.dao.VehicleDAO;
import main.dto.*;
import main.entity.Qualification;
import main.entity.VehicleType;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyDaoTest {

    private CompanyDAO companyDAO;

    @BeforeEach
    void setUp() {
        companyDAO = new CompanyDAO();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("UPDATE Company SET isDeleted = true WHERE isDeleted = false").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterEach
    void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    @Test
    public void testCreateCompany() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Test Company");
        companyDTO.setRevenue(new BigDecimal(100000.00));
        companyDTO.setAddress("123 Main Street");

        Long companyId = companyDAO.createCompany(companyDTO);
        assertNotNull(companyId);

        CompanyDTO retrievedCompany = companyDAO.getCompanyById(companyId);
        assertEquals("Test Company", retrievedCompany.getName());
        assertEquals(0, retrievedCompany.getRevenue().compareTo(new BigDecimal("100000.00")));
        assertEquals("123 Main Street", retrievedCompany.getAddress());
    }

    @Test
    public void testGetCompanyById_NotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            companyDAO.getCompanyById(12L);
        });

        assertEquals("Company with ID 12 does not exist.", exception.getMessage());
    }

    @Test
    public void testUpdateCompany() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Initial Company");
        companyDTO.setRevenue(new BigDecimal(100000.00));
        companyDTO.setAddress("123 Main Street");

        Long companyId = companyDAO.createCompany(companyDTO);
        assertNotNull(companyId);

        CompanyDTO updateDTO = new CompanyDTO();
        updateDTO.setName("Updated Company");
        updateDTO.setRevenue(new BigDecimal(150000.00));
        updateDTO.setAddress("124 Main Street");

        companyDAO.updateCompany(updateDTO, companyId);

        CompanyDTO updatedCompany = companyDAO.getCompanyById(companyId);
        assertEquals("Updated Company", updatedCompany.getName());
        assertEquals(0, updatedCompany.getRevenue().compareTo(new BigDecimal("150000.00")));
        assertEquals("124 Main Street", updatedCompany.getAddress());

    }

    @Test
    public void testDeleteCompany() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Deletable Company");
        companyDTO.setRevenue(new BigDecimal(10000.00));
        companyDTO.setAddress("123 Main Street");

        Long companyId = companyDAO.createCompany(companyDTO);
        assertNotNull(companyId);

        companyDAO.deleteCompany(companyId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            companyDAO.getCompanyById(companyId);
        });

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    public void testGetAllCompanies() {
        CompanyDTO company1 = new CompanyDTO("Company A", "Sofia, Bulgaria", BigDecimal.valueOf(200000));
        CompanyDTO company2 = new CompanyDTO("Company B", "Sofia, Bulgaria", BigDecimal.valueOf(200000));

        Long companyId1 = companyDAO.createCompany(company1);
        Long companyId2 = companyDAO.createCompany(company2);
        assertNotNull(companyId1);
        assertNotNull(companyId2);

        List<CompanyDTO> companies = companyDAO.getAllCompanies();
        assertEquals(2, companies.size());
    }

    @Test
    public void testGetCompaniesSortedByRevenue() {
        CompanyDTO company1 = new CompanyDTO("Company A", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
        CompanyDTO company2 = new CompanyDTO("Company B", "Sofia, Bulgaria", BigDecimal.valueOf(200000));
        CompanyDTO company3 = new CompanyDTO("Company C", "Sofia, Bulgaria", BigDecimal.valueOf(150000));

        Long companyId1 = companyDAO.createCompany(company1);
        Long companyId2 = companyDAO.createCompany(company2);
        Long companyId3 = companyDAO.createCompany(company3);

        List<CompanyDTO> companies = companyDAO.getCompaniesSortedByRevenue();
        assertEquals(3, companies.size());

        assertEquals("Company B", companies.get(0).getName());
        assertEquals("Company C", companies.get(1).getName());
        assertEquals("Company A", companies.get(2).getName());
    }

    @Test
    public void testGetCompaniesSortedByName() {
        CompanyDTO company1 = new CompanyDTO("Zeta", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
        CompanyDTO company2 = new CompanyDTO("Alpha", "Sofia, Bulgaria", BigDecimal.valueOf(200000));
        CompanyDTO company3 = new CompanyDTO("Beta", "Sofia, Bulgaria", BigDecimal.valueOf(150000));

        Long companyId1 = companyDAO.createCompany(company1);
        Long companyId2 = companyDAO.createCompany(company2);
        Long companyId3 = companyDAO.createCompany(company3);

        List<CompanyDTO> companies = companyDAO.getCompaniesSortedByName();
        assertEquals(3, companies.size());

        assertEquals("Alpha", companies.get(0).getName()); // Alphabetical order
        assertEquals("Beta", companies.get(1).getName());
        assertEquals("Zeta", companies.get(2).getName());
    }

    @Test
    public void testGetCompanyEmployees() {
        CompanyDTO companyDTO = new CompanyDTO("Company A", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
        Long companyId = companyDAO.createCompany(companyDTO);

        EmployeeDTO employeeDTO = new EmployeeDTO("D. Ivanov", Set.of(Qualification.MILITARY_CARGO), 3000);
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.createEmployee(employeeDTO, companyId);

        Set<EmployeeDTO> employees = companyDAO.getCompanyEmployees(companyId);

        assertNotNull(employees);
        assertEquals(1, employees.size());
    }

    @Test
    public void testGetCompanyClients() {
        CompanyDTO companyDTO = new CompanyDTO("Company A", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
        Long companyId = companyDAO.createCompany(companyDTO);

        ClientDTO clientDTO = new ClientDTO("Ivan", "ivan@example.com");
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.createClient(clientDTO, companyId);

        Set<ClientDTO> clients = companyDAO.getCompanyClients(companyId);

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    @Test
    public void testGetCompanyVehicles() {
        CompanyDTO companyDTO = new CompanyDTO("Company A", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
        Long companyId = companyDAO.createCompany(companyDTO);

        VehicleDTO vehicleDTO = new VehicleDTO(VehicleType.MILITARY_VEHICLE, 4, "SF440205AB", Qualification.MILITARY_CARGO);
        VehicleDAO vehicleDAO = new VehicleDAO();
        vehicleDAO.createVehicle(vehicleDTO, companyId);

        Set<VehicleDTO> vehicles = companyDAO.getCompanyVehicles(companyId);

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
    }

    @Test
    public void testGetTotalRevenueByCompany() {
        CompanyDTO companyDTO = new CompanyDTO("Company A", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
        Long companyId = companyDAO.createCompany(companyDTO);

        double totalRevenue = companyDAO.getTotalRevenueByCompany(companyId);

        assertEquals(0.0, totalRevenue);
    }

    @Test
    public void testGetTotalRevenueByCompany_NoTransports() {
        CompanyDTO companyDTO = new CompanyDTO("Company A", "Sofia, Bulgaria", BigDecimal.valueOf(100000));
        Long companyId = companyDAO.createCompany(companyDTO);

        double totalRevenue = companyDAO.getTotalRevenueByCompany(companyId);
        assertEquals(0.0, totalRevenue);
    }

    @AfterAll
    public static void shutDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            // Изчистване на всички записи в таблиците
            session.createQuery("delete from Transport").executeUpdate();
            session.createNativeQuery("DELETE FROM employee_qualification").executeUpdate();
            session.createQuery("delete from Employee").executeUpdate();
            session.createQuery("delete from Client").executeUpdate();
            session.createQuery("delete from Vehicle").executeUpdate();
            session.createQuery("delete from Company").executeUpdate();

            session.getTransaction().commit();
        }
        HibernateUtil.shutdown();
    }


}