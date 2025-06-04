package com.oo2.grupo9.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ticket")
@Data
public class Ticket {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @ManyToMany
    @JoinTable(name = "ticket_categoria", joinColumns = @JoinColumn(name = "id_ticket"),
    	    inverseJoinColumns = @JoinColumn(name = "id_categoria"))
    private List<Categoria> lstCategorias;

    @ManyToOne
    @JoinColumn(name = "id_prioridad")
    private Prioridad prioridad;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    
    @Column(name = "fecha_cierre", nullable = true)
    private LocalDate fechaCierre;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
    
    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private Tipo tipo;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuarioCliente;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Intervencion> lstIntervenciones;
    

}
