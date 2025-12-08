package pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pt.uminho.megsi.authenticator.application.port.Authenticator;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.mapper.WebAuthenticatorMapper;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.request.ChangePasswordRequest;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.request.RegistrationRequest;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.response.AuthenticationResponse;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.response.RegistrationUserResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authenticator")
public class AuthenticatorController {
    private final Authenticator authenticatorService;
    private final WebAuthenticatorMapper webAuthenticatorMapper;

    @SecurityRequirements
    @Operation(
            operationId = "authenticate",
            summary = "Authenticate user",
            description = "Validates user credentials and returns an authentication token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid username or password",
                            content = @Content(
                                    mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    @PostMapping(
            value = "/authenticate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthenticationResponse authenticate(Authentication authentication) {
        return webAuthenticatorMapper.dtoToResponse(authenticatorService.authenticate(authentication));
    }

    @PostMapping(
            value = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RegistrationUserResponse register(@Valid @RequestBody RegistrationRequest request) throws IOException {
        return webAuthenticatorMapper.userDtoToRegistrationUserResponse(authenticatorService.register(webAuthenticatorMapper.registrationRequestToDto(request)));
    }

    @PutMapping(
            value = "/change-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authenticatorService.changePassword(webAuthenticatorMapper.changePasswordRequestToDto(request));
    }
}
