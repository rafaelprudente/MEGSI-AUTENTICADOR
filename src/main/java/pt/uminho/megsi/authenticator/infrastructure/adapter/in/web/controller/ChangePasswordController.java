package pt.uminho.megsi.authenticator.infrastructure.adapter.in.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.uminho.megsi.authenticator.application.dto.ChangePasswordDto;
import pt.uminho.megsi.authenticator.application.port.Authenticator;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pages")
public class ChangePasswordController {
    private final Authenticator authenticatorService;

    @GetMapping("/change-password")
    public String formChangePasswordPage(@RequestParam() String hash, Model model) {
        model.addAttribute("hash", hash);

        return "pages/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String hash,
                                 @RequestParam String newPassword,
                                 @RequestParam String newPasswordConfirmation,
                                 Model model) {

        if (!newPassword.equals(newPasswordConfirmation)) {
            model.addAttribute("error", "Passwords must match.");
        } else {
            try {
                authenticatorService.changePassword(ChangePasswordDto.builder().hash(hash).password(newPassword).build());

                model.addAttribute("message", "Password Changed Successfully.");
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        return "pages/change-password";
    }
}
