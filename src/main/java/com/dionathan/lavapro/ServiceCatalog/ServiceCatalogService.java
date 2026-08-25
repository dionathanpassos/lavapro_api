package com.dionathan.lavapro.ServiceCatalog;

import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogIndicatorsDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogRequestDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogResponseDTO;
import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogUpdateRequestDTO;
import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceCatalogService {

    private final AuthenticatedUserService authenticatedUserService;
    private final ServiceCatalogRepository catalogRepository;
    private final ServiceCatalogMapper serviceCatalogMapper;

    @Transactional
    public ServiceCatalogResponseDTO create(ServiceCatalogRequestDTO requestDTO) {
        Company company = getCurrentCompany();

        if(catalogRepository.existsByCompanyAndNameIgnoreCase(company, requestDTO.name())){
            throw new BusinessException("Já existe um serviço com esse nome.");
        }

        ServiceCatalog serviceCatalog = serviceCatalogMapper.toEntity(requestDTO, company);
        ServiceCatalog saved = catalogRepository.save(serviceCatalog);

        return serviceCatalogMapper.fromEntity(saved);
    }

    @Transactional
    public ServiceCatalogResponseDTO update(Long id, ServiceCatalogUpdateRequestDTO requestDTO) {
        Company company = getCurrentCompany();

        ServiceCatalog serviceCatalog = catalogRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if(requestDTO.name() != null) {
            if(catalogRepository.existsByCompanyAndNameIgnoreCaseAndIdNot(company, requestDTO.name(), serviceCatalog.getId())){
                throw new BusinessException("Já existe um serviço com esse nome.");
            }
        }

        serviceCatalog = serviceCatalogMapper.updateToEntity(requestDTO, serviceCatalog);
        ServiceCatalog saved = catalogRepository.save(serviceCatalog);

        return serviceCatalogMapper.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ServiceCatalogResponseDTO findById(Long id) {
        Company company = getCurrentCompany();

        ServiceCatalog serviceCatalog = catalogRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return serviceCatalogMapper.fromEntity(serviceCatalog);

    }

    @Transactional(readOnly = true)
    public ServiceCatalogIndicatorsDTO getIndicators() {
        Company company = getCurrentCompany();

        Long totalProducts = catalogRepository.countByCompany(company);
        Long totalActive = catalogRepository.countByCompanyAndActiveIsTrue(company);

        return new ServiceCatalogIndicatorsDTO(
                totalProducts,
                totalActive
        );
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }


    public Page<ServiceCatalogResponseDTO> findAll(
            String search,
            ServiceCatalogType type,
            Boolean active,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        Page<ServiceCatalog> products = catalogRepository.findAllByCompanyAndFilters(company, search, type, active, pageable);

        return products.map(serviceCatalogMapper::fromEntity);
    }

    public void deactivate(Long id) {
        Company company = getCurrentCompany();

        ServiceCatalog serviceCatalog = catalogRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if(!serviceCatalog.isActive()) {
            throw new BusinessException("Produto já desativado");
        }

        serviceCatalog.setActive(false);
        catalogRepository.save(serviceCatalog);
    }

    public void activate(Long id) {
        Company company = getCurrentCompany();

        ServiceCatalog serviceCatalog = catalogRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if(serviceCatalog.isActive()) {
            throw new BusinessException("Produto já ativo");
        }

        serviceCatalog.setActive(true);
        catalogRepository.save(serviceCatalog);
    }


}
