package com.oo2.grupo9.controllers;

import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.implementation.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String mostrarPaginaInicio(Model model) {
        List<Usuario> todosUsuarios = usuarioService.traerTodos();
        model.addAttribute("usuarios", todosUsuarios);
        return ViewRouteHelper.INDEX;
    }
}