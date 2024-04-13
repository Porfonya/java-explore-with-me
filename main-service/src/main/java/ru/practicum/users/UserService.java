package ru.practicum.users;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    void deleteUser(Long userId);

    List<UserDto> getAllUsers(int from, int size);
}
