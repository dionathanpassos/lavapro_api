package com.dionathan.lavapro.auth;

import com.dionathan.lavapro.auth.dto.AuthLoginRequestDTO;
import com.dionathan.lavapro.auth.dto.AuthResponseDTO;
import com.dionathan.lavapro.auth.dto.AuthSignUpRequestDTO;
import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.UserDisabledException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.company.CompanyMapper;
import com.dionathan.lavapro.company.CompanyRepository;
import com.dionathan.lavapro.company.enums.CompanyStatus;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.security.JwtService;
import com.dionathan.lavapro.user.User;
import com.dionathan.lavapro.user.UserMapper;
import com.dionathan.lavapro.user.UserRepository;
import com.dionathan.lavapro.user.dto.UserResponseDTO;
import com.dionathan.lavapro.user.enums.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    @Transactional
    public UserResponseDTO register(AuthSignUpRequestDTO requestDTO) {

        if(userRepository.existsByEmail(requestDTO.email())) {
            throw new BusinessException("Email em utilização");
        }

        Company company = companyMapper.toEntity(requestDTO, CompanyStatus.ACTIVE);
        Company newCompany = companyRepository.save(company);

        String encodedPassword = passwordEncoder.encode(requestDTO.password());

        User user = userMapper.toEntity(requestDTO, newCompany, encodedPassword, Role.ROLE_OWNER);
        User saved = userRepository.save(user);

        return userMapper.fromEntity(saved);

    }

    public AuthResponseDTO login(AuthLoginRequestDTO requestDTO) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDTO.email(),
                            requestDTO.password()
                    )
            );
        } catch (DisabledException e) {
            throw new UserDisabledException("Sua conta está desativada. Entre em contato com o administrador.");
        }

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token);
    }
}
