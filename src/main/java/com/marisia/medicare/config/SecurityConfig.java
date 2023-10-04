package com.marisia.medicare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;

import com.marisia.medicare.service.AppUserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  private final AppUserService userService;

  public SecurityConfig(AppUserService userService) {
    this.userService = userService;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(
        new ClearSiteDataHeaderWriter(Directive.COOKIES));

    http
        .csrf(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/login", "/register").anonymous()
                .requestMatchers("/", "/static/**", "/error").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated())
        .headers(
            headers -> headers.frameOptions(f -> f.sameOrigin()))
        .userDetailsService(userService)
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", false))
        .rememberMe(Customizer.withDefaults())
        .logout(
            (logout) -> logout
                .addLogoutHandler(clearSiteData)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll());

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
