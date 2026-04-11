package io.cred0.core.roles;

import java.util.List;
import java.util.UUID;

import io.cred0.core.roles.api.RoleApiExceptionHandler;
import io.cred0.core.roles.api.RoleController;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.roles.service.RoleConflictException;
import io.cred0.core.roles.service.RoleNotFoundException;
import io.cred0.core.roles.service.RoleService;
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
class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new RoleController(roleService))
                .setControllerAdvice(new RoleApiExceptionHandler())
                .build();
    }

    @Test
    void getAllReturnsRoles() throws Exception {
        RoleEntity role = roleEntity();
        when(roleService.findAll()).thenReturn(List.of(role));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0].id").value(role.getId().toString()));
    }

    @Test
    void malformedUuidReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/roles/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("id must be a valid UUID"));
    }

    @Test
    void validationFailureReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createReturnsCreated() throws Exception {
        RoleEntity role = roleEntity();
        when(roleService.create(any(RoleEntity.class), any(), any())).thenReturn(role);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(role.getId().toString()));
    }

    @Test
    void updateNotFoundReturnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(roleService.update(eq(id), any(RoleEntity.class), any(), any()))
                .thenThrow(new RoleNotFoundException("Role not found: " + id));

        mockMvc.perform(put("/api/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void conflictReturns409() throws Exception {
        doThrow(new RoleConflictException("role name already exists", null))
                .when(roleService).create(any(RoleEntity.class), any(), any());

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(roleService).deleteById(id);

        mockMvc.perform(delete("/api/roles/{id}", id))
                .andExpect(status().isNoContent());
    }

    private RoleEntity roleEntity() {
        RoleEntity role = new RoleEntity();
        role.setId(UUID.randomUUID());
        role.setName("manager");
        role.setDescription("managers");
        role.setCreatedTimestamp(100L);
        role.setLastModifiedTimestamp(100L);
        return role;
    }

    private String validBody() {
        return """
                {
                  \"name\": \"manager\",
                  \"description\": \"managers\",
                  \"userIds\": [],
                  \"groupIds\": []
                }
                """;
    }
}
