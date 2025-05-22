package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class PrioridadDTO {
	@Min(1)
	@Max(5)
	private int nivelPrioridad;
	private String nombrePrioridad;
}
