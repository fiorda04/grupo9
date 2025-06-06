package com.oo2.grupo9.controllers;

import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.entities.Usuario; 
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.IIntervencionService;
import com.oo2.grupo9.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView; 

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/intervenciones")
public class IntervencionController {

    @Autowired
    private IIntervencionService intervencionService;

    @Autowired
    private IUsuarioService usuarioService;

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("")
    public ModelAndView panelIntervenciones() {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_INTERVENTION_PANEL); 
        mav.addObject("intervenciones", intervencionService.traerTodas());
        mav.addObject("empleados", usuarioService.traerPorRol(2L)); 
        return mav;
    }


    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/buscar/ticket")
    public ModelAndView buscarPorTicketId(@RequestParam("valor") Long ticketId) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_INTERVENTION_SEARCH_RESULTS); 
        
        List<Intervencion> resultados = intervencionService.traerPorTicketId(ticketId);
        mav.addObject("resultadosIntervenciones", resultados);
        mav.addObject("criterioBusqueda", "ID de Ticket: " + ticketId);
        
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/buscar/empleado")
    public ModelAndView buscarPorEmpleado(@RequestParam("valor") Long empleadoId) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_INTERVENTION_SEARCH_RESULTS);
        
        List<Intervencion> resultados = intervencionService.traerPorEmpleadoId(empleadoId);
        mav.addObject("resultadosIntervenciones", resultados);

        String nombreEmpleado = "ID " + empleadoId;
        try {
            Usuario empleado = usuarioService.traer(empleadoId);
            nombreEmpleado = empleado.getNombreUsuario();
        } catch (Exception e) {
            
        }
        
        mav.addObject("criterioBusqueda", "Empleado (Autor): " + nombreEmpleado);
        return mav;
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/buscar/contenido")
    public ModelAndView buscarPorContenido(@RequestParam("valor") String contenido) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_INTERVENTION_SEARCH_RESULTS);
        
        List<Intervencion> resultados = intervencionService.traerPorContenido(contenido);
        mav.addObject("resultadosIntervenciones", resultados);
        mav.addObject("criterioBusqueda", "Contenido (contiene): '" + contenido + "'");
        
        return mav;
    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/buscar/fechas")
    public ModelAndView buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_INTERVENTION_SEARCH_RESULTS);
        
        List<Intervencion> resultados = intervencionService.traerPorFechas(fechaDesde, fechaHasta);
        mav.addObject("resultadosIntervenciones", resultados);
        mav.addObject("criterioBusqueda", "Rango de Fechas: desde " + fechaDesde + " hasta " + fechaHasta);
        
        return mav;
    }
}