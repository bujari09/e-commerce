package com.e_commerce.service;

import com.e_commerce.dto.UserDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    UserDto registerUser(UserDto userDto);
    UserDto loginUser(String email, String password);
}