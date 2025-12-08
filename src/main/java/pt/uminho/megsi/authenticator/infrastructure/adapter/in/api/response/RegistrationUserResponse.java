package pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonPropertyOrder({"id", "name", "email", "enabled", "createdAt"})
public class RegistrationUserResponse {
    private UUID id;
    private String name;
    private String email;
    private boolean enabled;
    private LocalDateTime createdAt;
}
