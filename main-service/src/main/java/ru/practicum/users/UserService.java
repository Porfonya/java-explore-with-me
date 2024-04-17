package ru.practicum.users;

import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    void deleteUser(Long userId);

    List<UserDto> getAllUsers(int from, int size);

    List<UserDto> getSomeUsers(List<Long> userId, int from, int size);
}
