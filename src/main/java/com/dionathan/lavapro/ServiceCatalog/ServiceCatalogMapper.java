package com.dionathan.lavapro.ServiceCatalog;

import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogRequestDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogResponseDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogUpdateRequestDTO;
import com.dionathan.lavapro.company.Company;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceCatalogMapper {

    public ServiceCatalog toEntity(ServiceCatalogRequestDTO requestDTO, Company company) {
        ServiceCatalog serviceCatalog = new ServiceCatalog();

        serviceCatalog.setName(requestDTO.name().trim());
        serviceCatalog.setPrice(requestDTO.price());
        serviceCatalog.setType(requestDTO.type());
        serviceCatalog.setDescription(requestDTO.description());
        serviceCatalog.setCompany(company);

        return serviceCatalog;
    }

    public ServiceCatalogResponseDTO fromEntity(ServiceCatalog serviceCatalog) {
        return new ServiceCatalogResponseDTO(
                serviceCatalog.getId(),
                serviceCatalog.getName(),
                serviceCatalog.getPrice(),
                serviceCatalog.getType(),
                serviceCatalog.isActive(),
                serviceCatalog.getDescription(),
                serviceCatalog.getCreatedAt(),
                serviceCatalog.getUpdatedAt(),
                serviceCatalog.getDeletedAt()
        );
    }

    public ServiceCatalog updateToEntity(ServiceCatalogUpdateRequestDTO requestDTO, ServiceCatalog serviceCatalog) {

        if(requestDTO.name() != null) {
            serviceCatalog.setName(requestDTO.name().trim());
        }
        if(requestDTO.price() != null) {
            serviceCatalog.setPrice(requestDTO.price());
        }
        if(requestDTO.type() != null) {
            serviceCatalog.setType(requestDTO.type());
        }
        if(requestDTO.description() != null) {
            serviceCatalog.setDescription(requestDTO.description());
        }

        return serviceCatalog;
    }

    public List<ServiceCatalogResponseDTO> fromEntity(List<ServiceCatalog> serviceCatalogs) {
        return serviceCatalogs.stream().map(this::fromEntity).toList();
    }
}
