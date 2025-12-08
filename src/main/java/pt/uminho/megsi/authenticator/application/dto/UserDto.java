package pt.uminho.megsi.authenticator.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDto implements UserDetails {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String changePasswordHash;
    private boolean enabled;
    private boolean requestChangePassword;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime lockedAt;
    private Set<RoleDto> authorities;
}
