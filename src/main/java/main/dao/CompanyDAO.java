package main.dao;

import main.configuration.HibernateUtil;
import main.dto.*;
import main.entity.*;
import main.mapper.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class CompanyDAO {

    public Long createCompany(CompanyDTO companyDTO) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = CompanyMapper.toEntity(companyDTO);
            session.save(company);
            Long companyId = company.getCompanyId();
            System.out.println("Generated company ID: " + companyId);

            transaction.commit();
            return companyId;
        }
    }

    public CompanyDTO getCompanyById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Company company = session.get(Company.class, id);
            if (company == null || company.isDeleted()) {
                throw new IllegalArgumentException("Company with ID " + id + " does not exist.");
            }
            return CompanyMapper.toDTO(company);
        }
    }

    public void updateCompany(CompanyDTO companyDTO, Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company existingCompany = session.get(Company.class, companyId);
            if (existingCompany == null || existingCompany.isDeleted()) {
                throw new IllegalArgumentException("Cannot update company. The company either does not exist or is deleted.");
            }

            CompanyMapper.updateEntityFromDTO(companyDTO, existingCompany);
            session.update(existingCompany);
            transaction.commit();
        }
    }

    public void deleteCompany(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.get(Company.class, id);
            if (company == null || company.isDeleted()) {
                throw new IllegalArgumentException("Company with ID " + id + " does not exist or is already deleted.");
            }

            company.setDeleted(true);
            session.update(company);
            transaction.commit();
        }
    }

    public List<CompanyDTO> getAllCompanies() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Company> companies = session.createQuery("from Company c where c.isDeleted = false", Company.class).list();
            return companies.stream()
                    .map(CompanyMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<CompanyDTO> getCompaniesSortedByRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Company> cq = cb.createQuery(Company.class);
            Root<Company> root = cq.from(Company.class);
            cq.select(root)
                    .where(cb.equal(root.get("isDeleted"), false))
                    .orderBy(cb.desc(root.get("revenue")));
            List<Company> companies = session.createQuery(cq).getResultList();
            return companies.stream()
                    .map(CompanyMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<CompanyDTO> getCompaniesSortedByName() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Company> cq = cb.createQuery(Company.class);
            Root<Company> root = cq.from(Company.class);
            cq.select(root)
                    .where(cb.equal(root.get("isDeleted"), false))
                    .orderBy(cb.asc(root.get("name")));

            List<Company> companies = session.createQuery(cq).getResultList();
            return companies.stream()
                    .map(CompanyMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public Set<EmployeeDTO> getCompanyEmployees(Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Company company = session.get(Company.class, companyId);
            if (company == null || company.isDeleted()) {
                throw new IllegalArgumentException("Company with ID " + companyId + " does not exist or is deleted.");
            }

            // Използваме JOIN FETCH, за да заредим и квалификациите на служителите
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> root = cq.from(Employee.class);

            // Свързваме с qualifications на Employee и ги зареждаме с JOIN FETCH
            root.fetch("qualification", JoinType.LEFT);  // Зареждаме qualifications заедно със служителите

            cq.select(root).where(cb.equal(root.get("company"), company));
            List<Employee> employeeList = session.createQuery(cq).getResultList();

            return employeeList.stream()
                    .map(EmployeeMapper::toDTO)
                    .collect(Collectors.toSet());
        }
    }

    public Set<ClientDTO> getCompanyClients(Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Company company = session.get(Company.class, companyId);
            if (company == null || company.isDeleted()) {
                throw new IllegalArgumentException("Company with ID " + companyId + " does not exist or is deleted.");
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Client> cq = cb.createQuery(Client.class);

            Root<Client> root = cq.from(Client.class);
            Join<Client, Company> companyJoin = root.join("company");
            cq.select(root).where(cb.equal(companyJoin.get("companyId"), companyId));

            List<Client> clients = session.createQuery(cq).getResultList();
            return clients.stream()
                    .map(ClientMapper::toDTO)
                    .collect(Collectors.toSet());
        }
    }

    public Set<VehicleDTO> getCompanyVehicles(Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Company company = session.get(Company.class, companyId);
            if (company == null || company.isDeleted()) {
                throw new IllegalArgumentException("Company with ID " + companyId + " does not exist or is deleted.");
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Vehicle> cq = cb.createQuery(Vehicle.class);

            Root<Vehicle> root = cq.from(Vehicle.class);
            Join<Vehicle, Company> companyJoin = root.join("company");
            cq.select(root).where(cb.equal(companyJoin.get("companyId"), companyId));

            List<Vehicle> vehicles = session.createQuery(cq).getResultList();
            return vehicles.stream()
                    .map(VehicleMapper::toDTO)
                    .collect(Collectors.toSet());
        }
    }

    public long getTotalTransportsByCompany(Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(t) from Transport t where t.isDeleted = false and t.company.id = :companyId";

            Long totalTransports = session.createQuery(hql, Long.class)
                    .setParameter("companyId", companyId)
                    .getSingleResult();

            return totalTransports != null ? totalTransports : 0L;
        }
    }

    public double getTotalRevenueByCompany(Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select sum(t.price) from Transport t where t.isDeleted = false and t.isPaid = true and t.company.id = :companyId";

            Double totalRevenue = session.createQuery(hql, Double.class)
                    .setParameter("companyId", companyId)
                    .getSingleResult();

            return totalRevenue != null ? totalRevenue : 0.0;
        }
    }

    public double getTotalRevenueByCompanyInRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> cq = cb.createQuery(Double.class);
            Root<Transport> root = cq.from(Transport.class);

            // Създаване на условията за филтриране
            Predicate isNotDeleted = cb.isFalse(root.get("isDeleted"));
            Predicate isPaid = cb.isTrue(root.get("isPaid"));
            Predicate belongsToCompany = cb.equal(root.get("company").get("companyId"), companyId);
            Predicate inDateRange = cb.and(
                    cb.greaterThanOrEqualTo(root.get("departureDate"), startDate),
                    cb.lessThanOrEqualTo(root.get("arrivalDate"), endDate)
            );

            // Обединяване на условията
            cq.select(cb.sum(root.get("price")))
                    .where(cb.and(isNotDeleted, isPaid, belongsToCompany, inDateRange));

            // Изпълнение на заявката
            Double totalRevenue = session.createQuery(cq).getSingleResult();

            return (totalRevenue != null) ? totalRevenue : 0.0;
        }
    }

    public long getTotalTransportsByCompanyInRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Transport> root = cq.from(Transport.class);

            // Създаване на условията за филтриране
            Predicate isNotDeleted = cb.isFalse(root.get("isDeleted"));
            Predicate belongsToCompany = cb.equal(root.get("company").get("companyId"), companyId);
            Predicate inDateRange = cb.and(
                    cb.greaterThanOrEqualTo(root.get("departureDate"), startDate),
                    cb.lessThanOrEqualTo(root.get("arrivalDate"), endDate)
            );

            // Обединяване на условията
            cq.select(cb.count(root))
                    .where(cb.and(isNotDeleted, belongsToCompany, inDateRange));

            // Изпълнение на заявката
            Long totalTransports = session.createQuery(cq).getSingleResult();

            return (totalTransports != null) ? totalTransports : 0L;
        }
    }

   /*
   public double getTotalProfitByCompany(Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // 1) Взимаме самата компания от базата, за да получим initialInvestment
            Company company = session.get(Company.class, companyId);
            if (company == null || company.isDeleted()) {
                throw new IllegalArgumentException("Company with ID " + companyId + " does not exist or is deleted.");
            }

            // Приемаме, че company.getRevenue() съдържа "първоначалната инвестиция".
            double initialInvestment = (company.getRevenue() != null)
                    ? company.getRevenue().doubleValue()
                    : 0.0;

            // 2) Взимаме сумата от цените на всички превози (Transport.price)
            String hqlTransport = """
            select sum(t.price)
            from Transport t
            where t.isDeleted = false
              and t.isPaid = true
              and t.company.companyId = :companyId
        """;

            Double totalRevenue = session.createQuery(hqlTransport, Double.class)
                    .setParameter("companyId", companyId)
                    .getSingleResult();

            if (totalRevenue == null) {
                totalRevenue = 0.0;
            }

            // 3) Взимаме сумата от заплатите (Employee.salary)
            String hqlEmployees = """
            select sum(e.salary)
            from Employee e
            where e.isDeleted = false
              and e.company.companyId = :companyId
        """;
            Double totalSalaries = session.createQuery(hqlEmployees, Double.class)
                    .setParameter("companyId", companyId)
                    .getSingleResult();

            if (totalSalaries == null) {
                totalSalaries = 0.0;
            }

            double totalProfit = initialInvestment + totalRevenue - totalSalaries;

            return totalProfit;
        }
    }

    public Map<String, Object> getCompanyReport(Long companyId) {
        Map<String, Object> report = new HashMap<>();

        // Общи приходи
        double totalRevenue = getTotalRevenueByCompany(companyId);
        report.put("totalRevenue", totalRevenue);

        // Брой превози
        long totalTransports = getTotalTransportsByCompany(companyId);
        report.put("totalTransports", totalTransports);

        // Превозни средства на компанията
        Set<VehicleDTO> vehicles = getCompanyVehicles(companyId);
        report.put("vehicles", vehicles);

        // Клиенти на компанията
        Set<ClientDTO> clients = getCompanyClients(companyId);
        report.put("clients", clients);

        // Служители на компанията
        Set<EmployeeDTO> employees = getCompanyEmployees(companyId);
        report.put("employees", employees);

        return report;
    }
    */

    public void saveCompanyReportToFile(Long companyId, String directoryPath, String startDateStr, String endDateStr) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            throw new IllegalArgumentException("The path: " + directoryPath + " does not exist.");
        }

        // 1) Парсираме входните дати
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } finally {

        }

        // 2) Генерираме име на файл
        String fileName = "report_" + companyId + ".txt";
        String filePath = directoryPath + "/" + fileName;

        // 3) Взимаме CompanyDTO, за да покажем име на компанията
        CompanyDTO companyDTO = getCompanyById(companyId);

        // 4) Взимаме обектите (Vehicles, Clients, Employees)
        Set<VehicleDTO> vehicles = getCompanyVehicles(companyId);
        Set<ClientDTO> clients   = getCompanyClients(companyId);
        Set<EmployeeDTO> employees = getCompanyEmployees(companyId);

        // 5) Изчисляваме приходи/броя на превози, но САМО за зададения период:
        double totalRevenue = getTotalRevenueByCompanyInRange(companyId, startDate, endDate);
        long totalTransports = getTotalTransportsByCompanyInRange(companyId, startDate, endDate);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("--------- Report ---------");
            writer.newLine();

            // Заглавие + период
            writer.write("Company: " + companyDTO.getName()
                    + " (id: " + companyId + ")");
            writer.newLine();
            writer.write("Report for the period: " + startDateStr + " - " + endDateStr);
            writer.newLine();
            writer.newLine();

            writer.write("Total Revenue (in period): " + totalRevenue);
            writer.newLine();
            writer.write("Total Transports (in period): " + totalTransports);
            writer.newLine();
            writer.newLine();

            // Vehicles
            writer.write("Vehicles:");
            writer.newLine();
            for (VehicleDTO v : vehicles) {
                writer.write("  - " + v.getType()
                        + ", '" + v.getRegistrationNumber() + "'");
                writer.newLine();
            }
            writer.newLine();

            // Clients
            writer.write("Clients:");
            writer.newLine();
            for (ClientDTO c : clients) {
                writer.write("  - " + c.getName()
                        + ", '" + c.getContactInfo() + "'");
                writer.newLine();
            }
            writer.newLine();

            // Employees
            writer.write("Employees:");
            writer.newLine();
            for (EmployeeDTO e : employees) {
                writer.write("  - " + e.getName()
                        + ", salary:" + e.getSalary());
                writer.newLine();
            }

            writer.write("-----------------------------");
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException("Error processing file", e);
        }
    }

}