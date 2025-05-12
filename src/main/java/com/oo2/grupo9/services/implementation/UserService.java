package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.repositories.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service("userService") // Renombramos el bean a "userService" para coincidir con la guía
public class UserService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + nombreUsuario));

        return buildUserDetails(usuario);
    }

    private UserDetails buildUserDetails(Usuario usuario) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol()));

        return new org.springframework.security.core.userdetails.User( // Usamos el nombre completo de la clase User
                usuario.getNombreUsuario(),
                usuario.getContraseña(),
                usuario.isActivo(),
                true, // accountNonExpired
                true, // accountNonLocked
                true, // credentialsNonExpired
                authorities
        );
    }
}