package main.dao;

import main.configuration.HibernateUtil;
import main.entity.*;
import main.dto.*;
import main.mapper.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

public class TransportDAO {

    public Long createTransport(TransportDTO transportDTO, Long companyId, Long clientId, Long employeeId, Long vehicleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.get(Company.class, companyId);
            Client client = session.get(Client.class, clientId);
            Employee employee = session.get(Employee.class, employeeId);
            Vehicle vehicle = session.get(Vehicle.class, vehicleId);

            if (company == null || client == null || employee == null || vehicle == null) {
                throw new IllegalArgumentException("One or more related entities do not exist.");
            }

            if (company.isDeleted() || client.isDeleted() || employee.isDeleted() || vehicle.isDeleted()) {
                throw new IllegalArgumentException("One or more related entities have been deleted.");
            }

            Qualification requiredQualification = vehicle.getRequiredQualification();
            if (requiredQualification != null && !employee.getQualification().contains(requiredQualification)) {
                throw new IllegalArgumentException("Employee does not have the required qualification: " + requiredQualification);
            }

            if (vehicle.getCapacity() >= 12 && !employee.getQualification().contains(Qualification.PASSENGERS_12_PLUS)) {
                throw new IllegalArgumentException("Employee does not have the required qualification: " + Qualification.PASSENGERS_12_PLUS);
            }

            Transport transport = TransportMapper.toEntity(transportDTO);

            transport.setCompany(company);
            transport.setClient(client);
            transport.setDriver(employee);
            transport.setVehicle(vehicle);

            Long transportId = (Long) session.save(transport);
            transaction.commit();
            return transportId;
        }
    }

    public TransportDTO getTransport(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transport transport = session.get(Transport.class, id);
            if (transport == null || transport.isDeleted()) {
                throw new IllegalArgumentException("Transport with ID " + id + " does not exist or is deleted.");
            }
            return TransportMapper.toDTO(transport);
        }
    }

    public List<TransportDTO> getAllTransports() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Transport> transports = session.createQuery("from Transport where isDeleted = false", Transport.class).list();
            return transports.stream()
                    .map(TransportMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public void updateTransport(Long transportId, TransportDTO transportDTO) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Transport existingTransport = session.get(Transport.class, transportId);
            if (existingTransport == null || existingTransport.isDeleted()) {
                throw new IllegalArgumentException("Transport with ID " + transportId + " does not exist.");
            }

            TransportMapper.updateEntityFromDTO(transportDTO, existingTransport);
            existingTransport.setTransportId(existingTransport.getTransportId());
            existingTransport.setCompany(existingTransport.getCompany());
            existingTransport.setClient(existingTransport.getClient());
            existingTransport.setDriver(existingTransport.getDriver());
            existingTransport.setVehicle(existingTransport.getVehicle());

            session.update(existingTransport);
            transaction.commit();
        }
    }

    public void deleteTransport(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Transport transport = session.get(Transport.class, id);
            if (transport == null || transport.isDeleted()) {
                throw new IllegalArgumentException("Transport with ID " + id + " does not exist or is already deleted.");
            }
            transport.setDeleted(true);  // Маркирай като изтрит
            session.update(transport);
            transaction.commit();
        }
    }

    public long getTotalTransports() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(t) from Transport t where t.isDeleted = false";

            Long totalTransports = session.createQuery(hql, Long.class)
                    .getSingleResult();

            return totalTransports != null ? totalTransports : 0L;
        }
    }

    public double getTotalRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select sum(t.price) from Transport t where t.isPaid = true and t.isDeleted = false", Double.class).getSingleResult();
        }
    }

    public List<TransportDTO> getTransportsByDestination(String destination) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Transport> cq = cb.createQuery(Transport.class);
            Root<Transport> root = cq.from(Transport.class);
            Predicate condition = cb.and(
                    cb.equal(root.get("endLocation"), destination),
                    cb.isFalse(root.get("isDeleted"))
            );
            cq.where(condition);
            List<Transport> transports = session.createQuery(cq).getResultList();

            return transports.stream()
                    .map(TransportMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public double getTotalRevenueByDriver(Long driverId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select sum(t.price) from Transport t where t.driver.id = :driverId and t.isPaid = true and t.isDeleted = false";
            Double totalRevenue = session.createQuery(hql, Double.class)
                    .setParameter("driverId", driverId)
                    .getSingleResult();

            return totalRevenue != null ? totalRevenue : 0.0;
        }
    }

    /*
    public double getTotalRevenueByDateRange(String startDate, String endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Парсиране на низовете в Date обекти
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            // HQL заявка
            String hql = "select sum(t.price) from Transport t " +
                    "where t.date >= :startDate and t.date <= :endDate and t.isDeleted = false";

            Double totalRevenue = session.createQuery(hql, Double.class)
                    .setParameter("startDate", start)
                    .setParameter("endDate", end)
                    .getSingleResult();

            return totalRevenue != null ? totalRevenue : 0.0;
        } catch (ParseException e) {
            e.printStackTrace(); // Или хвърляйте грешка в зависимост от нуждите
            return 0.0;
        }
    }

    public List<Object[]> getDriverTransportCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Създаване на HQL заявка за броя на превозите за всеки шофьор
            String hql = "select t.driver.id, count(t) from Transport t " +
                    "where t.isDeleted = false group by t.driver.id";

            List<Object[]> results = session.createQuery(hql).getResultList();

            // Отпечатване на резултатите по начин, който показва стойностите
            for (Object[] result : results) {
                Long driverId = (Long) result[0];
                Long transportCount = (Long) result[1];
                System.out.println("Driver ID: " + driverId + ", Transport Count: " + transportCount);
            }

            return results;
        }
    }

     */

}