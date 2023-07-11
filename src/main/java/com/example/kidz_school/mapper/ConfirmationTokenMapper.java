package com.example.kidz_school.mapper;

import com.example.kidz_school.entity.ConfirmationToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfirmationTokenMapper {
    ConfirmationToken toEntity(String token);
}
