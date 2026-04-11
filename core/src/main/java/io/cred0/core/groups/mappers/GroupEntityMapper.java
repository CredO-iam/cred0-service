package io.cred0.core.groups.mappers;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.groups.api.GroupRequestDto;
import io.cred0.core.groups.api.GroupResponseDto;
import io.cred0.core.groups.persistence.GroupEntity;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class GroupEntityMapper {

    public static GroupEntity fromCreateRequest(GroupRequestDto request) {
        GroupEntity entity = new GroupEntity();
        mapCommonFields(request, entity);
        return entity;
    }

    public static GroupEntity fromUpdateRequest(GroupRequestDto request) {
        GroupEntity entity = new GroupEntity();
        mapCommonFields(request, entity);
        return entity;
    }

    public static GroupResponseDto toResponseDto(GroupEntity entity) {
        return new GroupResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedTimestamp(),
                entity.getLastModifiedTimestamp(),
                entity.getUsers().stream().map(user -> user.getId()).sorted().toList(),
                entity.getRoles().stream().map(role -> role.getId()).sorted().toList()
        );
    }

    public static List<GroupResponseDto> toResponseDtos(List<GroupEntity> entities) {
        return entities.stream().map(GroupEntityMapper::toResponseDto).toList();
    }

    public static Set<UUID> toIdSet(List<UUID> ids) {
        if (ids == null) {
            return Set.of();
        }
        return new LinkedHashSet<>(ids);
    }

    private static void mapCommonFields(GroupRequestDto request, GroupEntity entity) {
        entity.setName(request.name().trim());
        entity.setDescription(StringUtils.hasText(request.description()) ? request.description().trim() : null);
    }
}
