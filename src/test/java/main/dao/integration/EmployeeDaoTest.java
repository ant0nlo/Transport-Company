package main.dao.integration;

import main.configuration.HibernateUtil;
import main.dao.CompanyDAO;
import main.dao.EmployeeDAO;
import main.dto.EmployeeDTO;
import main.entity.Qualification;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import main.dto.*;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeDaoTest {

    private EmployeeDAO employeeDAO;
    private Long companyId;


    @BeforeAll
    public void setUpDatabase() {
        employeeDAO = new EmployeeDAO();
        CompanyDAO companyDAO = new CompanyDAO();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            CompanyDTO testCompany = new CompanyDTO("Test Company", "123 Main Street", BigDecimal.valueOf(100000));
            companyId = companyDAO.createCompany(testCompany);
            session.getTransaction().commit();
        }
    }

    @BeforeEach
    void setUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createQuery("UPDATE Employee SET isDeleted = true WHERE isDeleted = false").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @AfterEach
    void delPrevious() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            // Обновяваме всички служители с isDeleted = false на true
            session.createQuery("UPDATE Employee SET isDeleted = true WHERE isDeleted = false")
                    .executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Test
    public void testSaveEmployee() {
        Set<Qualification> qualifications = Set.of(Qualification.PASSENGERS_12_PLUS);

        EmployeeDTO employeeDTO = new EmployeeDTO("John Doe", qualifications,5000.00);
        Long employeeId = employeeDAO.createEmployee(employeeDTO, companyId);
        assertNotNull(employeeId);

        EmployeeDTO retrievedEmployee = employeeDAO.getEmployee(employeeId);
        assertEquals("John Doe", retrievedEmployee.getName());
        assertFalse(retrievedEmployee.getQualification().isEmpty());
        assertEquals(5000.00, retrievedEmployee.getSalary());
    }

    @Test
    public void testGetEmployee_NotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeDAO.getEmployee(999L);
        });

        assertEquals("Employee with ID 999 does not exist.", exception.getMessage());
    }

    @Test
    public void testGetAllEmployees() {
        employeeDAO.createEmployee(new EmployeeDTO("Alice", Set.of(), 8000), companyId);
        employeeDAO.createEmployee(new EmployeeDTO("Bob", Set.of(), 4000), companyId);

        List<EmployeeDTO> employees = employeeDAO.getAllEmployees();
        assertEquals(2, employees.size());
    }

    @Test
    public void testUpdateEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO("Alice", Set.of(), 8000);;
        Long employeeId = employeeDAO.createEmployee(employeeDTO, companyId);

        EmployeeDTO updatedDTO = new EmployeeDTO("Alice Smith", Set.of(), 8000);
        employeeDAO.updateEmployee(updatedDTO, employeeId);

        EmployeeDTO updatedEmployee = employeeDAO.getEmployee(employeeId);
        assertEquals("Alice Smith", updatedEmployee.getName());
        assertTrue(updatedEmployee.getQualification().isEmpty());
        assertEquals(8000, updatedEmployee.getSalary());
    }

    @Test
    public void testDeleteEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO("Bob", Set.of(), 4000);
        Long employeeId = employeeDAO.createEmployee(employeeDTO, companyId);
        employeeDAO.deleteEmployee(employeeId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeDAO.getEmployee(employeeId);
        });

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    public void testGetEmployeesSortedBySalary() {
        employeeDAO.createEmployee(new EmployeeDTO("Alice",  Set.of(), 7000), companyId);
        employeeDAO.createEmployee(new EmployeeDTO("Bob",  Set.of(), 4000), companyId);
        employeeDAO.createEmployee(new EmployeeDTO("Charlie",  Set.of(), 5000), companyId);

        List<EmployeeDTO> employees = employeeDAO.getEmployeesSortedBySalary();
        assertEquals(3, employees.size());

        assertEquals("Alice", employees.get(0).getName()); // Най-висока заплата
        assertEquals("Charlie", employees.get(1).getName()); // Средна заплата
        assertEquals("Bob", employees.get(2).getName()); // Най-ниска заплата
    }

    @AfterAll
    public void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createNativeQuery("DELETE FROM employee_qualification").executeUpdate();
            session.createQuery("DELETE FROM Employee").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();

            session.flush();
            transaction.commit();
        }
    }
}