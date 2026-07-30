package com.dionathan.lavapro.customer;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.dto.CustomerDetailsResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerRequestDTO;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerUpdateRequestDTO;
import com.dionathan.lavapro.vehicle.Vehicle;
import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO requestDTO, Company company) {
        Customer customer = new Customer();

        customer.setName(requestDTO.name());
        customer.setPhone(requestDTO.phone());
        customer.setCompany(company);

        return customer;

    }

    public CustomerResponseDTO fromEntity(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt()
        );
    }

    public CustomerDetailsResponseDTO fromEntityDetails(Customer customer) {

        return new CustomerDetailsResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt(),
                customer.getVehicles().stream().map(
                        vehicle -> new VehicleResponseDTO(
                        vehicle.getId(),
                        vehicle.getPlate(),
                        vehicle.getModel(),
                        vehicle.getBrand(),
                        vehicle.getColor(),
                        vehicle.getYear(),
                        vehicle.getCreatedAt(),
                        vehicle.getUpdatedAt(),
                        vehicle.getDeletedAt()
                )).toList()

        );
    }
    public List<CustomerResponseDTO> fromEntity(List<Customer> customers) {
        return customers.stream().map(this::fromEntity).toList();
    }

    public Customer updateToEntity(CustomerUpdateRequestDTO requestDTO, Customer customer) {

        if(requestDTO.name() != null) {
            customer.setName(requestDTO.name());
        }
        if(requestDTO.phone() != null) {
            customer.setPhone(requestDTO.phone());
        }


        return customer;

    }
}
