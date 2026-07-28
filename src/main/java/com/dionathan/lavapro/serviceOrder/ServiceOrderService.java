package com.dionathan.lavapro.serviceOrder;


import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderRequestDTO;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderResponseDTO;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderUpdateRequestDTO;
import com.dionathan.lavapro.vehicle.Vehicle;
import com.dionathan.lavapro.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final AuthenticatedUserService authenticatedUserService;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final VehicleRepository vehicleRepository;

    @Transactional
    public ServiceOrderResponseDTO create(ServiceOrderRequestDTO requestDTO) {

        Company company = getCurrentCompany();

        Vehicle vehicle = vehicleRepository.findByIdAndCompanyAndDeletedAtIsNull(requestDTO.vehicleId(), company)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        ServiceOrder serviceOrder = serviceOrderMapper.toEntity(requestDTO, company, vehicle);
        ServiceOrder saved = serviceOrderRepository.save(serviceOrder);

        return serviceOrderMapper.fromEntity(saved);

    }

    @Transactional
    public ServiceOrderResponseDTO update(Long id, ServiceOrderUpdateRequestDTO requestDTO) {

        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));

        if(serviceOrder.getStatus() == ServiceOrderStatus.CANCELLED || serviceOrder.getStatus() == ServiceOrderStatus.DELIVERED) {
            throw new BusinessException("Ordem de serviço finalizada ou cancelada, não é possível alterar");
        }

        serviceOrder = serviceOrderMapper.updateToEntity(requestDTO, serviceOrder);

        ServiceOrder saved = serviceOrderRepository.save(serviceOrder);

        return serviceOrderMapper.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponseDTO findById(Long id) {

       Company company = getCurrentCompany();

       ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));

       return serviceOrderMapper.fromEntity(serviceOrder);

    }

    public void delete(Long id) {

        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));

        serviceOrder.setDeletedAt(LocalDateTime.now());
        serviceOrderRepository.save(serviceOrder);
    }


    @Transactional(readOnly = true)
    public Page<ServiceOrderResponseDTO> findAll(
            ServiceOrderStatus status,
            String customer,
            String plate,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        Page<ServiceOrder> serviceOrders = serviceOrderRepository.findAllByCompanyAndFilters(company, status, customer, plate, startDateTime, endDateTime, pageable);

        return serviceOrders.map(serviceOrderMapper::fromEntity);
    }

    @Transactional
    public void start(Long id) {

        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));

        serviceOrder.start();
    }

    @Transactional
    public void finish(Long id) {

        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));

        serviceOrder.finish();
    }

    @Transactional
    public void deliver(Long id) {

        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));

        serviceOrder.deliver();
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }
}
