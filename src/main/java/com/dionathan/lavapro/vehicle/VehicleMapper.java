package com.dionathan.lavapro.vehicle;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.Customer;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerSummaryResponseDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleRequestDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleUpdateRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequestDTO requestDTO, Customer customer, Company company) {
        Vehicle vehicle = new Vehicle();

        vehicle.setPlate(requestDTO.plate().trim().toUpperCase());
        vehicle.setBrand(requestDTO.brand());
        vehicle.setModel(requestDTO.model());
        vehicle.setColor(requestDTO.color());
        vehicle.setYear(requestDTO.year());
        vehicle.setCompany(company);
        vehicle.setCustomer(customer);

        return vehicle;
    }

    public VehicleResponseDTO fromEntity(Vehicle vehicle) {

        Customer customer = vehicle.getCustomer();

        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getPlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getYear(),
                new CustomerSummaryResponseDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getPhone()
                ),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt(),
                vehicle.getDeletedAt()
        );
    }

    public List<VehicleResponseDTO> fromEntity(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(this::fromEntity)
                .toList();
    }

    public Vehicle updateToEntity(VehicleUpdateRequestDTO requestDTO, Vehicle vehicle) {

        if(requestDTO.plate() != null) {
            vehicle.setPlate(requestDTO.plate().trim().toUpperCase());
        }
        if(requestDTO.brand() != null) {
            vehicle.setBrand(requestDTO.brand());
        }
        if(requestDTO.model() != null) {
            vehicle.setModel(requestDTO.model());
        }
        if(requestDTO.color() != null) {
            vehicle.setColor(requestDTO.color());
        }
        if(requestDTO.year() != null) {
            vehicle.setYear(requestDTO.year());
        }


        return vehicle;
    }
}
