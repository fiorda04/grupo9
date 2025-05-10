package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ContactoDTO {
    @Email(message = "Debe ser un email valido")
    @NotEmpty(message = "El email no puede estar vacio")
    private String email;
    @NotEmpty(message = "El telefono no puede estar vacio")
    private int telefono;
    @NotEmpty(message = "El domicilio no puede estar vacio")
    private String domicilio;
    private Long localidadId;
}