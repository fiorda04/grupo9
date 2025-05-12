package com.oo2.grupo9.config;

import com.oo2.grupo9.services.implementation.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitamos CSRF por ahora (ya lo habilitaremos correctamente)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/**", "/imgs/**", "/js/**", "/vendor/**", "/", "/img/**").permitAll();  // Permitimos acceso a recursos estáticos y a la raíz
                    auth.requestMatchers("/usuarios/inscribirse").permitAll(); // Permitimos acceso a la página de inscripción sin autenticación
                    auth.requestMatchers("/usuarios/login").permitAll();
                    auth.requestMatchers("/usuarios/agregar").permitAll();
                    auth.anyRequest().authenticated(); // Cualquier otra petición requiere autenticación
                })
                .formLogin(login -> {
                    login.loginPage("/usuarios/login"); // Especificamos nuestra página de login personalizada
                    login.loginProcessingUrl("/loginprocess"); // URL a la que se enviarán las credenciales del login
                    login.usernameParameter("nombreUsuario"); // Nombre del parámetro del formulario para el nombre de usuario
                    login.passwordParameter("contraseña"); // Nombre del parámetro del formulario para la contraseña
                    login.defaultSuccessUrl("/", true); // URL a la que se redirige después de un login exitoso
                    login.permitAll(); // Permitimos acceso a la página de login sin autenticación
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout"); // URL para realizar el logout
                    logout.logoutSuccessUrl("/login?logout"); // URL a la que se redirige después del logout
                    logout.permitAll(); // Permitimos acceso a la URL de logout
                })
                .build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService); // Establecemos nuestro UserService
        provider.setPasswordEncoder(passwordEncoder()); // Establecemos el PasswordEncoder
        return provider;
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
