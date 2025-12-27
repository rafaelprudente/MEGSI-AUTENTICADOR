package pt.uminho.megsi.authenticator.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pt.uminho.megsi.authenticator.application.dto.AuthenticationDto;
import pt.uminho.megsi.authenticator.application.dto.UserDto;
import pt.uminho.megsi.authenticator.application.port.UserPersistence;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder jwtEncoder;
    private final UserPersistence userPersistence;

    @Value("${app.jwt_expiration}")
    private long jwtExpiration;


    public AuthenticationDto getGeneratedToken(Authentication authentication) {
        String userId = "";

        Optional<UserDto> user = userPersistence.findByUsername(authentication.getName());
        if (user.isPresent()) {
            userId = user.get().getId().toString();
        }

        Instant expiresAt = Instant.now().plus(jwtExpiration, ChronoUnit.SECONDS);
        AuthenticationDto authenticationDto = AuthenticationDto.builder().token(jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder()
                .issuer("jsdp-security")
                .issuedAt(Instant.now())
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("id", userId)
                .claim("scope", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
                .build())).getTokenValue()).expiresIn(expiresAt.getEpochSecond()).build();

        log.info("Generated JWT token to: {}", authentication.getName());

        return authenticationDto;
    }
}
