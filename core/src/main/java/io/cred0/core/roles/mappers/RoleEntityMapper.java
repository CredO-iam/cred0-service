package io.cred0.core.roles.mappers;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.roles.api.RoleRequestDto;
import io.cred0.core.roles.api.RoleResponseDto;
import io.cred0.core.roles.persistence.RoleEntity;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RoleEntityMapper {

    public static RoleEntity fromCreateRequest(RoleRequestDto request) {
        RoleEntity entity = new RoleEntity();
        mapCommonFields(request, entity);
        return entity;
    }

    public static RoleEntity fromUpdateRequest(RoleRequestDto request) {
        RoleEntity entity = new RoleEntity();
        mapCommonFields(request, entity);
        return entity;
    }

    public static RoleResponseDto toResponseDto(RoleEntity entity) {
        return new RoleResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedTimestamp(),
                entity.getLastModifiedTimestamp(),
                entity.getUsers().stream().map(user -> user.getId()).sorted().toList(),
                entity.getGroups().stream().map(group -> group.getId()).sorted().toList()
        );
    }

    public static List<RoleResponseDto> toResponseDtos(List<RoleEntity> entities) {
        return entities.stream().map(RoleEntityMapper::toResponseDto).toList();
    }

    public static Set<UUID> toIdSet(List<UUID> ids) {
        if (ids == null) {
            return Set.of();
        }
        return new LinkedHashSet<>(ids);
    }

    private static void mapCommonFields(RoleRequestDto request, RoleEntity entity) {
        entity.setName(request.name().trim());
        entity.setDescription(StringUtils.hasText(request.description()) ? request.description().trim() : null);
    }
}
