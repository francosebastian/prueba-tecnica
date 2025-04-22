package cl.francosebastian.pruebatecnica.controller;


import cl.francosebastian.pruebatecnica.dto.CreateUserDTO;
import cl.francosebastian.pruebatecnica.dto.UserDTO;
import cl.francosebastian.pruebatecnica.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para manejar las solicitudes relacionadas con los usuarios del sistema.
 */
@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService usuarioService;

    /**
     *
     * @return
     */
    @GetMapping
    public List<UserDTO> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     *
     * @param usuario
     * @return
     */
    @PostMapping("/register")
    public UserDTO createUsuario(@RequestBody CreateUserDTO usuario) {
        return usuarioService.createUsuario(usuario);
    }

    /**
     *
     * @param id
     * @param usuario
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUsuario(@PathVariable Long id, @RequestBody UserDTO usuario) {
        return usuarioService.updateUsuario(id, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (usuarioService.deleteUsuario(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}