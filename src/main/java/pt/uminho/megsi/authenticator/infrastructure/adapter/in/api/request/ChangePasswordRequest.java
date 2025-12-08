package pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.validator.PasswordMatches;

@Getter
@Setter
@PasswordMatches(message = "Passwords must match.")
public class ChangePasswordRequest {
    @NotBlank(message = "Hash cannot be blank.")
    private String hash;
    @NotBlank(message = "Password cannot be blank.")
    private String password;
    @NotBlank(message = "Password confirmation cannot be blank.")
    private String passwordConfirmation;
}
