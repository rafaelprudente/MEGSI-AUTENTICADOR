package pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private long expiresIn;
}
