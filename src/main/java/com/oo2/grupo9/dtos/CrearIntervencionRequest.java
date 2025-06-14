package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CrearIntervencionRequest(

    @NotNull(message = "Debe especificarse un ID de ticket")
    Long ticketId,

    @NotEmpty(message = "El contenido no puede estar vacío")
    String contenido,

    @NotNull(message = "Debe especificarse un ID de estado")
    Long estadoId,

    @NotNull(message = "Debe especificarse un ID de prioridad")
    Long prioridadId

) {}