package pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    @Email(message = "Invalid email address.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;
    @NotBlank(message = "Name cannot be blank.")
    private String name;
}
