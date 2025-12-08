package pt.uminho.megsi.authenticator.application.port;

import pt.uminho.megsi.authenticator.application.dto.UserDto;

import java.util.Optional;

public interface UserPersistence {
    Optional<UserDto> findByUsername(String username);

    UserDto register(UserDto registration);

    Optional<UserDto> findUserByHash(String hash);

    void changePassword(UserDto user);
}
