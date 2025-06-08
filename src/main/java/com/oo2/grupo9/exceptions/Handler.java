package com.oo2.grupo9.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class Handler{
	
	@ExceptionHandler(TicketNoEncontradoException.class)
    public ModelAndView manejarTicketNoEncontrado(TicketNoEncontradoException ex) {
        ModelAndView mAV = new ModelAndView("error/ticket-no-encontrado");
        mAV.addObject("mensaje", ex.getMessage());
        return mAV;
    }
	
	@ExceptionHandler(TicketCerradoException.class)
    public String manejarTicketCerrado(TicketCerradoException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error/ticket-cerrado";
    }
	
	@ExceptionHandler(CampoBusquedaVacioException.class)
	public ModelAndView manejarCampoBusquedaVacio(CampoBusquedaVacioException ex) {
		ModelAndView mAV = new ModelAndView("error/selecciona-un-campo");
		mAV.addObject("mensaje",ex.getMessage());
		return mAV;
	}
}
