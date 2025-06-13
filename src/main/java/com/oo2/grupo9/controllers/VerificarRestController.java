package com.oo2.grupo9.controllers;

import com.oo2.grupo9.dtos.LoginRequestDTO;
import com.oo2.grupo9.dtos.LoginResponseDTO;
import com.oo2.grupo9.services.implementation.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Verificacion", description = "Endpoints para el inicio de sesión y gestión de tokens.")
public class VerificarRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Operation(
        summary = "Verificar usuario", 
        description = "Permite a un usuario iniciar sesión con su nombre de usuario y contraseña para obtener un token de acceso JWT(JSON WEB TOKENS)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa", 
                     content = { @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = LoginResponseDTO.class)) }),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        String jwt = jwtService.generateToken(authentication); 
        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }
}