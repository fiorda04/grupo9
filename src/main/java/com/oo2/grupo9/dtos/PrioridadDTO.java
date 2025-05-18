package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PrioridadDTO {
	@Min(1)
	@Max(5)
	private int nivelPrioridad;
	private String nombrePrioridad;
}
