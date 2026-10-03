package com.dionathan.lavapro.vehicle;

import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.Customer;
import com.dionathan.lavapro.customer.CustomerRepository;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.user.User;
import com.dionathan.lavapro.vehicle.dto.VehicleRequestDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private User user;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void shouldCreateVehicleSuccessfully() {

        //Arrange
        Company company = new Company();

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(user.getCompany())
                .thenReturn(company);

        Customer customer = new Customer();
        customer.setId(1L);

        VehicleRequestDTO requestDTO = new VehicleRequestDTO(
                "ABC1D23",
                "HB20",
                "Hyundai",
                "Preto",
                2020,
                customer.getId()

        );

        Vehicle vehicle = new Vehicle();
        Vehicle savedVehicle = new Vehicle();

        VehicleResponseDTO responseDTO = new VehicleResponseDTO(
                savedVehicle.getId(),
                savedVehicle.getPlate(),
                savedVehicle.getModel(),
                savedVehicle.getBrand(),
                savedVehicle.getColor(),
                savedVehicle.getYear(),
                savedVehicle.getCreatedAt(),
                savedVehicle.getUpdatedAt(),
                savedVehicle.getDeletedAt()
        );

        when(customerRepository.findByIdAndCompany(
                requestDTO.customerId(),
                company
        )).thenReturn(Optional.of(customer));

        when(vehicleRepository.existsByPlateAndCompany(
                requestDTO.plate(),
                company
        )).thenReturn(false);

        when(vehicleMapper.toEntity(
                requestDTO,
                customer,
                company
        )).thenReturn(vehicle);

        when(vehicleRepository.save(vehicle))
                .thenReturn(savedVehicle);

        when(vehicleMapper.fromEntity(savedVehicle))
                .thenReturn(responseDTO);

        //ACT
        VehicleResponseDTO result =
                vehicleService.create(requestDTO);

        //ASSERT
        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    void shouldThrowExceptionWhenPlateAlreadyExists() {

        //ARRANGE
        Company company = new Company();

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(user.getCompany())
                .thenReturn(company);

        Customer customer = new Customer();
        customer.setId(1L);

        VehicleRequestDTO requestDTO = new VehicleRequestDTO(
                "ABC1D23",
                "HB20",
                "Hyundai",
                "Preto",
                2020,
                customer.getId()
        );

        when(customerRepository.findByIdAndCompany(
                requestDTO.customerId(),
                company
        )).thenReturn(Optional.of(customer));

        when(vehicleRepository.existsByPlateAndCompany(
                requestDTO.plate(),
                company
        )).thenReturn(true);

        //ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                        () -> vehicleService.create(requestDTO)
        );

        assertEquals(
                "Veículo/Placa já cadastrada",
                exception.getMessage()
        );

    }
  
}