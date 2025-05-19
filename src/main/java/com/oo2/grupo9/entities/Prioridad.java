package com.oo2.grupo9.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prioridad")
@Data
@NoArgsConstructor
public class Prioridad {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prioridad")
	private Long idPrioridad;
	
	@Column(name = "nivel_prioridad", nullable = false)
    private int nivelPrioridad;
	
	@Column(name = "nombre", nullable = false)
    private String nombrePrioridad;

}
