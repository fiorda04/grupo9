package com.oo2.grupo9.config;

import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Contacto;
import com.oo2.grupo9.entities.Estado;
import com.oo2.grupo9.entities.Localidad;
import com.oo2.grupo9.entities.Prioridad;
import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.entities.Tipo;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.repositories.CategoriaRepository;
import com.oo2.grupo9.repositories.EstadoRepository;
import com.oo2.grupo9.repositories.LocalidadRepository;
import com.oo2.grupo9.repositories.PrioridadRepository;
import com.oo2.grupo9.repositories.RolRepository;
import com.oo2.grupo9.repositories.TipoRepository;
import com.oo2.grupo9.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component // Para que Spring la detecte y ejecute
public class DataInitializer implements CommandLineRunner {

    @Autowired 
    private UsuarioRepository usuarioRepository;
    @Autowired 
    private RolRepository rolRepository;
    @Autowired 
    private LocalidadRepository localidadRepository;
    @Autowired 
    private PrioridadRepository prioridadRepository;
    @Autowired 
    private EstadoRepository estadoRepository;
    @Autowired 
    private CategoriaRepository categoriaRepository;
    @Autowired 
    private TipoRepository tipoRepository;
    @Autowired 
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner: Iniciando creacion de todos los datos base......");
        
        // --- 1. Crear Roles ---
        Rol rolCliente = createRolIfNotExists("Cliente");
        Rol rolEmpleado = createRolIfNotExists("Empleado");
        Rol rolAdmin = createRolIfNotExists("Admin");

        // --- 2. Crear Localidades ---
        createLocalidadIfNotExists("Glew");
        createLocalidadIfNotExists("Longchamp");
        createLocalidadIfNotExists("Burzaco");
        createLocalidadIfNotExists("Adrogue");
        createLocalidadIfNotExists("Temperley");
        Localidad localidadLomas = createLocalidadIfNotExists("Lomas"); 
        createLocalidadIfNotExists("Banfield");
        createLocalidadIfNotExists("R. de Escalada");
        createLocalidadIfNotExists("Lanus");
        createLocalidadIfNotExists("Avellaneda");
        
        // --- 3. Crear Prioridades ---
        createPrioridadIfNotExists(1L, 1, "Pendiente");
        createPrioridadIfNotExists(2L, 2, "Bajo");
        createPrioridadIfNotExists(3L, 3, "Medio");
        createPrioridadIfNotExists(4L, 4, "Alta");

        // --- 4. Crear Estados ---
        createEstadoIfNotExists("Abierto");
        createEstadoIfNotExists("Derivado sector 1");
        createEstadoIfNotExists("Derivado sector 2");
        createEstadoIfNotExists("Derivado sector 3");
        createEstadoIfNotExists("Derivado sector 4");
        createEstadoIfNotExists("Cerrado");

        // --- 5. Crear Categorías ---
        createCategoriaIfNotExists("Categoria 1");
        createCategoriaIfNotExists("Categoria 2");
        createCategoriaIfNotExists("Categoria 3");
        createCategoriaIfNotExists("Categoria 4");
        createCategoriaIfNotExists("Categoria 5");
        createCategoriaIfNotExists("Categoria 6");

        // --- 6. Crear Tipos ---
        createTipoIfNotExists("Reclamos");
        createTipoIfNotExists("Consultas");
        createTipoIfNotExists("Notificaciones");

        // --- 7. Crear Usuarios (usando los roles y localidadLomas creados/obtenidos arriba) ---
        createUsuarioIfNotExists("Admin", "Principal", 11111111, "admin", "admin123", rolAdmin, localidadLomas, "admin@example.com", 12345670, "Calle Falsa 123, Administración");
        createUsuarioIfNotExists("Juan", "Perez", 22222222, "empleado1", "empleado123", rolEmpleado, localidadLomas, "empleado1@example.com", 12345671, "Avenida Central 456, Soporte");
        createUsuarioIfNotExists("Ana", "Gomez", 33333333, "cliente1", "cliente123", rolCliente, localidadLomas, "cliente1@example.com", 12345672, "Plaza Mayor 789, Cliente");

        System.out.println("CommandLineRunner: Creación de todos los datos base COMPLETADA.");
    }

    private Rol createRolIfNotExists(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol)
                .orElseGet(() -> {
                    System.out.println("Creando Rol: " + nombreRol);
                    Rol nuevoRol = new Rol(); // Asume constructor vacío
                    nuevoRol.setNombreRol(nombreRol); // Asume setter
                    return rolRepository.save(nuevoRol);
                });
    }

    private Localidad createLocalidadIfNotExists(String nombreLocalidad) {
        return localidadRepository.findByNombreLocalidadonly(nombreLocalidad)
                .orElseGet(() -> {
                    System.out.println("Creando Localidad: " + nombreLocalidad);
                    Localidad nuevaLocalidad = new Localidad(); // Asume constructor vacío
                    nuevaLocalidad.setNombreLocalidad(nombreLocalidad); // Asume setter
                    return localidadRepository.save(nuevaLocalidad);
                });
    }

    private void createPrioridadIfNotExists(Long id, int nivel, String nombre) {
        prioridadRepository.findById(id) 
                .orElseGet(() -> {
                    System.out.println("Creando Prioridad: " + nombre + " con ID: " + id);
                    Prioridad p = new Prioridad();
                    p.setNivelPrioridad(nivel); 
                    p.setNombrePrioridad(nombre); 
                    return prioridadRepository.save(p);
                });
    }

    private void createEstadoIfNotExists(String nombre) {
        estadoRepository.findByNombreEstado(nombre)
                .orElseGet(() -> {
                    System.out.println("Creando Estado: " + nombre);
                    Estado e = new Estado();
                    e.setNombreEstado(nombre);
                    return estadoRepository.save(e);
                });
    }

    private void createCategoriaIfNotExists(String nombre) {
        categoriaRepository.findByNombreCategoria(nombre)
                .orElseGet(() -> {
                    System.out.println("Creando Categoria: " + nombre);
                    Categoria c = new Categoria();
                    c.setNombreCategoria(nombre);
                    return categoriaRepository.save(c);
                });
    }

    private void createTipoIfNotExists(String nombre) {
        tipoRepository.findByNombreTipo(nombre)
                .orElseGet(() -> {
                    System.out.println("Creando Tipo: " + nombre);
                    Tipo t = new Tipo();
                    t.setNombreTipo(nombre);
                    return tipoRepository.save(t);
                });
    }

    private void createUsuarioIfNotExists(String nombre, String apellido, int dni, String nombreUsuario,
            String pass, Rol rol, Localidad localidad, String email,
            int telefono, String domicilio) {
        if (usuarioRepository.findByNombreUsuario(nombreUsuario).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setContrasenia(passwordEncoder.encode(pass));
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setActivo(true);
            usuario.setRol(rol);

            Contacto contacto = new Contacto();
            contacto.setEmail(email);
            contacto.setTelefono(telefono); // Asegúrate que el tipo en Contacto.setTelefono sea int o Integer
            contacto.setDomicilio(domicilio);
            contacto.setLocalidad(localidad);

            usuario.setContacto(contacto);
            if (contacto.getUsuario() == null) { // Para la relación bidireccional
                contacto.setUsuario(usuario);
            }

            usuarioRepository.save(usuario);
            System.out.println("Usuario " + nombreUsuario + " creado.");
        }
    }
}