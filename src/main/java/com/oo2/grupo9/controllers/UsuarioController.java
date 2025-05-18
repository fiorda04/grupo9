package com.oo2.grupo9.controllers;

import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.entities.Localidad;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.ILocalidadService;
import com.oo2.grupo9.services.implementation.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;
    private ILocalidadService localidadService;

    public UsuarioController(UsuarioService usuarioService, ILocalidadService localidadService) {
        this.usuarioService = usuarioService;
        this.localidadService = localidadService;
    }

    @GetMapping("/inscribirse")
    public String mostrarFormularioInscripcion(Model model) {
        model.addAttribute("nuevoUsuario", new Usuario());
        model.addAttribute("localidades", localidadService.traerTodas());
        return ViewRouteHelper.USUARIO_INSCRIBIRSE;
    }

    @GetMapping
    @Operation(summary = "Lista todos los usuarios y localidades para la vista")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.traerTodos();
        List<Localidad> localidades = localidadService.traerTodas();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("nuevoUsuario", new Usuario());
        model.addAttribute("localidades", localidades);
        return ViewRouteHelper.USUARIO_LISTA;
    }

    @PostMapping("/agregar")
    public String agregarUsuario(@ModelAttribute("nuevoUsuario") Usuario usuario) {
        Long rolId = 1L;
        Long localidadId = usuario.getContacto().getLocalidad().getIdLocalidad();
        Localidad localidad = localidadService.traerPorId(localidadId).orElse(null);

        usuarioService.agregar(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getContacto().getEmail(),
                String.valueOf(usuario.getContacto().getTelefono()),
                usuario.getNombreUsuario(),
                usuario.getContraseña(),
                usuario.getContacto().getDomicilio(),
                localidad,
                rolId);

        return "redirect:" + ViewRouteHelper.ROUTE_INDEX;
    }

    @GetMapping("/login")
    public String login(Model model,
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "logout", required = false) String logout) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return ViewRouteHelper.USUARIO_LOGIN;
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "redirect:" + ViewRouteHelper.ROUTE_INDEX + "?logout";
    }

    @GetMapping("/loginsuccess")
    public String loginCheck() {
        return "redirect:" + ViewRouteHelper.ROUTE_INDEX;
    }

    @GetMapping("/admin/usuarios/modificar/{id}")
    public String mostrarFormularioModificarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.traer(id);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "admin/modificar-usuario";
        }
        return "redirect:/";
    }

}