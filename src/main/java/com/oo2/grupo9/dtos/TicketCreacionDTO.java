package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class TicketCreacionDTO {

    @NotBlank(message = "El título del ticket no puede estar vacío.")
    @Size(min = 6, max = 30, message = "El título debe tener entre 6 y 30 caracteres.")
    private String titulo;

    @NotBlank(message = "La descripción del ticket no puede estar vacía.")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres.") // Agregué un Size para la descripción
    private String descripcion;

    @NotEmpty(message = "Debe seleccionar al menos una categoría.")
    private List<Long> categoriasId;

    @NotNull(message = "Debe seleccionar un tipo de ticket.")
    private Long tipoId;
}
