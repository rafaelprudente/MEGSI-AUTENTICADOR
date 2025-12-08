package pt.uminho.megsi.authenticator.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationDto {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private long expiresIn;
}
