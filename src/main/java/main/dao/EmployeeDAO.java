package main.dao;

import main.dto.EmployeeDTO;
import main.entity.*;
import main.configuration.HibernateUtil;
import main.mapper.EmployeeMapper;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDAO {

    public Long createEmployee(EmployeeDTO employeeDTO, Long companyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.get(Company.class, companyId);


            Employee employee = EmployeeMapper.toEntity(employeeDTO);
            employee.setCompany(company);
            Long employeeId = (Long) session.save(employee);
            transaction.commit();
            return employeeId;
        }
    }

    public EmployeeDTO getEmployee(Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee employee = session.get(Employee.class, employeeId);
            if (employee == null || employee.isDeleted()) {
                throw new IllegalArgumentException("Employee with ID " + employeeId + " does not exist.");
            }
            Hibernate.initialize(employee.getQualification());

            return EmployeeMapper.toDTO(employee);
        }
    }

    public List<EmployeeDTO> getAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Employee> employees = session.createQuery("FROM Employee WHERE isDeleted = false", Employee.class).list();
            return employees.stream()
                    .map(EmployeeMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public void updateEmployee(EmployeeDTO employeeDTO, Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Employee existingEmployee = session.get(Employee.class, employeeId);
            if (existingEmployee == null || existingEmployee.isDeleted()) {
                throw new IllegalArgumentException("Employee with ID " + employeeId + " does not exist.");
            }

            EmployeeMapper.updateEntityFromDTO(employeeDTO, existingEmployee);
            session.update(existingEmployee);
            transaction.commit();
        }
    }

    public void deleteEmployee(Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, employeeId);
            if (employee == null || employee.isDeleted()) {
                throw new IllegalArgumentException("Employee with ID " + employeeId + " does not exist or is already deleted.");
            }
            employee.setDeleted(true);
            session.update(employee);
            transaction.commit();
        }
    }

    public List<EmployeeDTO> getEmployeesSortedBySalary() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> root = cq.from(Employee.class);

           // cq.select(root).orderBy(cb.desc(root.get("salary")));
            cq.select(root).where(cb.equal(root.get("isDeleted"), false))  // Филтриране на не-изтрити служители
                    .orderBy(cb.desc(root.get("salary")));

            List<Employee> employees = session.createQuery(cq).getResultList();
            return employees.stream()
                    .map(EmployeeMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

}