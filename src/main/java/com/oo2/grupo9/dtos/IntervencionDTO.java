package com.oo2.grupo9.dtos;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class IntervencionDTO {
	private Long ticketId;
	private String contenido;
	private LocalDate fechaIntervencion;
	private Long autorId;
	private Long estadoId;
    private Long prioridadId;
}
