package io.cred0.core.security;

import java.util.HashSet;
import java.util.Set;

import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CoreUserDetailsService implements UserDetailsService {

    private final JpaUserEntityRepository userRepository;

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findOneByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!"password".equalsIgnoreCase(user.getCredentialsType()) || !StringUtils.hasText(user.getCredentialsValue())) {
            throw new UsernameNotFoundException("User has no password credentials: " + username);
        }

        return User.withUsername(user.getUsername())
                .password(user.getCredentialsValue())
                .disabled(!user.isEnabled())
                .authorities(extractAuthorities(user))
                .build();
    }

    private Set<GrantedAuthority> extractAuthorities(UserEntity user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> addRoleAuthority(authorities, role.getName()));
        user.getGroups().forEach(group -> group.getRoles().forEach(role -> addRoleAuthority(authorities, role.getName())));
        return authorities;
    }

    private void addRoleAuthority(Set<GrantedAuthority> authorities, String roleName) {
        if (!StringUtils.hasText(roleName)) {
            return;
        }

        String normalized = roleName.trim().toUpperCase();
        String authority = normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
        authorities.add(new SimpleGrantedAuthority(authority));
    }
}

