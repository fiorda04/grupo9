package com.oo2.grupo9.services.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.oo2.grupo9.dtos.ContactoDTO;
import com.oo2.grupo9.dtos.UsuarioDTO;
import com.oo2.grupo9.entities.Contacto;
import com.oo2.grupo9.entities.Localidad;
import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.repositories.ContactoRepository;
import com.oo2.grupo9.repositories.RolRepository;
import com.oo2.grupo9.repositories.UsuarioRepository;
import com.oo2.grupo9.repositories.LocalidadRepository;
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
            throw new Exception("El nombre de usuario '" + usuarioDto.getNombreUsuario() + "' ya existe.");
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
        // Validaciones de unicidad para el admin
        if (usuarioRepository.findByNombreUsuario(usuarioDto.getNombreUsuario()).isPresent()) {
            throw new Exception("El nombre de usuario '" + usuarioDto.getNombreUsuario() + "' ya existe.");
        }
        // CAMBIO AQUÍ: findByDni ahora usa int
        if (usuarioRepository.findByDni(usuarioDto.getDni()).isPresent()) {
            throw new Exception("El DNI '" + usuarioDto.getDni() + "' ya está registrado.");
        }
        if (usuarioRepository.findByContacto_Email(contactoDto.getEmail()).isPresent()) {
            throw new Exception("El email '" + contactoDto.getEmail() + "' ya está registrado.");
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
    public void modificar(Usuario usuario) {
        Optional<Usuario> usuarioExistenteOptional = usuarioRepository.findById(usuario.getIdUsuario());
        if (usuarioExistenteOptional.isEmpty()) {
            throw new IllegalArgumentException("No existe un usuario con el ID proporcionado.");
        }
        Usuario usuarioExistente = usuarioExistenteOptional.get();

        if (usuarioRepository
                .findByNombreUsuarioAndIdUsuarioNotAndActivoTrue(usuario.getNombreUsuario(), usuario.getIdUsuario())
                .isPresent()) {
            throw new IllegalArgumentException("El nuevo nombre de usuario ya pertenece a otro usuario activo.");
        }
        if (usuarioRepository
                .findByDniAndContacto_Usuario_IdUsuarioNotAndActivoTrue(usuario.getDni(), usuario.getIdUsuario())
                .isPresent()) {
            throw new IllegalArgumentException("El nuevo DNI ya pertenece a otro usuario activo.");
        }
        if (usuarioRepository.findByContacto_EmailAndContacto_Usuario_IdUsuarioNotAndActivoTrue(
                usuario.getContacto().getEmail(), usuario.getIdUsuario()).isPresent()) {
            throw new IllegalArgumentException("El nuevo email ya pertenece a otro usuario activo.");
        }

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setDni(usuario.getDni());
        usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
        usuarioExistente.setContrasenia(usuario.getContrasenia());
        usuarioExistente.setRol(usuario.getRol());

        Contacto contactoExistente = usuarioExistente.getContacto();
        if (contactoExistente == null) {
            contactoExistente = new Contacto();
        }
        contactoExistente.setEmail(usuario.getContacto().getEmail());
        contactoExistente.setTelefono(usuario.getContacto().getTelefono());
        contactoExistente.setDomicilio(usuario.getContacto().getDomicilio());
        if (usuario.getContacto().getLocalidad() != null) {
            contactoExistente.setLocalidad(usuario.getContacto().getLocalidad());
        }
        contactoRepository.save(contactoExistente);
        usuarioExistente.setContacto(contactoExistente);

        usuarioRepository.save(usuarioExistente);
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
    
}