package pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.mapper;

import org.mapstruct.Mapper;
import pt.uminho.megsi.authenticator.application.dto.AuthenticationDto;
import pt.uminho.megsi.authenticator.application.dto.ChangePasswordDto;
import pt.uminho.megsi.authenticator.application.dto.UserDto;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.request.ChangePasswordRequest;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.request.RegistrationRequest;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.response.AuthenticationResponse;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.response.RegistrationUserResponse;

@Mapper(componentModel = "spring")
public interface WebAuthenticatorMapper {
    AuthenticationResponse dtoToResponse(AuthenticationDto authenticate);

    UserDto registrationRequestToDto(RegistrationRequest request);

    ChangePasswordDto changePasswordRequestToDto(ChangePasswordRequest request);

    RegistrationUserResponse userDtoToRegistrationUserResponse(UserDto register);
}
