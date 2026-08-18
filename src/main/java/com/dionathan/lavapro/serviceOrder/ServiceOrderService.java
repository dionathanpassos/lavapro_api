package com.dionathan.lavapro.serviceOrder;


import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.dashboard.dto.ServiceOrderDashboardDTO;
import com.dionathan.lavapro.payment.Payment;
import com.dionathan.lavapro.payment.PaymentRepository;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.serviceOrder.dto.*;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final AuthenticatedUserService authenticatedUserService;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final VehicleRepository vehicleRepository;
    private final PaymentRepository paymentRepository;

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
    public ServiceOrderDetailsResponseDTO findById(Long id) {

       Company company = getCurrentCompany();

       ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));
        List<Payment> payments = paymentRepository.findAllByCompanyAndServiceOrder(company, serviceOrder);

       return serviceOrderMapper.fromEntityDetails(serviceOrder, payments);

    }

    public void delete(Long id) {

        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de serviço não encontrada"));

        serviceOrder.setDeletedAt(LocalDateTime.now());
        serviceOrderRepository.save(serviceOrder);
    }


    @Transactional(readOnly = true)
    public Page<ServiceOrderProResponseDTO> findAll(
            ServiceOrderStatus status,
            String customer,
            String plate,
            LocalDate startDate,
            LocalDate endDate,
            String search,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

//        Page<ServiceOrder> serviceOrders = serviceOrderRepository.findAllByCompanyAndFilters(company, status, customer, plate, startDateTime, endDateTime, pageable);
//
//        return serviceOrders.map(serviceOrderMapper::fromEntity);

        Page<ServiceOrderProjection> projections = serviceOrderRepository
                .findAllByCompanyAndFilters(company, status, customer, plate, startDateTime, endDateTime, search, PaymentStatus.PAID, pageable);

        // 2. O map converte a projeção no seu DTO existente
        return projections.map(projection -> serviceOrderMapper.fromEntityR(
                projection.getServiceOrder(),
                projection.getIsPaid()
        ));
    }

    @Transactional(readOnly = true)
    public ServiceOrderIndicators getIndicators() {
        Company company = getCurrentCompany();

        Long waiting = serviceOrderRepository.countByCompanyAndStatus(
                company, ServiceOrderStatus.WAITING);

        Long inProgress = serviceOrderRepository.countByCompanyAndStatus(
                company, ServiceOrderStatus.IN_PROGRESS);

        Long ready = serviceOrderRepository.countByCompanyAndStatus(
                company, ServiceOrderStatus.READY);

        Long delivered = serviceOrderRepository.countByCompanyAndStatus(
                company, ServiceOrderStatus.DELIVERED);

        Long canceled = serviceOrderRepository.countByCompanyAndStatus(
                company, ServiceOrderStatus.CANCELLED);

        return new ServiceOrderIndicators(
                waiting,
                inProgress,
                ready,
                delivered,
                canceled
        );

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
