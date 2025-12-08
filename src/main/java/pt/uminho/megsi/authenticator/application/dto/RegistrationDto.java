package pt.uminho.megsi.authenticator.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationDto {
    private String email;
    private String name;
}
