package com.dionathan.lavapro.user;

import com.dionathan.lavapro.auth.dto.AuthSignUpRequestDTO;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.user.dto.UserRequestDTO;
import com.dionathan.lavapro.user.dto.UserResponseDTO;
import com.dionathan.lavapro.user.dto.UserUpdateRequestDTO;
import com.dionathan.lavapro.user.enums.Role;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {


    public User toEntity(AuthSignUpRequestDTO requestDTO, Company company, String encodedPassword, Role role){

        User user = new User();

        user.setName(requestDTO.name());
        user.setEmail(requestDTO.email().trim().toLowerCase());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setCompany(company);

        return user;
    }

    public User toEntityUser(UserRequestDTO requestDTO, Company company, String encodedPassword){

        User user = new User();

        user.setName(requestDTO.name());
        user.setEmail(requestDTO.email().trim().toLowerCase());
        user.setPassword(encodedPassword);
        user.setRole(requestDTO.role());
        user.setCompany(company);

        return user;
    }

    public UserResponseDTO fromEntity(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdateAt(),
                user.getDeletedAt()
        );
    }

    public List<UserResponseDTO> fromEntity(List<User> users) {
        return users.stream().map(this::fromEntity).toList();
    }


}
