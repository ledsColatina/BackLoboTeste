package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Permissao;
import com.LoboProject.domain.Usuario;
import com.LoboProject.repository.PermissaoRepository;
import com.LoboProject.repository.UsuarioRepository;
import com.LoboProject.security.GeradorSenhas;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
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
		gerador = new GeradorSenhas();
		user.setUsername(username);
		String senha = gerador.gerar(user.getSenha());
		user.setSenha(senha);
		Permissao perm;
		List<Permissao> listperm = new ArrayList<Permissao>();
		
		for(int i = 0; i < user.getSetores().size(); i++) {
			if(user.getSetores().get(i).getDescricao() == "Montagem") {
				perm = permissaoRepository.findByDescricao(user.getSetores().get(i).getDescricao());
				if(perm == null) {
					permissaoRepository.save(perm);
					listperm.add(perm);
				}else {
					listperm.add(perm);
				}
			}
		}
		
		Usuario userSalvo = usuarioRepository.save(user);
		
		return ResponseEntity.status(HttpStatus.OK).body(userSalvo);
	}
}	
