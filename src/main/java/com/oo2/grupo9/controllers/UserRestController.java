package com.oo2.grupo9.controllers;


import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.services.implementation.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UserRestController {

    private final UsuarioService usuarioService;

    public UserRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarUsuarioApi(@RequestBody Usuario nuevoUsuario) {
        try {
            Long rolId = 1L; // Asume un rol por defecto para la API
            usuarioService.agregar(
                    nuevoUsuario.getNombre(),
                    nuevoUsuario.getApellido(),
                    nuevoUsuario.getDni(),
                    nuevoUsuario.getContacto().getEmail(),
                    String.valueOf(nuevoUsuario.getContacto().getTelefono()),
                    nuevoUsuario.getNombreUsuario(),
                    nuevoUsuario.getContraseña(),
                    nuevoUsuario.getContacto().getDomicilio(),
                    nuevoUsuario.getContacto().getLocalidad(), // Asegúrate de que la Localidad esté presente en el request
                    rolId
            );
            return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}