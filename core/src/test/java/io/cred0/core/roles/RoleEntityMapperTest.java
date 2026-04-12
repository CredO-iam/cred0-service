package io.cred0.core.roles;

import java.util.Set;
import java.util.UUID;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.roles.api.RoleRequestDto;
import io.cred0.core.roles.api.RoleResponseDto;
import io.cred0.core.roles.mappers.RoleEntityMapper;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.users.persistence.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleEntityMapperTest {

    @Test
    void fromCreateRequestMapsCommonFields() {
        RoleRequestDto request = new RoleRequestDto("  manager  ", "  managers  ", null, null);

        RoleEntity entity = RoleEntityMapper.fromCreateRequest(request);

        assertEquals("manager", entity.getName());
        assertEquals("managers", entity.getDescription());
    }

    @Test
    void toResponseDtoMapsRelationshipIds() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(userId);
        GroupEntity group = new GroupEntity();
        group.setId(groupId);

        RoleEntity entity = new RoleEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("manager");
        entity.setDescription("managers");
        entity.setCreatedTimestamp(100L);
        entity.setLastModifiedTimestamp(200L);
        entity.setUsers(Set.of(user));
        entity.setGroups(Set.of(group));

        RoleResponseDto response = RoleEntityMapper.toResponseDto(entity);

        assertEquals(userId, response.userIds().getFirst());
        assertEquals(groupId, response.groupIds().getFirst());
    }
}
