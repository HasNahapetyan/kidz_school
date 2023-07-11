package com.example.kidz_school.mapper;

import com.example.kidz_school.dto.UserDTO;
import com.example.kidz_school.dto.UserRegistrationRequestDto;
import com.example.kidz_school.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO userDTO) ;

    UserDTO toDTO(User user) ;

    User mapFromRegisterDto(UserRegistrationRequestDto userRegistrationRequestDto);
}
