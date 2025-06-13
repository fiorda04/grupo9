package com.oo2.grupo9.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo9.helpers.ViewRouteHelper;

@ControllerAdvice
public class Handler{
	
	@ExceptionHandler(TicketNoEncontradoException.class)
    public ModelAndView manejarTicketNoEncontrado(TicketNoEncontradoException ex) {
        ModelAndView mAV = new ModelAndView("error/vista-general-exception");
        mAV.addObject("mensaje", ex.getMessage());
        return mAV;
    }
	
	@ExceptionHandler(TicketCerradoException.class)
    public String manejarTicketCerrado(TicketCerradoException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error/ticket-cerrado";
    }
	
	@ExceptionHandler(RangoDeFechasInvalidoException.class)
    public ModelAndView manejarRangoDeFechasInvalido(RangoDeFechasInvalidoException ex) {
        ModelAndView mav = new ModelAndView("error/vista-general-exception");
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ModelAndView manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        ModelAndView mav = new ModelAndView("error/vista-general-exception");
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ModelAndView manejarEmpleadoNoEncontrado(EmpleadoNoEncontradoException ex) {
        ModelAndView mav = new ModelAndView("error/vista-general-exception");
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(CategoriaNoValidaException.class)
    public ModelAndView manejarCategoriaNoValida(CategoriaNoValidaException ex) {
        ModelAndView mav = new ModelAndView("error/vista-general-exception");
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(UsuarioYaExistenteException.class)
    public ModelAndView manejarUsuarioYaExistente(UsuarioYaExistenteException ex) {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_YA_EXISTENTE_ERROR); 
        mAV.addObject("mensaje", ex.getMessage()); 
        mAV.addObject("volverUrl", "/usuarios/inscribirse");
        mAV.addObject("volverUrlAdmin", "/usuarios/admin/crear"); 
        return mAV;
    }
	
	@ExceptionHandler(CampoBusquedaVacioException.class)
	public ModelAndView manejarCampoBusquedaVacio(CampoBusquedaVacioException ex) {
		ModelAndView mAV = new ModelAndView("error/selecciona-un-campo");
		mAV.addObject("mensaje",ex.getMessage());
		return mAV;
	}
}
