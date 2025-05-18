package com.oo2.grupo9.services.implementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oo2.grupo9.entities.Contacto;
import com.oo2.grupo9.entities.Localidad; // ¡Importante!
import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.repositories.ContactoRepository;
import com.oo2.grupo9.repositories.RolRepository;
import com.oo2.grupo9.repositories.UsuarioRepository;
import com.oo2.grupo9.services.IUsuarioService;

import jakarta.transaction.Transactional;

@Service("usuarioService")
@Transactional
public class UsuarioService implements IUsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private ContactoRepository contactoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            ContactoRepository contactoRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.contactoRepository = contactoRepository;

    }

    @Override
    public long agregar(String nombre, String apellido, int dni, String email, String telefono, String nombreUsuario,
            String contrasenia, String domicilio, Localidad localidad, Long rolId) {

        Usuario nuevoUsuario = new Usuario();

        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setDni(dni);
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        String encodedPassword = passwordEncoder.encode(contrasenia);
        nuevoUsuario.setContraseña(encodedPassword); // <--- ¡Línea corregida!
        nuevoUsuario.setActivo(true);

        Optional<Rol> rolOptional = rolRepository.findById(rolId);
        if (rolOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el rol con ID: " + rolId);
        }
        nuevoUsuario.setRol(rolOptional.get());

        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setEmail(email);
        try {
            nuevoContacto.setTelefono(Integer.parseInt(telefono));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El telefono debe ser un numero valido.");
        }
        nuevoContacto.setLocalidad(localidad);
        nuevoContacto.setDomicilio(domicilio);

        contactoRepository.save(nuevoContacto);
        nuevoUsuario.setContacto(nuevoContacto);

        return usuarioRepository.save(nuevoUsuario).getIdUsuario();
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
        usuarioExistente.setContraseña(usuario.getContraseña());
        usuarioExistente.setRol(usuario.getRol()); // Asumimos que el Rol ya viene con el ID correcto

        Contacto contactoExistente = usuarioExistente.getContacto();
        if (contactoExistente == null) {
            contactoExistente = new Contacto();
        }
        contactoExistente.setEmail(usuario.getContacto().getEmail());
        contactoExistente.setTelefono(usuario.getContacto().getTelefono());
        contactoExistente.setDomicilio(usuario.getContacto().getDomicilio());
        if (usuario.getContacto().getLocalidad() != null) {
            contactoExistente.setLocalidad(usuario.getContacto().getLocalidad()); // Asumimos que la Localidad ya viene
                                                                                  // con el objeto correcto
        }
        contactoRepository.save(contactoExistente);
        usuarioExistente.setContacto(contactoExistente);

        usuarioRepository.save(usuarioExistente);
    }
    
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
}