package com.oo2.grupo9.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.implementation.UsuarioService;

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

    @PostMapping("/{id}")
    public String darDeBajaUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        System.out.println("ID RECIBIDO EN DAR DE BAJA: " + id);
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario dado de baja exitosamente.");
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("¡ERROR AL DAR DE BAJA USUARIO!");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al dar de baja al usuario: " + e.getMessage());
            return "redirect:/";
        }
        // redirectAttributes.addFlashAttribute("mensaje", "Prueba de redirección exitosa.");
        // return "redirect:/";
    }
}