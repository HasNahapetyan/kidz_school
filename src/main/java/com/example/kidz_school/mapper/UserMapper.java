package com.example.kidz_school.mapper;

import com.example.kidz_school.dto.UserRegistrationRequestDto;
import com.example.kidz_school.entity.User;
import com.example.kidz_school.security.CurrentUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapFromRegisterDto(UserRegistrationRequestDto userRegistrationRequestDto);

    User mapFromCurrent(CurrentUser currentUser);
}
