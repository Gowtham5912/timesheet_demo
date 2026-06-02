package com.example.timesheet.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PasswordChangeService {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final Set<String> requiredChangeUsers = ConcurrentHashMap.newKeySet();

    public PasswordChangeService(InMemoryUserDetailsManager userDetailsManager,
                                 PasswordEncoder passwordEncoder,
                                 Set<String> defaultUsersRequiringPasswordChange) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.requiredChangeUsers.addAll(defaultUsersRequiringPasswordChange);
    }

    public boolean isPasswordChangeRequired(String username) {
        return username != null && requiredChangeUsers.contains(username);
    }

    public void changePassword(String username, String newPassword) {
        UserDetails current = userDetailsManager.loadUserByUsername(username);
        UserDetails updated = User.withUserDetails(current)
                .password(passwordEncoder.encode(newPassword))
                .build();
        userDetailsManager.updateUser(updated);
        requiredChangeUsers.remove(username);
    }
}
