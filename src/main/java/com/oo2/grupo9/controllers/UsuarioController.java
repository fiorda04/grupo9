package com.oo2.grupo9.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.oo2.grupo9.dtos.ContactoDTO;
import com.oo2.grupo9.dtos.UsuarioDTO;
import com.oo2.grupo9.dtos.UsuarioModificacionDTO;
import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.IEmailService;
import com.oo2.grupo9.services.ILocalidadService;
import com.oo2.grupo9.services.IRolService;
import com.oo2.grupo9.services.IUsuarioService;
import com.oo2.grupo9.services.implementation.UsuarioService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final ILocalidadService localidadService;
    private final IRolService rolService;
    private final IEmailService emailService;

    public UsuarioController(UsuarioService usuarioService, ILocalidadService localidadService,  IRolService rolService, IEmailService emailService) {
        this.usuarioService = usuarioService;
        this.localidadService = localidadService;
        this.rolService = rolService;
        this.emailService = emailService;
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
            // Si todo fue bien, redirigimos al índice (patrón POST-redirect-GET)
            usuarioService.agregarDesdeDTO(usuarioDto, contactoDto);
            ModelAndView mAV = new ModelAndView(new RedirectView(ViewRouteHelper.ROUTE_INDEX, true));
            // Los mensajes flash se añaden al ModelAndView para que RedirectView los envíe como flash attributes
            mAV.addObject("success", "Usuario registrado exitosamente!");
            // Mandamos el email de que se registro con ese usuario y para que inicie sesion
            emailService.prepareAndSendWelcomeEmail(contactoDto.getEmail(), usuarioDto.getNombreUsuario());
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


    //MOSTRAR EL FORMULARIO DE CREACIÓN POR ADMINISTRADOR! 
    @PreAuthorize("hasRole('ROLE_Admin')") 
    @GetMapping("/admin/crear")
    public ModelAndView mostrarFormularioCreacionUsuarioAdmin() {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_ADMIN_CREAR); 
        mAV.addObject("nuevoUsuario", new UsuarioDTO());
        mAV.addObject("nuevoContacto", new ContactoDTO());
        mAV.addObject("localidades", localidadService.traerTodas());
        mAV.addObject("roles", rolService.traerTodos());
        return mAV;
    }

    // CREACIÓN DE USUARIOS POR ADMINISTRADOR! 
    @PreAuthorize("hasRole('ROLE_Admin')") 
    @PostMapping("/admin/agregar")
    public ModelAndView agregarUsuarioAdmin(
            @ModelAttribute("nuevoUsuario") @Valid UsuarioDTO usuarioDto,
            BindingResult usuarioBindingResult,
            @ModelAttribute("nuevoContacto") @Valid ContactoDTO contactoDto,
            BindingResult contactoBindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (usuarioBindingResult.hasErrors() || contactoBindingResult.hasErrors()) {
            ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_ADMIN_CREAR); // Vuelve a la vista admin
            mAV.addObject("nuevoUsuario", usuarioDto);
            mAV.addObject("nuevoContacto", contactoDto);
            mAV.addObject("localidades", localidadService.traerTodas());
            mAV.addObject("roles", rolService.traerTodos());
            mAV.addObject("error", "Por favor, corrige los errores en el formulario.");
            return mAV;
        }

        try {
           
            usuarioService.agregarUsuarioPorAdmin(usuarioDto, contactoDto);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario creado exitosamente por el administrador!");
            emailService.prepareAndSendWelcomeEmail(contactoDto.getEmail(), usuarioDto.getNombreUsuario());
            return new ModelAndView(new RedirectView(ViewRouteHelper.ROUTE_INDEX, true)); 

        } catch (Exception e) {
            ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_ADMIN_CREAR); // Vuelve a la vista admin
            mAV.addObject("nuevoUsuario", usuarioDto);
            mAV.addObject("nuevoContacto", contactoDto);
            mAV.addObject("localidades", localidadService.traerTodas());
            mAV.addObject("roles", rolService.traerTodos());
            mAV.addObject("errorMessage", "Error al crear el usuario (Admin): " + e.getMessage());
            return mAV;
        }
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/admin/modificar/{id}")
    public ModelAndView mostrarFormularioModificacionUsuarioAdmin(@PathVariable("id") long idUsuario,
            RedirectAttributes redirectAttributes) {
        ModelAndView mAV = new ModelAndView();
        try {
            UsuarioModificacionDTO usuarioModDto = usuarioService.obtenerUsuarioParaModificar(idUsuario);

            if (usuarioModDto == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado.");
                mAV.setViewName("redirect:" + ViewRouteHelper.ROUTE_INDEX); 
                return mAV;
            }
            mAV.setViewName(ViewRouteHelper.USUARIO_ADMIN_MODIFICAR); 
            mAV.addObject("usuarioMod", usuarioModDto); 
            mAV.addObject("localidades", localidadService.traerTodas());
            mAV.addObject("roles", rolService.traerTodos());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al cargar el usuario para modificar: " + e.getMessage());
            mAV.setViewName("redirect:" + ViewRouteHelper.ROUTE_INDEX); 
        }
        return mAV;
    }


    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/admin/guardarModificacion")
    public ModelAndView guardarModificacionUsuarioAdmin(
            @ModelAttribute("usuarioMod") @Valid UsuarioModificacionDTO usuarioModDto, 
            BindingResult bindingResult, 
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_ADMIN_MODIFICAR); 
            mAV.addObject("usuarioMod", usuarioModDto);
            mAV.addObject("localidades", localidadService.traerTodas());
            mAV.addObject("roles", rolService.traerTodos());
            mAV.addObject("errorMessage", "Error de validación. Por favor, corrige los campos marcados.");
            return mAV;
        }
        try {
            usuarioService.actualizarUsuarioAdmin(usuarioModDto);
            redirectAttributes.addFlashAttribute("successMessage", "¡Usuario modificado exitosamente!");
            return new ModelAndView(new RedirectView(ViewRouteHelper.ROUTE_INDEX, true)); 

        } catch (Exception e) {
            ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_ADMIN_MODIFICAR);
            mAV.addObject("usuarioMod", usuarioModDto); 
            mAV.addObject("localidades", localidadService.traerTodas());
            mAV.addObject("roles", rolService.traerTodos());
            mAV.addObject("errorMessage", "Error al modificar el usuario: " + e.getMessage());
            return mAV;
        }
    }


    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/admin/buscar/nombre") 
    public ModelAndView buscarUsuariosPorNombre(@RequestParam("valor") String nombreUsuario) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_USER_SEARCH_RESULTS);
        List<Usuario> resultados = usuarioService.traerPorNombreUsuarioConteniendo(nombreUsuario);
        mav.addObject("resultadosUsuarios", resultados);
        mav.addObject("criterioBusqueda", "Nombre de Usuario (contiene): '" + nombreUsuario + "'");
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/admin/buscar/dni")
    public ModelAndView buscarUsuariosPorDni(@RequestParam("valor") int dni) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_USER_SEARCH_RESULTS);
        List<Usuario> resultados = usuarioService.traerPorDniExacto(dni);
        mav.addObject("resultadosUsuarios", resultados);
        mav.addObject("criterioBusqueda", "DNI: " + dni);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/admin/buscar/email")
    public ModelAndView buscarUsuariosPorEmail(@RequestParam("valor") String email) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_USER_SEARCH_RESULTS);
        List<Usuario> resultados = usuarioService.traerPorEmailConteniendo(email);
        mav.addObject("resultadosUsuarios", resultados);
        mav.addObject("criterioBusqueda", "Email (contiene): '" + email + "'");
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/admin/buscar/rol")
    public ModelAndView buscarUsuariosPorRol(@RequestParam(name = "valor", required = false) Long rolId) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_USER_SEARCH_RESULTS);
        List<Usuario> resultados;
        String nombreRolBuscado = "Todos"; 
        if (rolId != null) {
            resultados = usuarioService.traerPorRol(rolId); 
            Optional<Rol> rolOpt = rolService.traerPorId(rolId);
            if (rolOpt.isPresent()) {
                nombreRolBuscado = rolOpt.get().getNombreRol();
            } else {
                 nombreRolBuscado = "ID de Rol " + rolId + " (desconocido)";
            }
        } else {
            resultados = Collections.emptyList(); 
            nombreRolBuscado = "Ningún rol seleccionado";
        }
        mav.addObject("resultadosUsuarios", resultados);
        mav.addObject("criterioBusqueda", "Rol: " + nombreRolBuscado);
        return mav;
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
        return new RedirectView(ViewRouteHelper.USUARIO_LOGIN + "?logout", true);
    }

    @GetMapping("/loginsuccess")
    public String loginCheck() {
        return "redirect:" + ViewRouteHelper.ROUTE_INDEX;
    }

}