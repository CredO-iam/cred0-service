package io.cred0.core.users.api;

import java.util.UUID;

import io.cred0.core.users.mappers.UserEntityMapper;
import io.cred0.core.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UsersResponse findAll() {
        return new UsersResponse(UserEntityMapper.toResponseDtos(this.userService.findAll()));
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable String id) {
        UUID userId = UserRequestValidator.parseUuid(id);
        return UserEntityMapper.toResponseDto(this.userService.findById(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody UserRequestDto request) {
        UserRequestValidator.validateForCreate(request);
        return UserEntityMapper.toResponseDto(this.userService.create(UserEntityMapper.fromCreateRequest(request)));
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable String id, @RequestBody UserRequestDto request) {
        UUID userId = UserRequestValidator.parseUuid(id);
        UserRequestValidator.validateForUpdate(request);
        return UserEntityMapper.toResponseDto(this.userService.update(userId, UserEntityMapper.fromUpdateRequest(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        UUID userId = UserRequestValidator.parseUuid(id);
        this.userService.deleteById(userId);
    }
}
