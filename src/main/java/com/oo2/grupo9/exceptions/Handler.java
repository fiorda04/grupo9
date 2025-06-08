package com.oo2.grupo9.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.ui.Model;

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

}
