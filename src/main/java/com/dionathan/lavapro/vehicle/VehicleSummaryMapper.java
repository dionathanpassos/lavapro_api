package com.dionathan.lavapro.vehicle;

import com.dionathan.lavapro.customer.Customer;
import com.dionathan.lavapro.customer.dto.CustomerSummaryResponseDTO;
import com.dionathan.lavapro.serviceOrder.dto.VehicleSummaryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class VehicleSummaryMapper {

    public VehicleSummaryResponseDTO fromEntity(Vehicle vehicle) {
        Customer customer = vehicle.getCustomer();

        return new VehicleSummaryResponseDTO(
                vehicle.getId(),
                vehicle.getPlate(),
                vehicle.getModel(),
                vehicle.getBrand(),
                new CustomerSummaryResponseDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getPhone()

                )
        );
    }
}
