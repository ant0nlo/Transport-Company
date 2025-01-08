package main.dao;

import main.dto.*;
import main.entity.Client;
import main.configuration.HibernateUtil;
import main.entity.*;
import main.mapper.ClientMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.stream.Collectors;

public class ClientDAO {

    public Long createClient(ClientDTO clientDTO, Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Извличаме компанията спрямо ID-то
            Company company = session.get(Company.class, companyId);
            if (company == null) {
                throw new IllegalArgumentException("Company with ID " + companyId + " does not exist.");
            }

            Client client = ClientMapper.toEntity(clientDTO);
            client.setCompany(company);
            Long clientId = (Long) session.save(client);
            transaction.commit();
            return clientId;
        }
    }

    public ClientDTO getClient(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Client client = session.get(Client.class, clientId);
            if (client == null || client.isDeleted()) { // Проверка за isDeleted
                throw new IllegalArgumentException("Client with ID " + clientId + " does not exist or is deleted.");
            }
            return ClientMapper.toDTO(client);
        }
    }

    public List<ClientDTO> getAllClients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Client> clients = session.createQuery("from Client c where c.isDeleted = false", Client.class).list();
            return clients.stream()
                    .map(ClientMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public void updateClient(ClientDTO clientDTO, Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Client existingClient = session.get(Client.class, clientId);
            if (existingClient == null || existingClient.isDeleted()) {
                throw new IllegalArgumentException("Client with ID " + clientId + " does not exist.");
            }

            ClientMapper.updateEntityFromDTO(clientDTO, existingClient);
            session.update(existingClient);
            transaction.commit();
        }
    }

    public void deleteClient(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Client client = session.get(Client.class, clientId);
            if (client == null || client.isDeleted()) {
                throw new IllegalArgumentException("Client with ID " + clientId + " does not exist or is already deleted.");
            }
            client.setDeleted(true);
            session.update(client);

            transaction.commit();
        }
    }
}