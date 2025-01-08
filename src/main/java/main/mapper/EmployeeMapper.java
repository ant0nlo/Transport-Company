package main.mapper;

import main.dto.EmployeeDTO;
import main.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getName(),
                employee.getQualification(),
                employee.getSalary()
        );
    }

    public static Employee toEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setQualification(dto.getQualification());
        employee.setSalary(dto.getSalary());
        return employee;
    }

    public static void updateEntityFromDTO(EmployeeDTO dto, Employee existingEmployee) {
        existingEmployee.setName(dto.getName());
        existingEmployee.setQualification(dto.getQualification());
        existingEmployee.setSalary(dto.getSalary());
    }
}