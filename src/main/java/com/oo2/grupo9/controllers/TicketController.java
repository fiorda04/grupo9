package com.oo2.grupo9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo9.dtos.TicketDTO;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.ICategoriaService;
import com.oo2.grupo9.services.ITicketService;
import com.oo2.grupo9.services.ITipoService;

@Controller
public class TicketController {

	private ICategoriaService categoriaService;
	private ITipoService tipoService;
	private ITicketService ticketService;

	@GetMapping("/tickets/crear")
	public String mostrarFormularioCrearTicket() {
		return ViewRouteHelper.TICKET_CREAR;
	}

	@GetMapping("/CrearTicket")
	public ModelAndView crearTicket() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.CREAR_TICKET);
		mAV.addObject("nuevoTicket", new TicketDTO());
		mAV.addObject("categorias", categoriaService.traerTodas());
		mAV.addObject("tipos", tipoService.traerTodos());
		return mAV;
	}

	@PostMapping("/agregar")
	public String agregarTicket(@ModelAttribute("nuevoTicket") TicketDTO ticketDTO) {
		Long prioridadId = 1L;
		Long estadoId = 1L;

		ticketService.agregar(ticketDTO.getTitulo(),
				ticketDTO.getDescripcion(),
				ticketDTO.getCategoriasId(),
				ticketDTO.getTipoId(),
				estadoId, 
				prioridadId,
				ticketDTO.getUsuarioIdCliente());

		return "redirect:" + ViewRouteHelper.LISTA_TICKETS;
	}
	
	@GetMapping("{id}")
	public ModelAndView verTicket(@PathVariable("id") Long id) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.VER_TICKET);
	    Ticket ticket = ticketService.traer(id);
	    mAV.addObject("ticket", ticket);
	    return mAV;
	}
	
	
}