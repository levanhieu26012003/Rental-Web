package com.lvh.RentalBE.mapper;

import com.lvh.RentalBE.dto.UserDTO;
import com.lvh.RentalBE.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(),user.getUsername(), user.getFullname(), user.getEmail(), user.getUserRole(), user.getAvatar(), user.getActive());
    }


    public static List<UserDTO> toDTOList(Collection<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}
