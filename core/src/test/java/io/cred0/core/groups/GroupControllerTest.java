package io.cred0.core.groups;

import java.util.List;
import java.util.UUID;

import io.cred0.core.groups.api.GroupApiExceptionHandler;
import io.cred0.core.groups.api.GroupController;
import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.groups.service.GroupConflictException;
import io.cred0.core.groups.service.GroupNotFoundException;
import io.cred0.core.groups.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new GroupController(groupService))
                .setControllerAdvice(new GroupApiExceptionHandler())
                .build();
    }

    @Test
    void getAllReturnsGroups() throws Exception {
        GroupEntity group = groupEntity();
        when(groupService.findAll()).thenReturn(List.of(group));

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groups[0].id").value(group.getId().toString()));
    }

    @Test
    void malformedUuidReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/groups/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("id must be a valid UUID"));
    }

    @Test
    void validationFailureReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createReturnsCreated() throws Exception {
        GroupEntity group = groupEntity();
        when(groupService.create(any(GroupEntity.class), any(), any())).thenReturn(group);

        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(group.getId().toString()));
    }

    @Test
    void updateNotFoundReturnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(groupService.update(eq(id), any(GroupEntity.class), any(), any()))
                .thenThrow(new GroupNotFoundException("Group not found: " + id));

        mockMvc.perform(put("/api/groups/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void conflictReturns409() throws Exception {
        doThrow(new GroupConflictException("group name already exists", null))
                .when(groupService).create(any(GroupEntity.class), any(), any());

        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(groupService).deleteById(id);

        mockMvc.perform(delete("/api/groups/{id}", id))
                .andExpect(status().isNoContent());
    }

    private GroupEntity groupEntity() {
        GroupEntity group = new GroupEntity();
        group.setId(UUID.randomUUID());
        group.setName("admins");
        group.setDescription("internal admins");
        group.setCreatedTimestamp(100L);
        group.setLastModifiedTimestamp(100L);
        return group;
    }

    private String validBody() {
        return """
                {
                  \"name\": \"admins\",
                  \"description\": \"internal admins\",
                  \"userIds\": [],
                  \"roleIds\": []
                }
                """;
    }
}
