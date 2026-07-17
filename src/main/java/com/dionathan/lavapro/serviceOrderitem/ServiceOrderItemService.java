package com.dionathan.lavapro.serviceOrderitem;

import com.dionathan.lavapro.ServiceCatalog.ServiceCatalog;
import com.dionathan.lavapro.ServiceCatalog.ServiceCatalogRepository;
import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.PaymentRepository;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import com.dionathan.lavapro.serviceOrder.ServiceOrderRepository;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemRequestDTO;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemResponseDTO;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOrderItemService {


    private final AuthenticatedUserService authenticatedUserService;
    private final ServiceOrderItemMapper serviceOrderItemMapper;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;
    private final ServiceOrderItemRepository serviceOrderItemRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ServiceOrderItemResponseDTO create(Long serviceOrderId, ServiceOrderItemRequestDTO requestDTO) {

        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompany(serviceOrderId, company)
                .orElseThrow(() -> new ResourceNotFoundException("OS não encontrada"));

        ServiceCatalog serviceCatalog = serviceCatalogRepository.findByIdAndCompany(requestDTO.serviceCatalogId(), company)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço ou produto não encontrado"));

        validateServiceOrderNotPaid(serviceOrder);
        serviceCatalog.validateIsActive();
        serviceOrder.validateCanModify();

        ServiceOrderItem serviceOrderItem = serviceOrderItemMapper.toEntity(company, requestDTO, serviceOrder, serviceCatalog);

        serviceOrderItem.changeQuantity(requestDTO.quantity());

        ServiceOrderItem saved = serviceOrderItemRepository.save(serviceOrderItem);

        serviceOrderRepository.save(serviceOrder);

        serviceOrder.recalculateTotal();

        return serviceOrderItemMapper.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<ServiceOrderItemResponseDTO> findAll(Long serviceOrderId) {
        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(serviceOrderId, company)
                .orElseThrow(() -> new ResourceNotFoundException("OS não encontrada."));

        List<ServiceOrderItem> serviceOrderItems = serviceOrderItemRepository.findAllByServiceOrderAndCompany(serviceOrder, company);

        return serviceOrderItemMapper.fromEntity(serviceOrderItems);
    }

    @Transactional
    public ServiceOrderItemResponseDTO update(Long itemId, @Valid ServiceOrderItemUpdateRequestDTO requestDTO) {
        Company company = getCurrentCompany();

        ServiceOrderItem serviceOrderItem = serviceOrderItemRepository.findByIdAndCompany(itemId, company)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado."));

        ServiceOrder serviceOrder = serviceOrderItem.getServiceOrder();

        validateServiceOrderNotPaid(serviceOrder);
        serviceOrder.validateCanModify();
        serviceOrderItem.changeQuantity(requestDTO.quantity());
        serviceOrder.recalculateTotal();

        return serviceOrderItemMapper.fromEntity(serviceOrderItem);

    }
    @Transactional
    public void delete(Long itemId) {
        Company company = getCurrentCompany();

        ServiceOrderItem serviceOrderItem = serviceOrderItemRepository.findByIdAndCompany(itemId, company)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado."));

        ServiceOrder serviceOrder = serviceOrderItem.getServiceOrder();

        validateServiceOrderNotPaid(serviceOrder);

        serviceOrder.validateCanModify();
        serviceOrder.removeItem(serviceOrderItem);

        serviceOrderItemRepository.delete(serviceOrderItem);
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }

    public void validateServiceOrderNotPaid(ServiceOrder serviceOrder) {
        if(paymentRepository.existsByServiceOrderAndPaymentStatus(serviceOrder, PaymentStatus.PAID)) {
            throw new BusinessException("Não é possível alterar itens de uma Ordem de Serviço já paga.");
        }
    }


}

