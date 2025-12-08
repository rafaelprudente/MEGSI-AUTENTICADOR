package pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pt.uminho.megsi.authenticator.infrastructure.adapter.in.api.request.ChangePasswordRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ChangePasswordRequest> {

    @Override
    public boolean isValid(ChangePasswordRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getPasswordConfirmation() == null) {
            return false;
        }
        return request.getPassword().equals(request.getPasswordConfirmation());
    }
}