package com.dionathan.lavapro.serviceOrderitem;

import com.dionathan.lavapro.ServiceCatalog.ServiceCatalog;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemRequestDTO;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemResponseDTO;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemUpdateRequestDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ServiceOrderItemMapper {

    public ServiceOrderItem toEntity(Company company, ServiceOrderItemRequestDTO requestDTO, ServiceOrder serviceOrder, ServiceCatalog serviceCatalog) {
        ServiceOrderItem serviceOrderItem = new ServiceOrderItem();

        serviceOrderItem.setServiceOrder(serviceOrder);
        serviceOrderItem.setServiceCatalog(serviceCatalog);
        serviceOrderItem.setCompany(company);
        serviceOrderItem.setQuantity(requestDTO.quantity());
        serviceOrderItem.setUnitPrice(serviceCatalog.getPrice());
        serviceOrderItem.setServiceName(serviceCatalog.getName());

        return serviceOrderItem;

    }

    public ServiceOrderItemResponseDTO fromEntity(ServiceOrderItem serviceOrderItem) {
        return new ServiceOrderItemResponseDTO(
                serviceOrderItem.getId(),
                serviceOrderItem.getServiceOrder().getId(),
                serviceOrderItem.getServiceCatalog().getId(),
                serviceOrderItem.getQuantity(),
                serviceOrderItem.getUnitPrice(),
                serviceOrderItem.getTotalPrice(),
                serviceOrderItem.getServiceName(),
                serviceOrderItem.getCreatedAt(),
                serviceOrderItem.getUpdatedAt()
        );
    }

    public List<ServiceOrderItemResponseDTO> fromEntity(List<ServiceOrderItem> serviceOrderItems) {
        return serviceOrderItems.stream().map(this::fromEntity).toList();
    }

    public ServiceOrderItem updateToEntity(ServiceOrderItemUpdateRequestDTO requestDTO, ServiceOrderItem serviceOrderItem) {
        serviceOrderItem.setQuantity(requestDTO.quantity());
        return serviceOrderItem;
    }
}
