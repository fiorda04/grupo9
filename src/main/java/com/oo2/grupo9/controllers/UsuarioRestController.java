package com.oo2.grupo9.controllers;

import com.oo2.grupo9.dtos.CrearUsuarioRequest;
import com.oo2.grupo9.dtos.CrearUsuarioResponse;
import com.oo2.grupo9.dtos.TraerUsuarioResponse;
import com.oo2.grupo9.services.IEmailService;
import com.oo2.grupo9.services.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "2. Gestion de Usuario", description = "Endpoints para administrar usuarios.")
public class UsuarioRestController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IEmailService emailService;

    @Operation(
        summary = "Crear un nuevo usuario (Admin)",
        description = "Crea un nuevo usuario en el sistema. Requiere rol de Administrador.",
        security = @SecurityRequirement(name = "Bearer Authentication") 
    )
    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping
    public ResponseEntity<CrearUsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) throws Exception {  
        CrearUsuarioResponse nuevoUsuario = usuarioService.crearUsuarioRest(request);
        emailService.prepareAndSendWelcomeEmail(request.email(), request.nombreUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @Operation(
    summary = "Listar todos los usuarios (Admin)",
    description = "Devuelve una lista de todos los usuarios registrados en el sistema. Requiere rol de Administrador.",
    security = @SecurityRequirement(name = "Bearer Authentication")
)
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping
    public ResponseEntity<List<TraerUsuarioResponse>> listarTodosLosUsuarios() {
        List<TraerUsuarioResponse> usuarios = usuarioService.traerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}