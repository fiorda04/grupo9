package com.oo2.grupo9.dtos;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntervencionDTO {
	private Long ticketId;
	private String contenido;
	private LocalDate fechaIntervencion;
	private Long autorId;
}
