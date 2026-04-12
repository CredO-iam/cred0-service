package io.cred0.core.roles.api;

import java.util.UUID;

import io.cred0.core.roles.mappers.RoleEntityMapper;
import io.cred0.core.roles.service.RoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public RolesResponse findAll() {
        return new RolesResponse(RoleEntityMapper.toResponseDtos(this.roleService.findAll()));
    }

    @GetMapping("/{id}")
    public RoleResponseDto findById(@PathVariable String id) {
        UUID roleId = RoleRequestValidator.parseUuid(id);
        return RoleEntityMapper.toResponseDto(this.roleService.findById(roleId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto create(@RequestBody RoleRequestDto request) {
        RoleRequestValidator.validateForCreate(request);
        return RoleEntityMapper.toResponseDto(this.roleService.create(
                RoleEntityMapper.fromCreateRequest(request),
                RoleEntityMapper.toIdSet(request.userIds()),
                RoleEntityMapper.toIdSet(request.groupIds())
        ));
    }

    @PutMapping("/{id}")
    public RoleResponseDto update(@PathVariable String id, @RequestBody RoleRequestDto request) {
        UUID roleId = RoleRequestValidator.parseUuid(id);
        RoleRequestValidator.validateForUpdate(request);
        return RoleEntityMapper.toResponseDto(this.roleService.update(
                roleId,
                RoleEntityMapper.fromUpdateRequest(request),
                RoleEntityMapper.toIdSet(request.userIds()),
                RoleEntityMapper.toIdSet(request.groupIds())
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        UUID roleId = RoleRequestValidator.parseUuid(id);
        this.roleService.deleteById(roleId);
    }
}
