package com.oo2.grupo9.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set; 
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Contacto;
import com.oo2.grupo9.entities.Estado;
import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.entities.Localidad;
import com.oo2.grupo9.entities.Prioridad;
import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Tipo;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.repositories.CategoriaRepository;
import com.oo2.grupo9.repositories.EstadoRepository;
import com.oo2.grupo9.repositories.IntervencionRepository;
import com.oo2.grupo9.repositories.LocalidadRepository;
import com.oo2.grupo9.repositories.PrioridadRepository;
import com.oo2.grupo9.repositories.RolRepository;
import com.oo2.grupo9.repositories.TicketRepository;
import com.oo2.grupo9.repositories.TipoRepository;
import com.oo2.grupo9.repositories.UsuarioRepository;

@Component
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
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private IntervencionRepository intervencionRepository;

    // Sets para almacenar las "claves" de los elementos ya existentes en la BD.
    // Se inicializarán al principio del método run().
    private Set<String> existingTicketKeys;
    private Set<String> existingInterventionKeys;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner: Iniciando creacion de todos los datos base......");

        // --- Cargar claves existentes al inicio ---
        // Aquí es donde hacemos la consulta INICIAL a la base de datos
        // para saber qué tickets e intervenciones ya existen.
        System.out.println("Cargando claves de tickets existentes...");
        existingTicketKeys = ticketRepository.findAll().stream()
                .map(t -> generateTicketKey(t.getTitulo(), t.getDescripcion(), t.getUsuarioCliente()))
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println("Cargando claves de intervenciones existentes...");
        existingInterventionKeys = intervencionRepository.findAll().stream()
                .map(i -> generateInterventionKey(i.getContenido(), i.getAutor(), i.getTicket()))
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println("Claves existentes cargadas. Procediendo con la inicialización de datos.");


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
        Prioridad prioridadPendiente = createPrioridadIfNotExists(1L, 1, "Pendiente");
        Prioridad prioridadBajo = createPrioridadIfNotExists(2L, 2, "Bajo");
        Prioridad prioridadMedio = createPrioridadIfNotExists(3L, 3, "Medio");
        Prioridad prioridadAlta = createPrioridadIfNotExists(4L, 4, "Alta");

        // --- 4. Crear Estados ---
        Estado estadoAbierto = createEstadoIfNotExists("Abierto");
        Estado estadoDerivado1 = createEstadoIfNotExists("Derivado sector 1");
        Estado estadoDerivado2 = createEstadoIfNotExists("Derivado sector 2");
        Estado estadoDerivado3 = createEstadoIfNotExists("Derivado sector 3");
        Estado estadoDerivado4 = createEstadoIfNotExists("Derivado sector 4");
        Estado estadoCerrado = createEstadoIfNotExists("Cerrado");

        // --- 5. Crear Categorías ---
        Categoria categoria1 = createCategoriaIfNotExists("Categoria 1");
        Categoria categoria2 = createCategoriaIfNotExists("Categoria 2");
        Categoria categoria3 = createCategoriaIfNotExists("Categoria 3");
        Categoria categoria4 = createCategoriaIfNotExists("Categoria 4");
        Categoria categoria5 = createCategoriaIfNotExists("Categoria 5");
        Categoria categoria6 = createCategoriaIfNotExists("Categoria 6");

        // --- Crear Listas de Categorías ---
        List<Categoria> categoriasInternet = new ArrayList<>(Arrays.asList(categoria1, categoria2));
        List<Categoria> categoriasFacturacion = new ArrayList<>(Arrays.asList(categoria2, categoria5));
        List<Categoria> categoriasSoporteTecnico = new ArrayList<>(Arrays.asList(categoria1, categoria3, categoria4));
        List<Categoria> categoriasAtencionAlCliente = new ArrayList<>(Arrays.asList(categoria6));
        List<Categoria> categoriasGenerales = new ArrayList<>(Arrays.asList(categoria1, categoria2, categoria3, categoria4, categoria5, categoria6));

        // --- 6. Crear Tipos ---
        Tipo tipoReclamos = createTipoIfNotExists("Reclamos");
        Tipo tipoConsultas = createTipoIfNotExists("Consultas");
        Tipo tipoNotificaciones = createTipoIfNotExists("Notificaciones");

        // --- 7. Crear Usuarios (1 Admin, 3 Empleados, 5 Clientes = 9 Usuarios en total) ---
        createUsuarioIfNotExists("Admin", "Principal", 11111111, "admin", "admin123", rolAdmin, localidadLomas, "admin@example.com", 12345670, "Calle Falsa 123, Administración");
        Usuario empleado1 = createUsuarioIfNotExists("Juan", "Perez", 22222222, "empleado1", "empleado123", rolEmpleado, localidadLomas, "empleado1@example.com", 12345671, "Avenida Central 456, Soporte");
        Usuario empleado2 = createUsuarioIfNotExists("Mario", "Lopez", 33333333, "empleado2", "empleado123", rolEmpleado, localidadLomas, "empleado2@example.com", 12345672, "Calle Mitre 456, Oficina 1");
        Usuario empleado3 = createUsuarioIfNotExists("Laura", "Garcia", 44444444, "empleado3", "empleado123", rolEmpleado, localidadLomas, "empleado3@example.com", 12345673, "Bulevar Tech 789, Desarrollo");

        Usuario cliente1 = createUsuarioIfNotExists("Ana", "Gomez", 55555555, "cliente1", "cliente123", rolCliente, localidadLomas, "cliente1@example.com", 12345674, "Plaza Mayor 789, Cliente");
        Usuario cliente2 = createUsuarioIfNotExists("Luis", "Martínez", 66666666, "cliente2", "cliente123", rolCliente, localidadLomas, "cliente2@example.com", 12345675, "Calle 1, Cliente");
        Usuario cliente3 = createUsuarioIfNotExists("María", "Fernández", 77777777, "cliente3", "cliente123", rolCliente, localidadLomas, "cliente3@example.com", 12345676, "Calle 2, Cliente");
        Usuario cliente4 = createUsuarioIfNotExists("Carlos", "Ramírez", 88888888, "cliente4", "cliente123", rolCliente, localidadLomas, "cliente4@example.com", 12345677, "Calle 3, Cliente");
        Usuario cliente5 = createUsuarioIfNotExists("Lucía", "Torres", 99999999, "cliente5", "cliente123", rolCliente, localidadLomas, "cliente5@example.2025.com", 12345678, "Calle 4, Cliente");

        // --- 8. Crear Tickets (entre 2 y 3 por cada cliente) ---
        // Admin no puede tener tickets asignados. Todos los tickets son de clientes.

        // Tickets para cliente1
        Ticket c1_ticket1 = createTicketIfNotExists("Problema de conexión", "No tengo internet en mi casa.", LocalDateTime.now().minusHours(5), cliente1, prioridadAlta, estadoAbierto, categoriasInternet, tipoReclamos);
        Ticket c1_ticket2 = createTicketIfNotExists("Queja por atención", "Fui mal atendido en la sucursal.", LocalDateTime.now().minusHours(9), cliente1, prioridadMedio, estadoAbierto, categoriasAtencionAlCliente, tipoReclamos);
        Ticket c1_ticket3 = createTicketIfNotExists("Consulta nueva instalación", "Quiero saber requisitos para fibra óptica.", LocalDateTime.now().minusHours(1), cliente1, prioridadBajo, estadoAbierto, categoriasGenerales, tipoConsultas);

        // Tickets para cliente2
        Ticket c2_ticket1 = createTicketIfNotExists("Consulta de facturación", "Quiero saber el detalle de mi última factura.", LocalDateTime.now().minusDays(1).minusHours(3), cliente2, prioridadMedio, estadoDerivado1, categoriasFacturacion, tipoConsultas);
        Ticket c2_ticket2_closed = createTicketIfNotExists("Ticket ya cerrado", "Este ticket debería estar cerrado.", LocalDateTime.now().minusDays(10), cliente2, prioridadBajo, estadoCerrado, categoriasGenerales, tipoConsultas);
        if (c2_ticket2_closed != null) {
            c2_ticket2_closed.setFechaCierre(LocalDateTime.now().minusDays(8));
            ticketRepository.save(c2_ticket2_closed);
        }
        Ticket c2_ticket3 = createTicketIfNotExists("Incidencia de red intermitente", "Se cae la conexión varias veces al día.", LocalDateTime.now().minusHours(6), cliente2, prioridadAlta, estadoAbierto, categoriasInternet, tipoReclamos);

        // Tickets para cliente3
        Ticket c3_ticket1 = createTicketIfNotExists("Reclamo por servicio de cable", "El servicio de cable se interrumpe constantemente.", LocalDateTime.now().minusDays(2).minusHours(1), cliente3, prioridadAlta, estadoDerivado2, categoriasSoporteTecnico, tipoReclamos);
        Ticket c3_ticket2 = createTicketIfNotExists("Solicitud de información", "Necesito detalles sobre el nuevo servicio de fibra óptica.", LocalDateTime.now().minusDays(1).minusHours(4), cliente3, prioridadPendiente, estadoAbierto, categoriasGenerales, tipoConsultas);

        // Tickets para cliente4
        Ticket c4_ticket1 = createTicketIfNotExists("Problema con teléfono fijo", "Mi teléfono fijo no tiene tono.", LocalDateTime.now().minusHours(7), cliente4, prioridadAlta, estadoAbierto, categoriasSoporteTecnico, tipoReclamos);
        Ticket c4_ticket2 = createTicketIfNotExists("Problema con hardware", "La impresora que instalaron no funciona.", LocalDateTime.now().minusDays(3).minusHours(6), cliente4, prioridadAlta, estadoDerivado2, categoriasSoporteTecnico, tipoReclamos);
        createTicketIfNotExists("Consulta sobre garantía (sin intervención)", "Pregunta sobre garantía de un equipo.", LocalDateTime.now().minusDays(5).minusHours(2), cliente4, prioridadBajo, estadoAbierto, categoriasGenerales, tipoConsultas);

        // Tickets para cliente5
        Ticket c5_ticket1 = createTicketIfNotExists("Consulta sobre planes", "Quiero cambiar mi plan de internet.", LocalDateTime.now().minusDays(1).minusHours(8), cliente5, prioridadMedio, estadoAbierto, categoriasFacturacion, tipoConsultas);
        createTicketIfNotExists("Problema con velocidad de internet (sin intervención)", "La velocidad no es la contratada.", LocalDateTime.now().minusHours(4), cliente5, prioridadAlta, estadoAbierto, categoriasInternet, tipoReclamos);
        createTicketIfNotExists("Notificación de cambio de domicilio (sin intervención)", "Solicito actualizar mi dirección de servicio.", LocalDateTime.now().minusDays(2).minusHours(9), cliente5, prioridadBajo, estadoAbierto, categoriasAtencionAlCliente, tipoNotificaciones);


        // --- 9. Crear Intervenciones (solo empleados) ---

        // Intervenciones para Ticket c1_ticket1
        createIntervencionIfNotExists("Se ha escalado el caso al área técnica. Pendiente de revisión.", LocalDateTime.now().minusHours(1), empleado1, c1_ticket1);
        createIntervencionIfNotExists("El técnico se comunicará en breve con el cliente.", LocalDateTime.now(), empleado2, c1_ticket1);

        // Intervenciones para Ticket c1_ticket2
        createIntervencionIfNotExists("Registrada queja del cliente. Se derivará para seguimiento.", LocalDateTime.now().minusHours(7), empleado1, c1_ticket2);
        createIntervencionIfNotExists("Supervisor de atención al cliente contactará al cliente.", LocalDateTime.now().minusHours(6), empleado3, c1_ticket2);

        // Intervenciones para Ticket c1_ticket3
        createIntervencionIfNotExists("Información sobre requisitos de fibra óptica enviada.", LocalDateTime.now().minusHours(0), empleado2, c1_ticket3);


        // Intervenciones para Ticket c2_ticket1
        createIntervencionIfNotExists("El sector de facturación informa que la factura ya fue enviada por correo electrónico.", LocalDateTime.now().minusHours(5), empleado2, c2_ticket1);
        createIntervencionIfNotExists("Se notifica al cliente que revise su bandeja de entrada y spam.", LocalDateTime.now().minusHours(2), empleado3, c2_ticket1);

        // Intervención para el ticket cerrado c2_ticket2_closed
        if (c2_ticket2_closed != null) {
            createIntervencionIfNotExists("Intento de intervención en un ticket cerrado.", LocalDateTime.now(), empleado1, c2_ticket2_closed);
        }

        // Intervenciones para Ticket c2_ticket3
        createIntervencionIfNotExists("Diagnóstico inicial de intermitencia de red. Se detectaron fluctuaciones en la señal.", LocalDateTime.now().minusHours(4), empleado1, c2_ticket3);
        createIntervencionIfNotExists("Se programó visita técnica para revisión en domicilio y equipo del cliente.", LocalDateTime.now().minusHours(2), empleado2, c2_ticket3);


        // Intervenciones para Ticket c3_ticket1
        createIntervencionIfNotExists("Problema recurrente con el servicio de cable. Se deriva al área de infraestructura.", LocalDateTime.now().minusDays(2).plusHours(2), empleado1, c3_ticket1);
        createIntervencionIfNotExists("Infraestructura está verificando la señal en la zona del cliente.", LocalDateTime.now().minusDays(1).plusHours(4), empleado2, c3_ticket1);
        createIntervencionIfNotExists("Se programa visita técnica para revisión en domicilio.", LocalDateTime.now().minusHours(3), empleado3, c3_ticket1);

        // Intervenciones para Ticket c3_ticket2
        createIntervencionIfNotExists("Se ha enviado brochure digital con la información solicitada. Se marca para seguimiento.", LocalDateTime.now().minusHours(10), empleado3, c3_ticket2);


        // Intervenciones para Ticket c4_ticket1
        createIntervencionIfNotExists("Se ha iniciado un diagnóstico remoto del servicio de telefonía.", LocalDateTime.now().minusHours(3), empleado1, c4_ticket1);
        createIntervencionIfNotExists("El diagnóstico inicial sugiere un problema en la línea externa. Se requiere revisión en campo.", LocalDateTime.now().minusHours(1), empleado2, c4_ticket1);

        // Intervenciones para Ticket c4_ticket2
        createIntervencionIfNotExists("Reporte de cliente: la impresora instalada no imprime. Se deriva al sector de hardware (sector 3).", LocalDateTime.now().minusDays(3).plusHours(2), empleado1, c4_ticket2);
        if (c4_ticket2 != null) {
            c4_ticket2.setEstado(estadoDerivado3); // Simulando cambio de estado
            ticketRepository.save(c4_ticket2); // Guardar el cambio de estado
        }
        createIntervencionIfNotExists("El equipo técnico de hardware está revisando el dispositivo. Se encontraron problemas de drivers.", LocalDateTime.now().minusDays(2).plusHours(5), empleado2, c4_ticket2);
        if (c4_ticket2 != null) {
            c4_ticket2.setEstado(estadoDerivado4); // Simulando cambio de estado
            ticketRepository.save(c4_ticket2); // Guardar el cambio de estado
        }
        createIntervencionIfNotExists("Se requiere actualización de firmware y calibración del equipo. Derivado al sector de software (sector 4).", LocalDateTime.now().minusDays(1).plusHours(3), empleado3, c4_ticket2);
        createIntervencionIfNotExists("Actualización de firmware completada. Pruebas de funcionamiento en progreso.", LocalDateTime.now().minusHours(5), empleado1, c4_ticket2);
        createIntervencionIfNotExists("Impresora funcionando correctamente tras las actualizaciones. Pendiente de verificación final con cliente.", LocalDateTime.now().minusHours(1), empleado2, c4_ticket2);


        // Intervenciones para Ticket c5_ticket1
        createIntervencionIfNotExists("Se le ha enviado al cliente un email con la comparativa de los planes disponibles y sus beneficios.", LocalDateTime.now().minusHours(8), empleado1, c5_ticket1);
        createIntervencionIfNotExists("Se aguarda respuesta del cliente para proceder con el cambio de plan si así lo desea.", LocalDateTime.now().minusHours(2), empleado2, c5_ticket1);

        System.out.println("CommandLineRunner: Creación de todos los datos base COMPLETADA.");
    }

    // Métodos createRolIfNotExists, createLocalidadIfNotExists, createPrioridadIfNotExists,
    // createEstadoIfNotExists, createCategoriaIfNotExists, createTipoIfNotExists,
    // createUsuarioIfNotExists permanecen SIN CAMBIOS.

    private Rol createRolIfNotExists(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol)
                .orElseGet(() -> {
                    System.out.println("Creando Rol: " + nombreRol);
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombreRol(nombreRol);
                    return rolRepository.save(nuevoRol);
                });
    }

    private Localidad createLocalidadIfNotExists(String nombreLocalidad) {
        return localidadRepository.findByNombreLocalidadonly(nombreLocalidad)
                .orElseGet(() -> {
                    System.out.println("Creando Localidad: " + nombreLocalidad);
                    Localidad nuevaLocalidad = new Localidad();
                    nuevaLocalidad.setNombreLocalidad(nombreLocalidad);
                    return localidadRepository.save(nuevaLocalidad);
                });
    }

    private Prioridad createPrioridadIfNotExists(Long id, int nivel, String nombre) {
        return prioridadRepository.findById(id)
                .orElseGet(() -> {
                    System.out.println("Creando Prioridad: " + nombre + " con ID: " + id);
                    Prioridad p = new Prioridad();
                    p.setNivelPrioridad(nivel);
                    p.setNombrePrioridad(nombre);
                    return prioridadRepository.save(p);
                });
    }

    private Estado createEstadoIfNotExists(String nombre) {
        return estadoRepository.findByNombreEstado(nombre)
                .orElseGet(() -> {
                    System.out.println("Creando Estado: " + nombre);
                    Estado e = new Estado();
                    e.setNombreEstado(nombre);
                    return estadoRepository.save(e);
                });
    }

    private Categoria createCategoriaIfNotExists(String nombre) {
        return categoriaRepository.findByNombreCategoria(nombre)
                .orElseGet(() -> {
                    System.out.println("Creando Categoria: " + nombre);
                    Categoria c = new Categoria();
                    c.setNombreCategoria(nombre);
                    return categoriaRepository.save(c);
                });
    }

    private Tipo createTipoIfNotExists(String nombre) {
        return tipoRepository.findByNombreTipo(nombre)
                .orElseGet(() -> {
                    System.out.println("Creando Tipo: " + nombre);
                    Tipo t = new Tipo();
                    t.setNombreTipo(nombre);
                    return tipoRepository.save(t);
                });
    }

    private Usuario createUsuarioIfNotExists(String nombre, String apellido, int dni, String nombreUsuario,
                                             String pass, Rol rol, Localidad localidad, String email,
                                             int telefono, String domicilio) {
        Optional<Usuario> usuOp = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuOp.isEmpty()) {
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
            contacto.setTelefono(telefono);
            contacto.setDomicilio(domicilio);
            contacto.setLocalidad(localidad);
            contacto.setUsuario(usuario);

            usuario.setContacto(contacto);

            usuarioRepository.save(usuario);
            System.out.println("Usuario " + nombreUsuario + " creado.");
            return usuario;
        }
        return usuOp.get();
    }


    // Método auxiliar para generar la clave del ticket
    private String generateTicketKey(String titulo, String descripcion, Usuario cliente) {
        // Asegúrate de que los campos no sean nulos antes de concatenar para evitar NullPointerException
        String t = titulo != null ? titulo : "";
        String d = descripcion != null ? descripcion : "";
        Long cId = (cliente != null && cliente.getIdUsuario() != 0) ? cliente.getIdUsuario() : -1L; // Usar 0 o -1 si el ID puede ser 0 o no estar asignado aún
        return t + "|" + d + "|" + cId;
    }

    private Ticket createTicketIfNotExists(String titulo, String descripcion, LocalDateTime fechaCreacion,
                                           Usuario cliente, Prioridad prioridad, Estado estado,
                                           List<Categoria> categorias, Tipo tipo) {
        if (cliente.getRol().getNombreRol().equals("Admin")) {
            System.out.println("ERROR: El usuario '" + cliente.getNombreUsuario() + "' con rol Admin no puede ser cliente de un ticket.");
            return null;
        }

        String ticketKey = generateTicketKey(titulo, descripcion, cliente);

        // Ahora comprobamos contra el set de claves existentes cargadas desde la BD
        if (existingTicketKeys.contains(ticketKey)) {
            System.out.println("Ticket con clave '" + ticketKey + "' ya existe en la BD. No se creará duplicado.");
            // Si necesitas el objeto Ticket, deberías recuperarlo de la BD aquí,
            // pero eso implicaría una nueva consulta. Para la inicialización,
            // si ya existe, podemos simplemente no crearlo.
            return null; // O busca y devuelve el existente si es estrictamente necesario.
        }

        Ticket ticket = new Ticket();
        ticket.setTitulo(titulo);
        ticket.setDescripcion(descripcion);
        ticket.setFechaCreacion(fechaCreacion);
        ticket.setUsuarioCliente(cliente);
        ticket.setPrioridad(prioridad);
        ticket.setEstado(estado);
        ticket.setLstCategorias(categorias);
        ticket.setTipo(tipo);

        Ticket savedTicket = ticketRepository.save(ticket);
        System.out.println("Ticket '" + titulo + "' (ID: " + savedTicket.getIdTicket() + ") creado.");

        // Agregamos la clave del ticket recién creado a nuestro set para evitar duplicados en la misma ejecución.
        existingTicketKeys.add(ticketKey);
        return savedTicket;
    }

    // Método auxiliar para generar la clave de la intervención
    private String generateInterventionKey(String contenido, Usuario autor, Ticket ticket) {
        String c = contenido != null ? contenido : "";
        Long aId = (autor != null && autor.getIdUsuario() != 0) ? autor.getIdUsuario() : -1L;
        Long tId = (ticket != null && ticket.getIdTicket() != 0) ? ticket.getIdTicket() : -1L;
        return c + "|" + aId + "|" + tId;
    }

    private Intervencion createIntervencionIfNotExists(String contenido, LocalDateTime fecha, Usuario autor, Ticket ticket) {
        if (!autor.getRol().getNombreRol().equals("Empleado")) {
            System.out.println("No se puede agregar intervención: el usuario " + autor.getNombreUsuario() + " con rol " + autor.getRol().getNombreRol() + " no está autorizado para generar intervenciones.");
            return null;
        }
        if (ticket == null || (ticket.getEstado() != null && "Cerrado".equalsIgnoreCase(ticket.getEstado().getNombreEstado()))) {
            System.out.println("No se puede agregar intervención: el ticket es nulo o está cerrado.");
            return null;
        }

        String interventionKey = generateInterventionKey(contenido, autor, ticket);

        // Ahora comprobamos contra el set de claves existentes cargadas desde la BD
        if (existingInterventionKeys.contains(interventionKey)) {
            System.out.println("Intervención con clave '" + interventionKey + "' ya existe en la BD. No se creará duplicado.");
            return null; // Si existe, no creamos y devolvemos null.
        }

        Intervencion intervencion = new Intervencion();
        intervencion.setContenido(contenido);
        intervencion.setFechaIntervencion(fecha);
        intervencion.setAutor(autor);
        intervencion.setTicket(ticket);

        if (ticket.getLstIntervenciones() == null) {
            ticket.setLstIntervenciones(new ArrayList<>());
        }
        
        ticket.getLstIntervenciones().add(intervencion);
        Intervencion savedIntervention = intervencionRepository.save(intervencion);
        System.out.println("Intervención creada para el ticket " + ticket.getIdTicket() + " por " + autor.getNombreUsuario());

        // Agregamos la clave de la intervención recién creada a nuestro set.
        existingInterventionKeys.add(interventionKey);
        return savedIntervention;
    }
}