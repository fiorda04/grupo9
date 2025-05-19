package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrioridadDTO {
	@Min(1)
	@Max(4)
	private int nivelPrioridad;
	private String nombrePrioridad;
}
