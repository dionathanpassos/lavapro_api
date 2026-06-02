package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerSummaryResponseDTO;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderRequestDTO;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderResponseDTO;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderUpdateRequestDTO;
import com.dionathan.lavapro.serviceOrder.dto.VehicleSummaryResponseDTO;
import com.dionathan.lavapro.vehicle.Vehicle;
import com.dionathan.lavapro.vehicle.VehicleSummaryMapper;
import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceOrderMapper {

    private final VehicleSummaryMapper vehicleSummaryMapper;

    public ServiceOrderMapper(VehicleSummaryMapper vehicleSummaryMapper) {
        this.vehicleSummaryMapper = vehicleSummaryMapper;
    }

    public ServiceOrder toEntity(ServiceOrderRequestDTO requestDTO, Company company, Vehicle vehicle) {
        ServiceOrder serviceOrder = new ServiceOrder();

        serviceOrder.setTotalAmount(requestDTO.totalAmount());
        serviceOrder.setObservations(requestDTO.observations());
        serviceOrder.setCompany(company);
        serviceOrder.setVehicle(vehicle);
        serviceOrder.setStatus(ServiceOrderStatus.WAITING);

        return serviceOrder;
    }

    public ServiceOrderResponseDTO fromEntity(ServiceOrder serviceOrder) {

        Vehicle vehicle = serviceOrder.getVehicle();
        return new ServiceOrderResponseDTO(
                serviceOrder.getId(),
                serviceOrder.getTotalAmount(),
                serviceOrder.getObservations(),
                serviceOrder.getStatus(),
                vehicleSummaryMapper.fromEntity(vehicle),
                serviceOrder.getCreatedAt(),
                serviceOrder.getUpdatedAt(),
                serviceOrder.getDeletedAt()
        );
    }


    public List<ServiceOrderResponseDTO> fromEntity(List<ServiceOrder> serviceOrders) {
        return serviceOrders.stream().map(this::fromEntity).toList();
    }



    public ServiceOrder updateToEntity(ServiceOrderUpdateRequestDTO requestDTO, ServiceOrder serviceOrder) {

        if(requestDTO.totalAmount() != null) {
            serviceOrder.setTotalAmount(requestDTO.totalAmount());
        }
        if(requestDTO.observations() != null) {
            serviceOrder.setObservations(requestDTO.observations());
        }
        if(requestDTO.status() != null) {
            serviceOrder.setStatus(requestDTO.status());
        }

        return serviceOrder;
    }


}
