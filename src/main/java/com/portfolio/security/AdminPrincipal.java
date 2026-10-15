package com.portfolio.security;

import com.portfolio.constant.AppConstants;
import com.portfolio.entity.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class AdminPrincipal implements UserDetails {

    private final String id;
    private final String fullName;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public static AdminPrincipal create(AdminUser adminUser) {
        String roleName = adminUser.getRole() != null ? adminUser.getRole().name() : AppConstants.ROLE_ADMIN_NAME;
        if (!roleName.startsWith(AppConstants.ROLE_PREFIX)) {
            roleName = AppConstants.ROLE_PREFIX + roleName;
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        return new AdminPrincipal(
            adminUser.getId(),
            adminUser.getFullName(),
            adminUser.getEmail(),
            adminUser.getPassword(),
            adminUser.isEnabled(),
            Collections.singletonList(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
