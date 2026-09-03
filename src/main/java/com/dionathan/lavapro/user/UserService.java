package com.dionathan.lavapro.user;

import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.user.dto.*;
import com.dionathan.lavapro.user.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO create(UserRequestDTO requestDTO) {
        Company company = getCurrentCompany();

        User userAuthenticated = authenticatedUserService.getAuthenticatedUser();

        userAuthenticated.validateCanModifyUsers();

        if(userRepository.existsByEmail(requestDTO.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        String encodedPassword = passwordEncoder.encode(requestDTO.email());

        User user = userMapper.toEntityUser(requestDTO, company, encodedPassword);
        User saved = userRepository.save(user);

        return userMapper.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(
            String name,
            Boolean active,
            Pageable pageable) {
        Company company = getCurrentCompany();
        Page<User> users = userRepository.findAllByCompanyAndFilters(company, name, active, pageable);
        return users.map(userMapper::fromEntity);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        Company company = getCurrentCompany();

        User user = userRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return userMapper.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, @Valid UserUpdateRequestDTO requestDTO) {
        Company company = getCurrentCompany();

        authenticatedUserService.getAuthenticatedUser().validateCanModifyUsers();

        User user = userRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if(requestDTO.name() != null) {
            user.updateName(requestDTO.name());
        }
        if(requestDTO.email() != null) {
            if(userRepository.existsByEmailAndIdNot(requestDTO.email(), user.getId())) {
                throw new BusinessException("Email já cadastrado");
            }
            user.updateEmail(requestDTO.email());
        }
        if(requestDTO.phone() != null) {
            user.updatePhone(requestDTO.phone());
        }
        if(requestDTO.role() != null) {
            user.updateRole(requestDTO.role());
        }
        if(requestDTO.password() != null) {
            user.updatePassword(passwordEncoder.encode(requestDTO.password()));
        }

        return userMapper.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO deactivate(Long id) {
        Company company = getCurrentCompany();

        authenticatedUserService.getAuthenticatedUser().validateCanModifyUsers();

        User user = userRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.deactivate();

        return userMapper.fromEntity(user);

    }

    @Transactional
    public UserResponseDTO activate(Long id) {
        Company company = getCurrentCompany();

        authenticatedUserService.getAuthenticatedUser().validateCanModifyUsers();

        User user = userRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.activate();

        return userMapper.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO profile() {
        User user = authenticatedUserService.getAuthenticatedUser();

        return userMapper.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO updateProfile(UserUpdateProfileRequestDTO requestDTO) {
        User user = authenticatedUserService.getAuthenticatedUser();

        if(requestDTO.name() != null) {
            user.updateName(requestDTO.name());
        }
        if(requestDTO.email() != null) {
            if(userRepository.existsByEmailAndIdNot(requestDTO.email(), user.getId())) {
                throw new BusinessException("Email já cadastrado");
            }
            user.updateEmail(requestDTO.email());
        }
        if(requestDTO.password() != null) {
            user.updatePassword(passwordEncoder.encode(requestDTO.password()));
        }

        return userMapper.fromEntity(user);

    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }

    @Transactional(readOnly = true)
    public UserIndicatorsDTO getIndicators(String name, Boolean active) {
        Company company = getCurrentCompany();

        return userRepository.getIndicators(company, name, active);
    }
}
