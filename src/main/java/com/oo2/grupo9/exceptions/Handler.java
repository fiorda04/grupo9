package com.oo2.grupo9.exceptions;

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

}
