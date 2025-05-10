package com.oo2.grupo9.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "localidad")
@Data
public class Localidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localidad")
    private Long idLocalidad;

    @Column(name = "nombre_localidad", nullable = false)
    private String nombreLocalidad;
}
