package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
public class UserDto {

    @Email(message = "Электронная почта заполнена неправильно")
    @Size(min = 6, max = 254, message = "Электронная почта должна иметь от 2 до 254 символов")
    @NotBlank(message = "Электронная почта не может состоять из пробелов")
    private String email;

    private Long id;
    @Size(min = 2, max = 250, message = "Имя должен иметь от 2 до 254 символов")
    @NotBlank(message = "Имя не может состоять из пробелов")
    private String name;

}

