package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@Valid @RequestBody NewUserDto newUserDto) {
        log.info("POST /users - создание пользователя с email '{}'", newUserDto.getEmail());
        UserResponseDto createdUser = userService.create(newUserDto);
        log.debug("Пользователь создан: {}", createdUser);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public UserResponseDto update(@Positive  @PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        log.info("PATCH /users/{} - обновление пользователя", id);
        UserResponseDto updatedUser = userService.update(id, userUpdateDto);
        log.debug("Пользователь обновлен: {}", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@Positive @PathVariable Long id) {
        log.info("GET /users/{} - получение пользователя", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        log.info("GET /users - получение списка всех пользователей");
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable Long id) {
        log.info("DELETE /users/{} - удаление пользователя", id);
        userService.delete(id);
        log.debug("Пользователь с ID={} удалён", id);
    }
}