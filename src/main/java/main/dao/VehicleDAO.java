package main.dao;

import main.configuration.HibernateUtil;
import main.dto.VehicleDTO;
import main.entity.Company;
import main.entity.Vehicle;
import main.mapper.VehicleMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleDAO {
    public Long createVehicle(VehicleDTO vehicleDTO, Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.get(Company.class, companyId);
            if (company == null) {
                throw new IllegalArgumentException("Company with ID " + companyId + " does not exist.");
            }

            Vehicle vehicle = VehicleMapper.toEntity(vehicleDTO);
            vehicle.setCompany(company); // Свързване с компанията

            Long vehicleId = (Long) session.save(vehicle);
            transaction.commit();
            return  vehicleId;
        }
    }

    public VehicleDTO getVehicle(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Vehicle vehicle = session.get(Vehicle.class, id);

            if (vehicle == null || vehicle.isDeleted()) {
                throw new IllegalArgumentException("Vehicle with ID " + id + " does not exist or is deleted.");
            }

            return VehicleMapper.toDTO(vehicle);
        }
    }

    public List<VehicleDTO> getAllVehicles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Vehicle> vehicles = session.createQuery("from Vehicle v where v.isDeleted = false", Vehicle.class).list();
            return vehicles.stream()
                    .map(VehicleMapper::toDTO) // Преобразуване към DTO
                    .collect(Collectors.toList());
        }
    }

    public void updateVehicle(VehicleDTO vehicleDTO, Long vehicleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Vehicle existingVehicle = session.get(Vehicle.class, vehicleId);
                if (existingVehicle == null || existingVehicle.isDeleted()) {
                    throw new IllegalArgumentException("Vehicle not found with ID: " + vehicleId);
                }

            VehicleMapper.updateEntityFromDTO(vehicleDTO, existingVehicle);
            session.update(existingVehicle);
            transaction.commit();
        }
    }

    public void deleteVehicle(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, id);
            if (vehicle == null || vehicle.isDeleted()) {
                throw new IllegalArgumentException("Vehicle with ID " + id + " does not exist or is already deleted.");
            }
            vehicle.setDeleted(true); // Логическо изтриване
            session.update(vehicle);
            transaction.commit();
        }
    }

    public VehicleDTO getVehicleByRegistrationNumber(String registrationNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Vehicle> cq = cb.createQuery(Vehicle.class);
            Root<Vehicle> root = cq.from(Vehicle.class);

            cq.select(root).where(
                    cb.and(
                            cb.equal(root.get("registrationNumber"), registrationNumber),
                            cb.isFalse(root.get("isDeleted"))
                    )
            );

            Vehicle vehicle = session.createQuery(cq).uniqueResult();
            return vehicle != null ? VehicleMapper.toDTO(vehicle) : null;
        }
    }

       /*
    public boolean canClientDriveVehicle(Long vehicleId, Set<Qualification> clientQualifications) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Vehicle vehicle = session.get(Vehicle.class, vehicleId);

            if (vehicle == null || vehicle.isDeleted()) {
                throw new IllegalArgumentException("Vehicle with ID " + vehicleId + " does not exist or is deleted.");
            }

            if (vehicle.getCapacity() >= 12) {
                return clientQualifications.contains(Qualification.PASSENGERS_12_PLUS);
            }

            return true;
        }
    }
     */
}