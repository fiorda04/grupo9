package com.oo2.grupo9.controllers;

import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.dtos.ContactoDTO;
import com.oo2.grupo9.dtos.UsuarioDTO;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.ILocalidadService;
import com.oo2.grupo9.services.implementation.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


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
    public ModelAndView mostrarFormularioInscripcion() {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_INSCRIBIRSE);
        mAV.addObject("nuevoUsuario", new UsuarioDTO()); // Pasa un objeto DTO vacío para el formulario
        mAV.addObject("nuevoContacto", new ContactoDTO()); // Pasa un objeto DTO de Contacto vacío
        mAV.addObject("localidades", localidadService.traerTodas()); // Carga las localidades para el desplegable
        return mAV;
    }

    @PostMapping("/agregar")
    public ModelAndView agregarUsuario(
            @ModelAttribute("nuevoUsuario") @Valid UsuarioDTO usuarioDto,
            BindingResult usuarioBindingResult, // Captura errores de validación para UsuarioDTO
            @ModelAttribute("nuevoContacto") @Valid ContactoDTO contactoDto,
            BindingResult contactoBindingResult // Captura errores de validación para ContactoDTO
    ) {
        // 1. Verificar si hay errores de validación en cualquiera de los DTOs
        if (usuarioBindingResult.hasErrors() || contactoBindingResult.hasErrors()) {
            // Si hay errores, volvemos al formulario de inscripción
            ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_INSCRIBIRSE);
            // Volvemos a pasar los DTOs con los datos que el usuario ya ingresó (incluyendo los errores)
            mAV.addObject("nuevoUsuario", usuarioDto);
            mAV.addObject("nuevoContacto", contactoDto);
            mAV.addObject("localidades", localidadService.traerTodas()); // Recargar localidades
            mAV.addObject("error", "Por favor, corrige los errores en el formulario."); // Mensaje general de error
            return mAV;
        }
        // 2. Si no hay errores de validación, intentar agregar el usuario
        try {
            // Delegamos al servicio, que ahora espera DTOs y maneja el mapeo y la encriptación
            usuarioService.agregarDesdeDTO(usuarioDto, contactoDto);
            // Si todo fue bien, redirigimos al índice (patrón POST-redirect-GET)
            ModelAndView mAV = new ModelAndView(new RedirectView(ViewRouteHelper.ROUTE_INDEX, true));
            // Los mensajes flash se añaden al ModelAndView para que RedirectView los envíe como flash attributes
            mAV.addObject("success", "Usuario registrado exitosamente!");
            return mAV;

        } catch (Exception e) {
            // 3. Si ocurre una excepción de negocio (ej. nombre de usuario duplicado)
            ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_INSCRIBIRSE); // Volvemos al formulario
            mAV.addObject("nuevoUsuario", usuarioDto);
            mAV.addObject("nuevoContacto", contactoDto);
            mAV.addObject("localidades", localidadService.traerTodas());
            mAV.addObject("error", "Error al registrar el usuario: " + e.getMessage()); // Muestra el mensaje de la excepción
            return mAV;
        }
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(name = "error", required = false) String error,
                              @RequestParam(name = "logout", required = false) String logout) {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_LOGIN);
        mAV.addObject("error", error); 
        mAV.addObject("logout", logout);
        return mAV;
    }

    @GetMapping("/logout")
    public RedirectView logout() {
        return new RedirectView(ViewRouteHelper.ROUTE_INDEX + "?logout", true);
    }

    @GetMapping("/loginsuccess")
    public String loginCheck() {
        return "redirect:" + ViewRouteHelper.ROUTE_INDEX;
    }

    @GetMapping("/admin/usuarios/modificar/{id}")
    public ModelAndView mostrarFormularioModificarUsuario(@PathVariable Long id) {
        ModelAndView mAV = new ModelAndView();
        Usuario usuario = usuarioService.traer(id); // Obtiene la entidad Usuario

        if (usuario != null) {
            // Aquí, para un formulario de modificación, lo ideal sería mapear la entidad
            // Usuario a un UsuarioDTO de EDICIÓN (que puede ser diferente al de registro)
            // y pasar ese DTO a la vista. Por simplicidad, por ahora pasamos la entidad.
            // UsuarioDTO usuarioParaModificar = modelMapper.map(usuario, UsuarioDTO.class);
            mAV.addObject("usuario", usuario); // Pasa la entidad al modelo para que la vista la use
            mAV.addObject("localidades", localidadService.traerTodas()); // Para el dropdown de localidades
            mAV.setViewName("admin/modificar-usuario"); // Define el nombre de la vista
        } else {
            // Si el usuario no se encuentra, redirigimos o mostramos un error
            mAV.setViewName(ViewRouteHelper.ROUTE_INDEX); // Redirigir al inicio o a una página de error
            mAV.addObject("error", "Usuario no encontrado para modificar.");
        }
        return mAV;
    }

}