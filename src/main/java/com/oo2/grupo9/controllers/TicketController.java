package com.oo2.grupo9.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.oo2.grupo9.helpers.ViewRouteHelper;

@Controller
public class TicketController {

    @GetMapping("/tickets/crear")
    public String mostrarFormularioCrearTicket() {
        return ViewRouteHelper.TICKET_CREAR;
    }
}