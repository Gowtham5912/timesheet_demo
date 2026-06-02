package com.example.timesheet.config;

import com.example.timesheet.service.PasswordChangeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PasswordChangeFilter passwordChangeFilter) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/approve/**", "/reject/**", "/delete/**").hasRole("MANAGER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        http.addFilterBefore(passwordChangeFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build();

        UserDetails manager = User.withUsername("manager")
            .password(passwordEncoder.encode("password"))
            .roles("MANAGER")
            .build();

        return new InMemoryUserDetailsManager(user, manager);
    }

    @Bean
    public PasswordChangeService passwordChangeService(InMemoryUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        return new PasswordChangeService(userDetailsManager, passwordEncoder, Set.of("user", "manager"));
    }

    @Bean
    public PasswordChangeFilter passwordChangeFilter(PasswordChangeService passwordChangeService) {
        return new PasswordChangeFilter(passwordChangeService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
