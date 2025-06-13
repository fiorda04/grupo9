package com.oo2.grupo9.dtos;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearTicketRequest(

    @NotEmpty(message = "El título no puede estar vacío")
    @Size(min = 6, max = 30, message = "El título debe tener entre 6 y 30 caracteres.")
    String titulo,

    @NotEmpty(message = "La descripción no puede estar vacía")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres.")
    String descripcion,

    @NotNull(message = "Debe tener al menos una categoría.")
    List<Long> categoriasId,

    @NotNull(message = "El tipo no puede estar vacío.")
    Long tipoId

) {}
