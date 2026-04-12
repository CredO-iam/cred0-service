package io.cred0.core.users.persistence;

import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.roles.persistence.RoleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, length = 200)
    private String username;

    @Column(name = "first_name", nullable = false, length = 200)
    private String firstName;

    @Column(name = "created_timestamp", nullable = false)
    private Long createdTimestamp;

    @Column(name = "last_modified_timestamp", nullable = false)
    private Long lastModifiedTimestamp;

    @Column(name = "last_name", nullable = false, length = 200)
    private String lastName;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Lob
    @Column(name = "attributes", nullable = false)
    private String attributes;

    @Column(name = "credentials_type", length = 120)
    private String credentialsType;

    @Lob
    @Column(name = "credentials_value")
    private String credentialsValue;

    @ManyToMany(mappedBy = "users")
    private Set<GroupEntity> groups = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<RoleEntity> roles = new HashSet<>();
}
