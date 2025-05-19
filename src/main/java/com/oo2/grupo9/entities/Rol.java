package com.oo2.grupo9.entities;

import jakarta.persistence.*;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "rol")
@Data
public class Rol implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "nombre_rol", unique = true, nullable = false)
    private String nombreRol;

    @Override
    public String getAuthority() {
        return "ROLE_" + nombreRol;
    }
    
}