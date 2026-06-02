package com.example.timesheet.controller;

import com.example.timesheet.service.PasswordChangeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordChangeController {

    private final PasswordChangeService passwordChangeService;

    public PasswordChangeController(PasswordChangeService passwordChangeService) {
        this.passwordChangeService = passwordChangeService;
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        model.addAttribute("username", authentication.getName());
        model.addAttribute("requirePasswordChange", passwordChangeService.isPasswordChangeRequired(authentication.getName()));
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        if (newPassword == null || newPassword.isBlank()) {
            model.addAttribute("error", "New password cannot be empty.");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
        } else if (newPassword.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long.");
        }

        if (model.containsAttribute("error")) {
            model.addAttribute("username", username);
            model.addAttribute("requirePasswordChange", passwordChangeService.isPasswordChangeRequired(username));
            return "change-password";
        }

        passwordChangeService.changePassword(username, newPassword);
        return "redirect:/";
    }
}
