package com.dionathan.lavapro.company;

import com.dionathan.lavapro.auth.dto.AuthSignUpRequestDTO;
import com.dionathan.lavapro.company.enums.CompanyStatus;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toEntity(AuthSignUpRequestDTO requestDTO, CompanyStatus status) {

        Company company = new Company();

        company.setCompanyName(requestDTO.companyName());
        company.setCompanyEmail(requestDTO.companyEmail().trim().toLowerCase());
        company.setStatus(status);

        return company;

    }
}
