package com.LoboProject.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.LoboProject.domain.Composicao;
import com.LoboProject.repository.ComposicaoRepository;

@Service
public class ComposicaoService {

	@Autowired
	private ComposicaoRepository composicaoRepository;
	
	@Transactional
	public List<Composicao> criarComposicao(List<Composicao> composicao) {
		int i = 0;
		List <Composicao>composicaoSalvo = new ArrayList<Composicao>();
		
		while(composicao.get(i) != null) {
			composicaoSalvo.add(composicao.get(i));
			i++;
		}
		composicaoRepository.saveAll(composicaoSalvo);
		return composicaoSalvo;
	}
	
	public ResponseEntity<Composicao> atualizarComposicao(Long codigo, Composicao composicao){
		return composicaoRepository.findById(codigo)
		           .map(record -> {
		               record.setProdutoParte(composicao.getProdutoParte());
		               record.setQuantidade(composicao.getQuantidade());
		               Composicao updated = composicaoRepository.save(record);
		               return ResponseEntity.ok().body(updated);
		           }).orElse(ResponseEntity.notFound().build());
	}
	
}
