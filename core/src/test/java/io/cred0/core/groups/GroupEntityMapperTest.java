package io.cred0.core.groups;

import java.util.Set;
import java.util.UUID;

import io.cred0.core.groups.api.GroupRequestDto;
import io.cred0.core.groups.api.GroupResponseDto;
import io.cred0.core.groups.mappers.GroupEntityMapper;
import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.users.persistence.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupEntityMapperTest {

    @Test
    void fromCreateRequestMapsCommonFields() {
        GroupRequestDto request = new GroupRequestDto("  admins  ", "  internal admins  ", null, null);

        GroupEntity entity = GroupEntityMapper.fromCreateRequest(request);

        assertEquals("admins", entity.getName());
        assertEquals("internal admins", entity.getDescription());
    }

    @Test
    void toResponseDtoMapsRelationshipIds() {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(userId);
        RoleEntity role = new RoleEntity();
        role.setId(roleId);

        GroupEntity entity = new GroupEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("admins");
        entity.setDescription("internal admins");
        entity.setCreatedTimestamp(100L);
        entity.setLastModifiedTimestamp(200L);
        entity.setUsers(Set.of(user));
        entity.setRoles(Set.of(role));

        GroupResponseDto response = GroupEntityMapper.toResponseDto(entity);

        assertEquals(userId, response.userIds().getFirst());
        assertEquals(roleId, response.roleIds().getFirst());
    }
}
