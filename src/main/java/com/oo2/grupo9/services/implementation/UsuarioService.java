package com.oo2.grupo9.services.implementation;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oo2.grupo9.dtos.ContactoDTO;
import com.oo2.grupo9.dtos.CrearUsuarioRequest;
import com.oo2.grupo9.dtos.CrearUsuarioResponse;
import com.oo2.grupo9.dtos.TraerUsuarioResponse;
import com.oo2.grupo9.dtos.UsuarioDTO;
import com.oo2.grupo9.dtos.UsuarioModificacionDTO;
import com.oo2.grupo9.entities.Contacto;
import com.oo2.grupo9.entities.Localidad;
import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.exceptions.RolyLocalidadException;
import com.oo2.grupo9.exceptions.UsuarioYaExistenteException;
import com.oo2.grupo9.exceptions.UsuarioYaExistenteResException;
import com.oo2.grupo9.repositories.ContactoRepository;
import com.oo2.grupo9.repositories.LocalidadRepository;
import com.oo2.grupo9.repositories.RolRepository;
import com.oo2.grupo9.repositories.UsuarioRepository;
import com.oo2.grupo9.services.IUsuarioService;

import jakarta.transaction.Transactional;



@Service("usuarioService")
@Transactional
public class UsuarioService implements IUsuarioService {

    private PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ContactoRepository contactoRepository;
    private final LocalidadRepository localidadRepository;
    private final ModelMapper modelMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            ContactoRepository contactoRepository, LocalidadRepository localidadRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.contactoRepository = contactoRepository;
        this.localidadRepository = localidadRepository;
        this.passwordEncoder = passwordEncoder;
    

        // --- INICIALIZACIÓN Y CONFIGURACIÓN DE MODELMAPPER ---
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        this.modelMapper.createTypeMap(UsuarioDTO.class, Usuario.class)
                .addMappings(mapper -> {
                    mapper.skip(Usuario::setIdUsuario);
                    mapper.skip(Usuario::setContacto);
                    mapper.skip(Usuario::setRol);
                    
                });

        this.modelMapper.createTypeMap(ContactoDTO.class, Contacto.class)
                .addMappings(mapper -> {
                    mapper.skip(Contacto::setIdContacto);
                    mapper.skip(Contacto::setLocalidad);
                });
        // --- FIN CONFIGURACIÓN DE MODELMAPPER ---
    }

    @Override
    public Usuario agregarDesdeDTO(UsuarioDTO usuarioDto, ContactoDTO contactoDto) throws Exception {
        if (usuarioRepository.findByNombreUsuario(usuarioDto.getNombreUsuario()).isPresent()) {
            throw new UsuarioYaExistenteException("El nombre de usuario '" + usuarioDto.getNombreUsuario() + "' ya existe.");
        }
        if (usuarioRepository.findByDni(usuarioDto.getDni()).isPresent()) {
            throw new UsuarioYaExistenteException("El DNI '" + usuarioDto.getDni() + "' ya está registrado.");
        }
        if (usuarioRepository.findByContacto_Email(contactoDto.getEmail()).isPresent()) {
            throw new UsuarioYaExistenteException("El email '" + contactoDto.getEmail() + "' ya está registrado.");
        }
        Localidad localidad = localidadRepository.findById(contactoDto.getLocalidadId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Localidad no encontrada con ID: " + contactoDto.getLocalidadId()));

        Contacto nuevoContacto = this.modelMapper.map(contactoDto, Contacto.class);
        nuevoContacto.setLocalidad(localidad);

        nuevoContacto = contactoRepository.save(nuevoContacto);

        Rol rolUsuario = rolRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Rol por defecto no encontrado (ID: 1)."));

        Usuario nuevoUsuario = this.modelMapper.map(usuarioDto, Usuario.class);
        nuevoUsuario.setContrasenia(passwordEncoder.encode(usuarioDto.getContrasenia()));
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setContacto(nuevoContacto);
        nuevoUsuario.setRol(rolUsuario);

        nuevoUsuario = usuarioRepository.save(nuevoUsuario);
       
        return nuevoUsuario;
    }

    @Override
    public Usuario agregarUsuarioPorAdmin(UsuarioDTO usuarioDto, ContactoDTO contactoDto) throws Exception {
        if (usuarioRepository.findByNombreUsuario(usuarioDto.getNombreUsuario()).isPresent()) {
             throw new UsuarioYaExistenteException("El nombre de usuario '" + usuarioDto.getNombreUsuario() + "' ya existe.");
        }
        if (usuarioRepository.findByDni(usuarioDto.getDni()).isPresent()) {
            throw new UsuarioYaExistenteException("El DNI '" + usuarioDto.getDni() + "' ya está registrado.");
        }
        if (usuarioRepository.findByContacto_Email(contactoDto.getEmail()).isPresent()) {
            throw new UsuarioYaExistenteException("El email '" + contactoDto.getEmail() + "' ya está registrado.");
        }

        Localidad localidad = localidadRepository.findById(contactoDto.getLocalidadId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Localidad no encontrada con ID: " + contactoDto.getLocalidadId()));

        Contacto nuevoContacto = this.modelMapper.map(contactoDto, Contacto.class);
        nuevoContacto.setLocalidad(localidad);
        nuevoContacto = contactoRepository.save(nuevoContacto);

        Rol rolSeleccionado = rolRepository.findById(usuarioDto.getRolId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Rol no encontrado con ID: " + usuarioDto.getRolId()));

        Usuario nuevoUsuario = this.modelMapper.map(usuarioDto, Usuario.class);
        nuevoUsuario.setContrasenia(passwordEncoder.encode(usuarioDto.getContrasenia()));
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setContacto(nuevoContacto);
        nuevoUsuario.setRol(rolSeleccionado);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());

        nuevoUsuario = usuarioRepository.save(nuevoUsuario);
        return nuevoUsuario;
    }

    @Override
    public void eliminar(Long id) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByIdUsuarioAndActivoTrue(id);
            if (usuarioOptional.isEmpty()) {
                throw new IllegalArgumentException("No existe un usuario activo con el ID proporcionado.");
            }
            Usuario usuario = usuarioOptional.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            System.err.println("¡ERROR EN EL SERVICIO AL ELIMINAR USUARIO!");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void eliminar(String nombreUsuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNombreUsuarioAndActivoTrue(nombreUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("No existe un usuario activo con el nombre de usuario proporcionado.");
        }
        Usuario usuario = usuarioOptional.get();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public Usuario traer(long idUsuario) {
        return usuarioRepository.findByIdUsuarioAndActivoTrue(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un usuario activo con el ID proporcionado."));
    }

    @Override
    public Usuario traerActivoEInactivo(long idUsuario) {
        return usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un usuario con el ID proporcionado."));
    }

    @Override
    public Usuario traer(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuarioAndActivoTrue(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un usuario activo con el nombre de usuario proporcionado."));
    }

    @Override
    public Usuario traerPorDni(int dni) {
        return usuarioRepository.findByDniAndActivoTrue(dni)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un usuario activo con el DNI proporcionado."));
    }

    @Override
    public Usuario traerPorEmail(String email) {
        return usuarioRepository.findByContacto_EmailAndActivoTrue(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un usuario activo con el email proporcionado."));
    }

    @Override
    public List<Usuario> traer() {
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    public List<Usuario> traerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> traerPorRol(long idRol) {
        Optional<Rol> rolOptional = rolRepository.findById(idRol);
        if (rolOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el rol con el ID proporcionado.");
        }
        return usuarioRepository.findByRol_IdRolAndActivoTrue(idRol);
    }

    @Override
    public boolean darDeAlta(long idUsuario) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                                         .orElseThrow(() -> new Exception("Usuario no encontrado con ID: " + idUsuario));
        if (!usuario.isActivo()) { 
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
            return true;
        }
        return false; 
    }
    

    public UsuarioModificacionDTO obtenerUsuarioParaModificar(long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return null;
        }
        Contacto contacto = usuario.getContacto(); 
        UsuarioModificacionDTO dto = new UsuarioModificacionDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setDni(usuario.getDni());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        if (usuario.getRol() != null) { 
            dto.setRolId(usuario.getRol().getIdRol());
        }
        if (contacto != null) {
            dto.setEmail(contacto.getEmail());
            dto.setTelefono(contacto.getTelefono());
            dto.setDomicilio(contacto.getDomicilio());
            if (contacto.getLocalidad() != null) { 
                dto.setLocalidadId(contacto.getLocalidad().getIdLocalidad());
            }
        } else {
        
        }
        return dto;
    }

    @Override
    public void actualizarUsuarioAdmin(UsuarioModificacionDTO usuarioModDto) throws Exception{
        Long idUsuario = usuarioModDto.getIdUsuario();
        if (idUsuario == null) {
            throw new Exception("El ID del usuario es necesario para la modificación.");
        }
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario).orElseThrow(() -> new Exception("Usuario no encontrado con ID: " + idUsuario));
        if (usuarioRepository.findByNombreUsuarioAndIdUsuarioNot(usuarioModDto.getNombreUsuario(), idUsuario).isPresent()) {
            throw new UsuarioYaExistenteException("El nombre de usuario '" + usuarioModDto.getNombreUsuario() + "' ya pertenece a otro usuario.");
        }
        if (usuarioModDto.getDni() != null && usuarioModDto.getDni() != 0 && usuarioRepository.findByDniAndIdUsuarioNot(usuarioModDto.getDni(), idUsuario).isPresent()) {
            throw new UsuarioYaExistenteException("El DNI '" + usuarioModDto.getDni() + "' ya pertenece a otro usuario.");
        }
        if (StringUtils.hasText(usuarioModDto.getEmail()) && usuarioRepository.findByEmailAndIdUsuarioNot(usuarioModDto.getEmail(), idUsuario).isPresent()) {
            throw new UsuarioYaExistenteException("El email '" + usuarioModDto.getEmail() + "' ya pertenece a otro usuario.");
        }
        usuarioExistente.setNombre(usuarioModDto.getNombre());
        usuarioExistente.setApellido(usuarioModDto.getApellido());
        usuarioExistente.setDni(usuarioModDto.getDni());
        usuarioExistente.setNombreUsuario(usuarioModDto.getNombreUsuario());
        if (StringUtils.hasText(usuarioModDto.getContrasenia())) {
            usuarioExistente.setContrasenia(passwordEncoder.encode(usuarioModDto.getContrasenia()));
        }
        if (usuarioModDto.getRolId() != null) {
            Rol rol = rolRepository.findById(usuarioModDto.getRolId())
                    .orElseThrow(() -> new Exception("Rol no encontrado con ID: " + usuarioModDto.getRolId()));
            usuarioExistente.setRol(rol);
        }
        Contacto contactoExistente = usuarioExistente.getContacto();
        contactoExistente.setEmail(usuarioModDto.getEmail());
        contactoExistente.setTelefono(usuarioModDto.getTelefono());
        contactoExistente.setDomicilio(usuarioModDto.getDomicilio());
        if (usuarioModDto.getLocalidadId() != null) {
            Localidad localidad = localidadRepository.findById(usuarioModDto.getLocalidadId()).orElseThrow(() -> new Exception("Localidad no encontrada con ID: " + usuarioModDto.getLocalidadId()));
            contactoExistente.setLocalidad(localidad);
        }
        usuarioRepository.save(usuarioExistente);
    }
    
    @Override
    public List<Usuario> traerPorNombreUsuarioConteniendo(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return traerTodos(); 
        }
        return usuarioRepository.findByNombreUsuarioContainingIgnoreCase(nombreUsuario.trim());
    }

    @Override
    public List<Usuario> traerPorDniExacto(int dni) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByDni(dni); 
        if (usuarioOptional.isPresent()) {
            return List.of(usuarioOptional.get()); 
        } else {
            return Collections.emptyList(); 
        }
    }

    @Override
    public List<Usuario> traerPorEmailConteniendo(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Collections.emptyList(); 
        }
        return usuarioRepository.findByNombreUsuarioContainingIgnoreCase(email.trim());
    }

    //Crear usuario Rest
    @Override
    public CrearUsuarioResponse crearUsuarioRest(CrearUsuarioRequest request) {
        // Validaciones (se quedan igual)
        if (usuarioRepository.findByNombreUsuario(request.nombreUsuario()).isPresent()) {
            throw new UsuarioYaExistenteResException("El nombre de usuario '" + request.nombreUsuario() + "' ya existe.");
        }
        if (usuarioRepository.findByDni(request.dni()).isPresent()) {
            throw new UsuarioYaExistenteResException("El DNI '" + request.dni() + "' ya está registrado.");
        }
        if (usuarioRepository.findByContacto_Email(request.email()).isPresent()) {
            throw new UsuarioYaExistenteResException("El email '" + request.email() + "' ya está registrado.");
        }

        //entidades
        Rol rol = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new RolyLocalidadException("Rol no encontrado con ID: " + request.rolId()));
        
        Localidad localidad = localidadRepository.findById(request.localidadId())
                .orElseThrow(() -> new RolyLocalidadException("Localidad no encontrada con ID: " + request.localidadId()));

        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setEmail(request.email());
        nuevoContacto.setTelefono(request.telefono());
        nuevoContacto.setDomicilio(request.domicilio());
        nuevoContacto.setLocalidad(localidad);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.nombre());
        nuevoUsuario.setApellido(request.apellido());
        nuevoUsuario.setDni(request.dni());
        nuevoUsuario.setNombreUsuario(request.nombreUsuario());
        nuevoUsuario.setContrasenia(passwordEncoder.encode(request.contrasenia()));
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setContacto(nuevoContacto);
        nuevoContacto.setUsuario(nuevoUsuario);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        
        // 4. Mapear la entidad guardada al DTO de respuesta COMPLETO y devolverlo
        return new CrearUsuarioResponse(
            usuarioGuardado.getIdUsuario(),
            usuarioGuardado.getNombre(),
            usuarioGuardado.getApellido(),
            usuarioGuardado.getDni(),
            usuarioGuardado.getNombreUsuario(),
            usuarioGuardado.getRol().getNombreRol(),
            usuarioGuardado.isActivo(),
            usuarioGuardado.getFechaCreacion(),
            usuarioGuardado.getContacto().getEmail(),
            usuarioGuardado.getContacto().getTelefono(),
            usuarioGuardado.getContacto().getDomicilio(),
            usuarioGuardado.getContacto().getLocalidad().getNombreLocalidad() // Obtenemos el nombre de la localidad
        );
    }
    
    private TraerUsuarioResponse convertirADTO(Usuario usuario) {
        return new TraerUsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getContacto().getEmail(),  
                usuario.getUsername(),
                usuario.getRol().getNombreRol(),     
                usuario.isEnabled(),
                usuario.getFechaCreacion()
        );
    }

    @Override
    public List<TraerUsuarioResponse> traerTodosLosUsuarios() {
    return usuarioRepository.findAll()
            .stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
}