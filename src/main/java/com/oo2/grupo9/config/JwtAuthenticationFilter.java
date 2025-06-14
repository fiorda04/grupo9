package com.oo2.grupo9.config;

import com.oo2.grupo9.services.implementation.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; //esto es para leer y validar el token
    private final UserDetailsService userDetailsService; 

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    //Este metodo principal se va a ejecutar en cada peticion
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { //si la cabecera no empieza con Bearer no verifica el token como lo login basicamente esta publico
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); //se extrae el token
        final String username = jwtService.extractUsername(jwt); //con el token que se pasa se trae el nombre de usuario

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { //consultamos si pudimoss leer un nusuarion valido del token y ademas si no se autentio todavia el usuario en la peticion
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username); //traemos los datos de esto importante la contraseña hasgeada y los roles
            if (jwtService.isTokenValid(jwt, userDetails)) { //compara si el token es valido para este usuario y obvio si no expiro
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //se pone null porque esta aautenticado por token no por contrasenia
                        userDetails.getAuthorities() //rolesss
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken); //a partir de aca spring sabe que el usuario esta autenticado. Espo permite que fuinones los @preautorize
            }
        }
        filterChain.doFilter(request, response);
    }
}