package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.checker.Checker;
import ru.practicum.users.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/admin/users")
public class UserController {
    public final UserServiceImpl userService;
    public Checker checker;

    @PostMapping()
    public ResponseEntity<UserDto> addUser(@RequestBody @Validated UserDto userDto) {
        log.info("Добавление нового пользователя");
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size){
       return userService.getAllUsers(from, size);
    }

    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "id") Long userId) {
        checker.checkerUser(userId);
        log.info("Категория удалена");
        userService.deleteUser(userId);
    }



}
