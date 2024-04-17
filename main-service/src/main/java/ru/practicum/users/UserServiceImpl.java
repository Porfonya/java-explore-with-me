package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictExc;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {

        if (userRepository.existsUserByEmail(userDto.getEmail())) {
            throw new ConflictExc("Такая почта уже существует");
        } else {
            User user = userMapper.mapToUser(userDto);
            return userMapper.mapToUserDto(userRepository.save(user));
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException(userId);
        }

    }


    @Override
    public List<UserDto> getAllUsers(int from, int size) {

        Pageable page = PageRequest.of(from / size, size);
        log.info("Поиск всех пользователей");
        List<User> users = userRepository.findAll(page).getContent();
        return userMapper.mapToListUserDto(users);

    }

    @Override
    public List<UserDto> getSomeUsers(List<Long> userId, int from, int size) {
        log.info("Поиск по списку id пользователей");
        Pageable page = PageRequest.of(from / size, size);
        List<User> users = userRepository.findByIdIn(userId, page);
        return userMapper.mapToListUserDto(users);
    }

}
