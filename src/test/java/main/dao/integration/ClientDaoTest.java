package main.dao.integration;

import main.configuration.HibernateUtil;
import main.dao.ClientDAO;
import main.dao.CompanyDAO;
import main.dto.ClientDTO;
import main.dto.CompanyDTO;
import main.exception.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientDaoTest {

    private ClientDAO clientDAO;
    private CompanyDAO companyDAO;

    @BeforeAll
    public void setUp() {
        clientDAO = new ClientDAO();
        companyDAO = new CompanyDAO();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.getTransaction().commit();
        }
    }

    @BeforeEach
    void cleanUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Client").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testCreateClient() {
        CompanyDTO companyDTO = new CompanyDTO("Test Company", "Sofia, Bulgaria", null);
        Long companyId = companyDAO.createCompany(companyDTO);

        ClientDTO clientDTO = new ClientDTO("Test Client", "testclient@gmail.com");
        Long clientId = clientDAO.createClient(clientDTO, companyId);

        assertNotNull(clientId);

        ClientDTO retrievedClient = clientDAO.getClient(clientId);
        assertEquals("Test Client", retrievedClient.getName());
        assertEquals("testclient@gmail.com", retrievedClient.getContactInfo());
    }

    @Test
    public void testGetClient_NotFound() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            clientDAO.getClient(999L);
        });
        assertEquals("Client with ID 999 does not exist or is deleted.", exception.getMessage());
    }

    @Test
    public void testUpdateClient() {
        CompanyDTO companyDTO = new CompanyDTO("Test Company", "Sofia, Bulgaria", null);
        Long companyId = companyDAO.createCompany(companyDTO);

        ClientDTO clientDTO = new ClientDTO("Original Client", "original@gmail.com");
        Long clientId = clientDAO.createClient(clientDTO, companyId);

        ClientDTO updatedClientDTO = new ClientDTO("Updated Client", "updated@gmail.com");
        clientDAO.updateClient(updatedClientDTO, clientId);

        ClientDTO retrievedClient = clientDAO.getClient(clientId);
        assertEquals("Updated Client", retrievedClient.getName());
        assertEquals("updated@gmail.com", retrievedClient.getContactInfo());
    }

    @Test
    public void testDeleteClient() {
        CompanyDTO companyDTO = new CompanyDTO("Test Company", "Sofia, Bulgaria", null);
        Long companyId = companyDAO.createCompany(companyDTO);

        ClientDTO clientDTO = new ClientDTO("Deletable Client", "deletable@gmail.com");
        Long clientId = clientDAO.createClient(clientDTO, companyId);

        clientDAO.deleteClient(clientId);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            clientDAO.getClient(clientId);
        });
        assertEquals("Client with ID " + clientId + " does not exist or is deleted.", exception.getMessage());
    }

    @Test
    public void testGetAllClients() {
        CompanyDTO companyDTO = new CompanyDTO("Test Company", "Sofia, Bulgaria", null);
        Long companyId = companyDAO.createCompany(companyDTO);

        ClientDTO client1 = new ClientDTO("Client 1", "client1@gmail.com");
        ClientDTO client2 = new ClientDTO("Client 2", "client2@gmail.com");

        clientDAO.createClient(client1, companyId);
        clientDAO.createClient(client2, companyId);

        List<ClientDTO> clients = clientDAO.getAllClients();
        assertEquals(2, clients.size());

        assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Client 1")));
        assertTrue(clients.stream().anyMatch(c -> c.getName().equals("Client 2")));
    }

    @AfterAll
    public void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createQuery("DELETE FROM Client").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();

            session.flush();
            transaction.commit();
        }
    }
}