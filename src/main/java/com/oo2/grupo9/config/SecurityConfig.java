package com.oo2.grupo9.config;

import com.oo2.grupo9.services.implementation.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthFilter; // Inyecta el filtro

    public SecurityConfig(UserService userService, JwtAuthenticationFilter jwtAuthFilter) {
        this.userService = userService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider) 
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/",                                
                            "/css/**",                           
                            "/imgs/**",                          
                            "/js/**",                           
                            "/vendor/**",                        
                            "/swagger-ui/**", "/v3/api-docs/**", 
                            "/error",                            
                            "/usuarios/inscribirse",             
                            "/usuarios/agregar",                
                            "/login",                            
                            "/loginprocess"                      
                    ).permitAll()
                    .requestMatchers("/tickets/crear").hasRole("Cliente") // Solo CLIENTE puede ver el formulario de creación (GET)
                    .requestMatchers("/tickets/guardar").hasRole("Cliente") // Solo CLIENTE puede enviar el formulario de creación (POST)
                    .anyRequest().authenticated();
                })
                .formLogin(login -> {
                    // Configuración del formulario de login
                    login.loginPage("/usuarios/login");                
                    login.loginProcessingUrl("/loginprocess"); 
                    login.usernameParameter("username");       
                    login.passwordParameter("password");       
                    login.defaultSuccessUrl("/", true);                                              
                    //login.failureUrl("/login?error");          
                    login.permitAll();                        
                })
                .logout(logout -> {
                    // Configuración del cierre de sesión
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")); 
                    //logout.logoutSuccessUrl("/"); 
                    logout.invalidateHttpSession(true);      
                    logout.deleteCookies("JSESSIONID");      // Elimina la cookie de sesión
                    logout.permitAll();                      
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
