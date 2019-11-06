package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.LoboProject.security.GeradorSenhas;

@RestController
@RequestMapping("/users")
public class UsuarioResource {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private GeradorSenhas gerador;
	
	@GetMapping
	public ResponseEntity<?> listarUsuarios(){
		List<Usuario> user = usuarioRepository.findAll();
		return !user.isEmpty() ? ResponseEntity.ok(user) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<?> BuscarUsername(@PathVariable String username){
		Optional<Usuario> user = usuarioRepository.findByUsername(username);
		return user.isPresent() ? ResponseEntity.ok(user) : ResponseEntity.notFound().build() ;
	}
	
	@PostMapping()
	public ResponseEntity<Usuario> CriarUsuario(@Valid @RequestBody Usuario user, HttpServletResponse response) {
		String username = user.getUsername().toLowerCase();
		user.setUsername(username);
		String senha = gerador.gerar(user.getSenha());
		user.setSenha(senha);
		Usuario userSalvo = usuarioRepository.save(user);
		
		return ResponseEntity.status(HttpStatus.OK).body(userSalvo);
	}
	
	@DeleteMapping("/{username}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarUsuario(@PathVariable String username){
		usuarioRepository.deleteByUsername(username);
	}
	
	@PutMapping("/{username}")
	public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String username, @Valid @RequestBody Usuario user){
		return usuarioRepository.findByUsername(username)
		           .map(record -> {
		        	   record.setUsername(user.getUsername());
		        	   record.setNome(user.getNome());
		        	   record.setSenha(gerador.gerar(user.getSenha()));
		        	   record.setPermissoes(user.getPermissoes());
		        	   
		               Usuario updated = usuarioRepository.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
	}
	
}
