package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class ContactoDTO {
    @Email(message = "Debe ser un email valido")
    @NotEmpty(message = "El email no puede estar vacio")
    private String email;
    @NotNull(message = "El teléfono no puede estar vacío.")
    private int telefono;
    @NotEmpty(message = "El domicilio no puede estar vacio")
    private String domicilio;
    private Long localidadId;
}