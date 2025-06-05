package com.oo2.grupo9.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "estado")
@Data
public class Estado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
	private Long idEstado;
	
	@Column(name = "nombre", nullable = false)
    private String nombreEstado;
	
	public static final Long ID_ESTADO_CERRADO = 6L;
}
