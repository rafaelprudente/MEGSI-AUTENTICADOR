package pt.uminho.megsi.authenticator.domain.enummerator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
    ROLE_SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_FISCAL("ROLE_FISCAL"),
    ROLE_DRIVER("ROLE_DRIVER");

    final String value;

    @Override
    public String toString() {
        return value;
    }
}
