package pt.uminho.megsi.authenticator.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.uminho.megsi.authenticator.application.dto.AuthenticationDto;
import pt.uminho.megsi.authenticator.application.dto.ChangePasswordDto;
import pt.uminho.megsi.authenticator.application.dto.EmailDto;
import pt.uminho.megsi.authenticator.application.dto.RoleDto;
import pt.uminho.megsi.authenticator.application.dto.UserDto;
import pt.uminho.megsi.authenticator.application.port.Authenticator;
import pt.uminho.megsi.authenticator.application.port.UserPersistence;
import pt.uminho.megsi.authenticator.domain.enummerator.Roles;
import pt.uminho.megsi.authenticator.domain.exception.BusinessException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatorService implements Authenticator {
    private final JwtService jwtService;
    private final UserPersistence userPersistence;
    private final KafkaService kafkaService;
    private final PasswordEncoder passwordEncoder;
    private final ResourceLoader resourceLoader;

    @Value("${app.kafka_registration_topic}")
    String registrationTopic;
    @Value("${app.api_host}")
    String apiHost;

    @Override
    public AuthenticationDto authenticate(Authentication authentication) {
        return jwtService.getGeneratedToken(authentication);
    }

    @Override
    public UserDto register(UserDto registration) {
        userPersistence.findByUsername(registration.getEmail()).ifPresent(user -> {
            throw new BusinessException("User already exists.");
        });

        registration.setUsername(registration.getEmail());
        registration.setEmail(registration.getEmail());
        registration.setName(registration.getName());
        registration.setRequestChangePassword(true);
        registration.setChangePasswordHash(generateHash256());
        registration.setAccountNonExpired(true);
        registration.setAccountNonLocked(true);
        registration.setCredentialsNonExpired(true);
        registration.setCreatedAt(LocalDateTime.now());
        registration.setUpdatedAt(LocalDateTime.now());
        registration.setAuthorities(new HashSet<>(Collections.singleton(RoleDto.builder().authority((Roles.ROLE_DRIVER.toString())).build())));

        UserDto newUser = userPersistence.register(registration);

        sendEmail(newUser);

        return newUser;
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        PasswordValidator validator = getPasswordValidator();
        RuleResult result = validator.validate(new PasswordData(changePasswordDto.getPassword()));
        if (!result.isValid()) {
            throw new BusinessException(String.join(" ", validator.getMessages(result)));
        }

        UserDto user = userPersistence.findUserByHash(changePasswordDto.getHash()).orElseThrow();

        user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
        user.setChangePasswordHash(null);
        user.setEnabled(true);
        user.setRequestChangePassword(false);
        user.setUpdatedAt(LocalDateTime.now());

        userPersistence.changePassword(user);
    }

    private void sendEmail(UserDto newUser) {
        try {
            String userRegisteredEmailTemplate =
                    new BufferedReader(new InputStreamReader(
                            resourceLoader.getResource("classpath:email-templates/user-registered.html")
                                    .getInputStream()
                    ))
                            .lines()
                            .collect(Collectors.joining("\n"))
                            .replace("${nome}", newUser.getName())
                            .replace("${email}", newUser.getEmail())
                            .replace("${confirmationLink}", "http://" + apiHost + "/pages/change-password?hash=" + newUser.getChangePasswordHash());

            kafkaService.send(registrationTopic, EmailDto.builder()
                    .sender("Authentication Service <aluno.uminho.megsi@gmail.com>")
                    .recipient(newUser.getEmail())
                    .subject("Registration email")
                    .body(userRegisteredEmailTemplate)
                    .build());

        } catch (Exception e) {
            log.error("Error reading template: " + e.getMessage(), e);
        }
    }

    private static String generateHash256() {
        byte[] bytes = new byte[128];
        new SecureRandom().nextBytes(bytes);
        return generateHash256(bytes);
    }

    private static String generateHash256(byte[] value) {
        StringBuilder hexString = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(value);
            hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return hexString.toString();
    }

    private PasswordValidator getPasswordValidator() {
        LengthRule r1 = new LengthRule(8, 16);

        CharacterCharacteristicsRule r2 = new CharacterCharacteristicsRule();

        r2.setNumberOfCharacteristics(4);
        r2.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        r2.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        r2.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
        r2.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));

        WhitespaceRule r3 = new WhitespaceRule();

        return new PasswordValidator(r1, r2, r3);
    }
}
