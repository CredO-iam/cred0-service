package io.cred0.core.groups.persistence;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.users.persistence.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_groups_name", columnNames = "name")
        }
)
@Getter
@Setter
public class GroupEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_timestamp", nullable = false)
    private Long createdTimestamp;

    @Column(name = "last_modified_timestamp", nullable = false)
    private Long lastModifiedTimestamp;

    @ManyToMany
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_user_groups", columnNames = {"group_id", "user_id"})
    )
    private Set<UserEntity> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "group_roles",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_group_roles", columnNames = {"group_id", "role_id"})
    )
    private Set<RoleEntity> roles = new HashSet<>();
}
