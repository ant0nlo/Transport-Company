package main.mapper;

import main.dto.CompanyDTO;
import main.entity.Company;

public class CompanyMapper {
    public static CompanyDTO toDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setName(company.getName());
        dto.setAddress(company.getAddress());
        dto.setRevenue(company.getRevenue());
        return dto;
    }

    public static Company toEntity(CompanyDTO dto) {
        Company company = new Company();
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setRevenue(dto.getRevenue());
        return company;
    }

    public static void updateEntityFromDTO(CompanyDTO dto, Company existingCompany) {
        existingCompany.setName(dto.getName());
        existingCompany.setAddress(dto.getAddress());
        existingCompany.setRevenue(dto.getRevenue());
    }
}