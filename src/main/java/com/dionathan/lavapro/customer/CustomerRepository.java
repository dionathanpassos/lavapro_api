package com.dionathan.lavapro.customer;

import com.dionathan.lavapro.company.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPhoneAndCompany(String phone, Company company);

   Optional<Customer> findByIdAndCompany(Long id, Company companyid);

    Page<Customer> findAllByCompany(Company company, Pageable pageable);

    Long countByCompany(Company company);
}
