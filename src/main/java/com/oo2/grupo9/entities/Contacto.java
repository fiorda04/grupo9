package com.oo2.grupo9.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contacto")
@Data
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto")
    private Long idContacto;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefono")
    private int telefono;

    @Column(name = "domicilio")
    private String domicilio;

    @ManyToOne
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;

    @OneToOne(mappedBy = "contacto")
    private Usuario usuario; 
}