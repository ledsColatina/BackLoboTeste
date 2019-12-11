package com.LoboProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Setor;
import com.LoboProject.repository.SetorRepository;

@Service
public class SetorService {

	@Autowired
	private SetorRepository setorRepository;
	
	
	public Setor atualizar(Long id, Setor setor) {
		return setorRepository.findById(id)
		           .map(record -> {
		               record.setDescricao(setor.getDescricao());
		               record.setBase(setor.isBase());
		               Setor updated = setorRepository.save(record);
		               return updated;
		           }).orElse(null);
	}
}
