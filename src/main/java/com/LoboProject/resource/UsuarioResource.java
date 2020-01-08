package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.UsuarioRepository;
import com.LoboProject.service.UsuarioService;


@RestController
@RequestMapping("/users")
public class UsuarioResource {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> listarUsuarios(){
		List<Usuario> user = usuarioRepository.findAll();
		return !user.isEmpty() ? ResponseEntity.ok(user) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{username}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> BuscarUsername(@PathVariable String username){
		Optional<Usuario> user = usuarioRepository.findByUsername(username);
		return user.isPresent() ? ResponseEntity.ok(user) : ResponseEntity.notFound().build() ;
	}

	
	@PostMapping()
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Usuario> CriarUsuario(@RequestBody Usuario user) {
		return usuarioService.CriarUsuario(user);
	}
	
	@DeleteMapping("/{username}")
	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarUsuario(@PathVariable String username){
		Optional<Usuario> usua = usuarioRepository.findByUsername(username);
		if(usua.isPresent() == true) {
			usua.get().setSetores(null);
			usua.get().setPermissoes(null);
			usuarioService.atualizarUsuario2(username, usua.get());
			usuarioRepository.deleteById(usua.get().getCodigo());
		}
	}
	
	@PutMapping("/{username}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String username, @RequestBody Usuario user){
		return usuarioService.atualizarUsuario(username, user);
	}
	
}
