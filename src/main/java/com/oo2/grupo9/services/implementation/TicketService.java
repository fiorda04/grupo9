package com.oo2.grupo9.services.implementation;

import org.springframework.stereotype.Service;

import com.oo2.grupo9.services.ITicketService;

import jakarta.transaction.Transactional;

@Service("ticketService")
@Transactional
public class TicketService implements ITicketService{

}
