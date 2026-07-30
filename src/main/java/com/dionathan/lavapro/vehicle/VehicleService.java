package com.dionathan.lavapro.vehicle;

import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.Customer;
import com.dionathan.lavapro.customer.CustomerRepository;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.user.User;
import com.dionathan.lavapro.vehicle.dto.VehicleDetailsResponseDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleRequestDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleResponseDTO;
import com.dionathan.lavapro.vehicle.dto.VehicleUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final AuthenticatedUserService authenticatedUserService;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponseDTO create(VehicleRequestDTO requestDTO) {

        Company company = getCurrentCompany();

        Customer customer = customerRepository.findByIdAndCompany(requestDTO.customerId(), company)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if(vehicleRepository.existsByPlateAndCompany(requestDTO.plate(), company)) {
            throw new BusinessException("Veículo/Placa já cadastrada");
        }

        Vehicle vehicle = vehicleMapper.toEntity(requestDTO, customer, company);
        Vehicle saved = vehicleRepository.save(vehicle);

        return vehicleMapper.fromEntity(saved);
    }

    public VehicleDetailsResponseDTO findById(Long id) {

        Company company = getCurrentCompany();

        Vehicle vehicle = vehicleRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        return vehicleMapper.fromEntityDetails(vehicle);
    }

    @Transactional
    public VehicleResponseDTO update(Long id, VehicleUpdateRequestDTO requestDTO) {

        Company company = getCurrentCompany();

        Vehicle vehicle = vehicleRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));


        if(requestDTO.plate() != null && !requestDTO.plate().isBlank()) {
            String newPlate = requestDTO.plate().trim();
            if(!newPlate.equalsIgnoreCase(vehicle.getPlate())){
                if(vehicleRepository.existsByPlateAndCompanyAndIdNot(newPlate, company, vehicle.getId())) {
                    throw new BusinessException("Veículo/Placa já cadastrada");
                }
            }
        }

        if(requestDTO.customerId() != null) {
            Customer customer = customerRepository.findByIdAndCompany(requestDTO.customerId(), company)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Cliente não encontrado"));
            vehicle.setCustomer(customer);
        }

        vehicle = vehicleMapper.updateToEntity(requestDTO, vehicle);

        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {

        Company company = getCurrentCompany();

        Vehicle vehicle = vehicleRepository.findByIdAndCompanyAndDeletedAtIsNull(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        //Futuramente quando a OS estiver implementada, validar se existe OS aberta antes de excluir o veículo

        vehicle.setDeletedAt(LocalDateTime.now());
        vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public Page<VehicleResponseDTO> findAll(
            String plate,
            String customer,
            String model,
            String brand,
            Pageable pageable
    ) {

        Company company = getCurrentCompany();

        Page<Vehicle> vehicles = vehicleRepository.findAllByCompanyAndFilters(company, plate, customer, model, brand, pageable);

        return vehicles.map(vehicleMapper::fromEntity);
    }

    @Transactional(readOnly = true)
    public VehicleResponseDTO findByPlate(String plate) {

        Company company = getCurrentCompany();

        Vehicle vehicle = vehicleRepository.findByPlateAndCompanyAndDeletedAtIsNull(plate, company)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        return vehicleMapper.fromEntity(vehicle);
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }


}
