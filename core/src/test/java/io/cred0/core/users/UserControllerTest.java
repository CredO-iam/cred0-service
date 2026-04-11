package io.cred0.core.users;

import java.util.List;
import java.util.UUID;

import io.cred0.core.users.api.UserApiExceptionHandler;
import io.cred0.core.users.api.UserController;
import io.cred0.core.users.persistence.UserEntity;
import io.cred0.core.users.service.UserConflictException;
import io.cred0.core.users.service.UserNotFoundException;
import io.cred0.core.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService))
                .setControllerAdvice(new UserApiExceptionHandler())
                .build();
    }

    @Test
    void getAllReturnsUsers() throws Exception {
        UserEntity user = userEntity();
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value(user.getId().toString()))
                .andExpect(jsonPath("$.users[0].credentials.secretSet").value(true));
    }

    @Test
    void malformedUuidReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/users/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("id must be a valid UUID"));
    }

    @Test
    void validationFailureReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createReturnsCreated() throws Exception {
        UserEntity user = userEntity();
        when(userService.create(any(UserEntity.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.credentials.secretSet").value(true));
    }

    @Test
    void updateNotFoundReturnsNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.update(eq(id), any(UserEntity.class)))
                .thenThrow(new UserNotFoundException("User not found: " + id));

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateBody()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void conflictReturns409() throws Exception {
        doThrow(new UserConflictException("username or email already exists", null))
                .when(userService).create(any(UserEntity.class));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateBody()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("username or email already exists"));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(userService).deleteById(id);

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());
    }

    private UserEntity userEntity() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("jdoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setCreatedTimestamp(123L);
        user.setLastModifiedTimestamp(123L);
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setAttributes("[{\"name\":\"department\",\"value\":\"engineering\"}]");
        user.setCredentialsType("password");
        user.setCredentialsValue("temporary-secret");
        return user;
    }

    private String validCreateBody() {
        return """
                {
                  \"username\": \"jdoe\",
                  \"firstName\": \"John\",
                  \"lastName\": \"Doe\",
                  \"email\": \"john.doe@example.com\",
                  \"enabled\": true,
                  \"emailVerified\": false,
                  \"attributes\": [
                    { \"name\": \"department\", \"value\": \"engineering\" }
                  ],
                  \"credentials\": {
                    \"type\": \"password\",
                    \"value\": \"temporary-secret\"
                  }
                }
                """;
    }

    private String validUpdateBody() {
        return """
                {
                  \"username\": \"jdoe\",
                  \"firstName\": \"John\",
                  \"lastName\": \"Doe\",
                  \"email\": \"john.doe@example.com\",
                  \"enabled\": true,
                  \"emailVerified\": true,
                  \"attributes\": [
                    { \"name\": \"department\", \"value\": \"engineering\" }
                  ]
                }
                """;
    }
}
