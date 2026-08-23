package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerSummaryResponseDTO;
import com.dionathan.lavapro.payment.Payment;
import com.dionathan.lavapro.payment.dto.PaymentResponseDTO;
import com.dionathan.lavapro.serviceOrder.dto.*;
import com.dionathan.lavapro.serviceOrderitem.dto.ServiceOrderItemResponseDTO;
import com.dionathan.lavapro.vehicle.Vehicle;
import com.dionathan.lavapro.vehicle.VehicleSummaryMapper;
import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ServiceOrderMapper {

    private final VehicleSummaryMapper vehicleSummaryMapper;

    public ServiceOrderMapper(VehicleSummaryMapper vehicleSummaryMapper) {
        this.vehicleSummaryMapper = vehicleSummaryMapper;
    }

    public ServiceOrder toEntity(ServiceOrderRequestDTO requestDTO, Company company, Vehicle vehicle) {
        ServiceOrder serviceOrder = new ServiceOrder();

        serviceOrder.setObservations(requestDTO.observations());
        serviceOrder.setCompany(company);
        serviceOrder.setVehicle(vehicle);
        serviceOrder.setStatus(ServiceOrderStatus.WAITING);

        return serviceOrder;
    }

    public ServiceOrderResponseDTO fromEntity(ServiceOrder serviceOrder) {

        Vehicle vehicle = serviceOrder.getVehicle();

        List<ServiceOrderItemResponseDTO> items = serviceOrder.getItems().stream()
                .map(item -> new ServiceOrderItemResponseDTO(
                        item.getId(),
                        item.getServiceOrder().getId(),
                        item.getServiceCatalog().getId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice(),
                        item.getServiceName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()
                )).toList();

        return new ServiceOrderResponseDTO(
                serviceOrder.getId(),
                serviceOrder.getTotalAmount(),
                serviceOrder.getObservations(),
                serviceOrder.getStatus(),
                vehicleSummaryMapper.fromEntity(vehicle),
                items,
                serviceOrder.getCreatedAt(),
                serviceOrder.getUpdatedAt(),
                serviceOrder.getDeletedAt()
        );
    }

    public ServiceOrderProResponseDTO fromEntityR(ServiceOrder serviceOrder, Boolean isPaid) {

        Vehicle vehicle = serviceOrder.getVehicle();

        List<ServiceOrderItemResponseDTO> items = serviceOrder.getItems().stream()
                .map(item -> new ServiceOrderItemResponseDTO(
                        item.getId(),
                        item.getServiceOrder().getId(),
                        item.getServiceCatalog().getId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice(),
                        item.getServiceName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()
                )).toList();

        return new ServiceOrderProResponseDTO(
                serviceOrder.getId(),
                serviceOrder.getTotalAmount(),
                serviceOrder.getObservations(),
                serviceOrder.getStatus(),
                vehicleSummaryMapper.fromEntity(vehicle),
                items,
                serviceOrder.getCreatedAt(),
                serviceOrder.getUpdatedAt(),
                serviceOrder.getDeletedAt(),
                isPaid != null && isPaid
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


    public ServiceOrderDetailsResponseDTO fromEntityDetails(ServiceOrder serviceOrder, List<Payment> payments) {
        Vehicle vehicle = serviceOrder.getVehicle();

        List<ServiceOrderItemResponseDTO> items = serviceOrder.getItems().stream()
                .map(item -> new ServiceOrderItemResponseDTO(
                        item.getId(),
                        item.getServiceOrder().getId(),
                        item.getServiceCatalog().getId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice(),
                        item.getServiceName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()
                )).toList();

        List<PaymentResponseDTO> paymentsList = payments.stream()
                .map(payment -> new PaymentResponseDTO(
                        payment.getId(),
                        payment.getAmount(),
                        payment.getPaymentMethod(),
                        payment.getPaymentStatus(),
                        payment.getPaidAt(),
                        payment.getCreatedAt(),
                        payment.getUpdatedAt(),
                        payment.getCanceledAt(),
                        payment.getServiceOrder().getId(),
                        payment.getServiceOrder().getVehicle().getCustomer().getName(),
                        payment.getServiceOrder().getVehicle().getPlate(),
                        payment.getServiceOrder().getVehicle().getBrand(),
                        payment.getServiceOrder().getVehicle().getModel()

                )).toList();
        return new ServiceOrderDetailsResponseDTO(
                serviceOrder.getId(),
                serviceOrder.getTotalAmount(),
                serviceOrder.getObservations(),
                serviceOrder.getStatus(),
                vehicleSummaryMapper.fromEntity(vehicle),
                items,
                paymentsList,
                serviceOrder.getCreatedAt(),
                serviceOrder.getUpdatedAt(),
                serviceOrder.getDeletedAt()
        );
    }
}
