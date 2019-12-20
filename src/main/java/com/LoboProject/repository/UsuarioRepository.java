package com.LoboProject.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.LoboProject.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public Optional<Usuario> findByUsername(String username);
	
	public void deleteByUsername(String username);

	
	
}
