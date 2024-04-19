package ru.practicum.users;

import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    void deleteUser(Long userId);

    List<UserDto> getAllUsers(int from, int size);

    List<UserDto> getUsersByIds(List<Long> userId, int from, int size);
}
