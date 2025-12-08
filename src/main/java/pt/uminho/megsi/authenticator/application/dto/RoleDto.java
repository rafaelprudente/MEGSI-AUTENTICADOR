package pt.uminho.megsi.authenticator.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Builder
public class RoleDto implements GrantedAuthority {
    private Long id;
    private String authority;
}
