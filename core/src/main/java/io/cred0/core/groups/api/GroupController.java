package io.cred0.core.groups.api;

import java.util.UUID;

import io.cred0.core.groups.mappers.GroupEntityMapper;
import io.cred0.core.groups.service.GroupService;
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
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public GroupsResponse findAll() {
        return new GroupsResponse(GroupEntityMapper.toResponseDtos(this.groupService.findAll()));
    }

    @GetMapping("/{id}")
    public GroupResponseDto findById(@PathVariable String id) {
        UUID groupId = GroupRequestValidator.parseUuid(id);
        return GroupEntityMapper.toResponseDto(this.groupService.findById(groupId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponseDto create(@RequestBody GroupRequestDto request) {
        GroupRequestValidator.validateForCreate(request);
        return GroupEntityMapper.toResponseDto(this.groupService.create(
                GroupEntityMapper.fromCreateRequest(request),
                GroupEntityMapper.toIdSet(request.userIds()),
                GroupEntityMapper.toIdSet(request.roleIds())
        ));
    }

    @PutMapping("/{id}")
    public GroupResponseDto update(@PathVariable String id, @RequestBody GroupRequestDto request) {
        UUID groupId = GroupRequestValidator.parseUuid(id);
        GroupRequestValidator.validateForUpdate(request);
        return GroupEntityMapper.toResponseDto(this.groupService.update(
                groupId,
                GroupEntityMapper.fromUpdateRequest(request),
                GroupEntityMapper.toIdSet(request.userIds()),
                GroupEntityMapper.toIdSet(request.roleIds())
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        UUID groupId = GroupRequestValidator.parseUuid(id);
        this.groupService.deleteById(groupId);
    }
}
