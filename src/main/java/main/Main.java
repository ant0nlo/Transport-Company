package main;

import main.dao.*;
import main.dto.*;
import main.entity.*;
import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        try {
/*
            CompanyDTO companyDTO = new CompanyDTO("EHL", "Sofia, Bulgaria", BigDecimal.valueOf(10000));
            CompanyDAO companyDAO = new CompanyDAO();
            Long companyId = companyDAO.createCompany(companyDTO);
            System.out.println("\n=== Created Company ===");
            System.out.println("Company Name: " + companyDTO.getName());
            System.out.println("Company Address: " + companyDTO.getAddress());
            System.out.println("Company Revenue: " + companyDTO.getRevenue());
            System.out.println("Company ID: " + companyId);
            System.out.println("=========================\n");

            ClientDTO clientDTO = new ClientDTO("Stefan", "Stefan@example.com");
            ClientDAO clientDAO = new ClientDAO();
            Long clientId = clientDAO.createClient(clientDTO, companyId);
            System.out.println("\n=== Created Client ===");
            System.out.println("Client Name: " + clientDTO.getName());
            System.out.println("Client Contact Info: " + clientDTO.getContactInfo());
            System.out.println("Client ID: " + clientId);
            System.out.println("=========================\n");

            EmployeeDTO employeeDTO = new EmployeeDTO("G. Petrov", Set.of(Qualification.PASSENGERS_12_PLUS), 2000);
            EmployeeDAO employeeDAO = new EmployeeDAO();
            Long employeeId = employeeDAO.createEmployee(employeeDTO, companyId);
            System.out.println("\n=== Created Employee ===");
            System.out.println("Employee Name: " + employeeDTO.getName());
            System.out.println("Employee Qualifications: " + employeeDTO.getQualification());
            System.out.println("Employee Salary: " + employeeDTO.getSalary());
            System.out.println("Employee ID: " + employeeId);
            System.out.println("=========================\n");

            VehicleDTO vehicleDTO = new VehicleDTO(VehicleType.VAN, 4, "SF4893AB", null);
            VehicleDAO vehicleDAO = new VehicleDAO();
            Long vehicleId = vehicleDAO.createVehicle(vehicleDTO, companyId);
            System.out.println("\n=== Created Vehicle ===");
            System.out.println("Vehicle Type: " + vehicleDTO.getType());
            System.out.println("Vehicle Registration Number: " + vehicleDTO.getRegistrationNumber());
            System.out.println("Vehicle Capacity: " + vehicleDTO.getCapacity());
            System.out.println("Vehicle Qualification: " + vehicleDTO.getRequiredQualification());
            System.out.println("Vehicle ID: " + vehicleId);
            System.out.println("=========================\n");

            TransportDTO transportDTO = new TransportDTO(
                    "Sofia", "Varna", LocalDate.of(2024, 12, 24),
                    LocalDate.of(2024, 12, 25), "Table",
                    20, 20000, true
            );

            TransportDAO transportDAO = new TransportDAO();
            transportDAO.createTransport(transportDTO, companyId, clientId, employeeId, vehicleId);

            System.out.println("\n=== Created Transport ===");
            System.out.println("Start Location: " + transportDTO.getStartLocation());
            System.out.println("End Location: " + transportDTO.getEndLocation());
            System.out.println("Departure Date: " + transportDTO.getDepartureDate());
            System.out.println("Arrival Date: " + transportDTO.getArrivalDate());
            System.out.println("Cargo Description: " + transportDTO.getCargoDescription());
            System.out.println("Cargo Weight: " + transportDTO.getCargoWeight());
            System.out.println("Price: " + transportDTO.getPrice());
            System.out.println("Paid: " + transportDTO.isPaid());
            System.out.println("=========================\n");

            System.out.println("getTotalTransportsByCompanyInRange\n: "
                    + companyDAO.getTotalTransportsByCompanyInRange(companyId,
                    LocalDate.of(2025, 01, 01),
                    LocalDate.of(2025, 01, 31)));

            System.out.println("getTotalRevenueByCompanyInRange\n: "
                    + companyDAO.getTotalRevenueByCompanyInRange(companyId,
                    LocalDate.of(2025, 01, 01),
                    LocalDate.of(2025, 01, 31)));

            System.out.println("\n=== Transport ===");
            System.out.println("Total Revenue: " + transportDAO.getTotalRevenue());
            System.out.println("Total Revenue By Driver: " + transportDAO.getTotalRevenueByDriver(employeeId));
            System.out.println("=========================\n");


 */
            CompanyDAO companyDAO = new CompanyDAO();

            System.out.println("Total profit for the company: " + companyDAO.getTotalProfitByCompany(383L));
            Map<String, Object> companyReport = companyDAO.getCompanyReport(383L);

            System.out.println("Total Revenue: " + companyReport.get("totalRevenue"));
            System.out.println("Total Transports: " + companyReport.get("totalTransports"));
            System.out.println("Vehicles: " + companyReport.get("vehicles"));
            System.out.println("Clients: " + companyReport.get("clients"));
            System.out.println("Employees: " + companyReport.get("employees"));

            companyDAO.saveCompanyReportToFile(388L, "src/main/java/main/reports/", "2025-01-01", "2025-01-31");



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}