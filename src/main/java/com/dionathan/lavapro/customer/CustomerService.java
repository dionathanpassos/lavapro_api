package com.dionathan.lavapro.customer;

import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.dto.CustomerDetailsResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerRequestDTO;
import com.dionathan.lavapro.customer.dto.CustomerResponseDTO;
import com.dionathan.lavapro.customer.dto.CustomerUpdateRequestDTO;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public CustomerResponseDTO create(CustomerRequestDTO requestDTO) {

        Company company = getCurrentCompany();

        if(customerRepository.existsByPhoneAndCompany(requestDTO.phone(), company)) {
            throw new BusinessException("Número de telefone em utilização");
        }

        Customer customer = customerMapper.toEntity(requestDTO, company);
        Customer saved = customerRepository.save(customer);

        return customerMapper.fromEntity(saved);
    }

    public CustomerDetailsResponseDTO findById(Long id) {

        Company company = getCurrentCompany();

        Customer customer = customerRepository.findByIdAndCompanyWithVehicles(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return customerMapper.fromEntityDetails(customer);
    }

    @Transactional
    public CustomerResponseDTO update(Long id, CustomerUpdateRequestDTO requestDTO) {

        Company company = getCurrentCompany();

        Customer customer = customerRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        customer = customerMapper.updateToEntity(requestDTO, customer);
        Customer saved = customerRepository.save(customer);

        return customerMapper.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {

        Company company = getCurrentCompany();

        Customer customer = customerRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if(customer.getDeletedAt() != null) {
            throw new BusinessException("Cliente já desativado");
        }

        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> findAll(
            String name,
            String phone,
            String plate,
            String search,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        Page<Customer> customers = customerRepository.findAllByCompanyAndFilters(company, name, phone, plate, search, pageable);

        return customers.map(customerMapper::fromEntity);
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }
}
