package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Permissao;
import com.LoboProject.domain.Setor;
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.PermissaoRepository;
import com.LoboProject.repository.SetorRepository;
import com.LoboProject.repository.UsuarioRepository;
import com.LoboProject.security.GeradorSenhas;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Autowired
	private SetorRepository setorRepository;
	
	private GeradorSenhas gerador;
	
	public ResponseEntity<Usuario> atualizarUsuario(String username, Usuario user){
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
	
	
	public ResponseEntity<Usuario> CriarUsuario(Usuario user) {
		String username = user.getUsername().toLowerCase();
		if(usuarioRepository.findByUsername(username).isPresent() != false) return ResponseEntity.status(HttpStatus.CONFLICT).build();
		gerador = new GeradorSenhas();
		user.setUsername(username);
		String senha = gerador.gerar(user.getSenha());
		user.setSenha(senha);
		Permissao perm = new Permissao();
		List<Permissao> listperm = new ArrayList<Permissao>();
		Permissao permitirUser = new Permissao();
		Optional<Setor> setor;
		
		for(int i = 0; i < user.getSetores().size(); i++) {
			setor = setorRepository.findById(user.getSetores().get(i).getId());
			if(setor != null) {
				
				perm = permissaoRepository.findByDescricao(setor.get().getDescricao());
				if(perm == null) {
					//permitirUser.setCodigo((long) i);
					permitirUser.setDescricao(user.getSetores().get(i).getDescricao());
					//permissaoRepository.save(permitirUser);
					listperm.add(perm);
				}else {
					listperm.add(perm);
				}
			}
		}
		if(user.isTipo() ==  true) {
			permitirUser.setCodigo((long)1);
			permitirUser.setDescricao("ADMIN");
			listperm.add(permitirUser);
		}
		permitirUser.setCodigo((long) 2);
		permitirUser.setDescricao("USER");
		listperm.add(permitirUser);
		Usuario userSalvo = usuarioRepository.save(user);
		
		return ResponseEntity.status(HttpStatus.OK).body(userSalvo);
	}
}	
