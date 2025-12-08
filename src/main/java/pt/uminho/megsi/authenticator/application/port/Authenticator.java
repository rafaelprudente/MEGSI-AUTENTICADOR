package pt.uminho.megsi.authenticator.application.port;

import org.springframework.security.core.Authentication;
import pt.uminho.megsi.authenticator.application.dto.AuthenticationDto;
import pt.uminho.megsi.authenticator.application.dto.ChangePasswordDto;
import pt.uminho.megsi.authenticator.application.dto.UserDto;

import java.io.IOException;

public interface Authenticator {
    AuthenticationDto authenticate(Authentication authentication);

    UserDto register(UserDto registration) throws IOException;

    void changePassword(ChangePasswordDto o);
}
