package com.LoboProject.resource;

import java.sql.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.LoboProject.domain.Producao;
import com.LoboProject.domain.Relatorios;
import com.LoboProject.repository.ProducaoRepository;
import com.LoboProject.repository.UsuarioRepository;
import com.LoboProject.service.ProducaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/producao")
public class ProducaoResource {

	@Autowired
	private ProducaoRepository producaoRepository;
	
	@Autowired
	private ProducaoService producaoService;
	
	@Autowired
	private UsuarioRepository userRepository;
	
	
	@GetMapping("/{username}")
	public Page<Producao> listarProducao(@PathVariable String username, Pageable pageable){
		String nome = userRepository.findByUsername(username).get().getNome();
		return producaoRepository.findByNomeOrderByCodigoDesc(nome, pageable);
	}
	
	@GetMapping("/relatorioSetor")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public  ResponseEntity<List<?>> listarProducao30diasSetor(){
		List<Relatorios> producao = producaoService.agruparComUltimosDiasPorSetor();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
/*	@GetMapping("/relatorioProdutos/{periodo}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public  ResponseEntity<List<?>> listarProducao30diasProdutoSetor(@PathVariable String periodo){
		List<Relatorios> producao = producaoService.agruparComUltimosDiasPorProdutoSetor(periodo);
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}*/
	
	@GetMapping("/relatorioProdutos/{periodo}/{periodo2}/{descricaoSetor}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public  ResponseEntity<List<?>> listarProducao30diasProdutoSetor(@PathVariable Date periodo, @PathVariable Date periodo2,@PathVariable String descricaoSetor){
		List<Relatorios> producao = producaoService.agruparComUltimosDiasPorProdutoSetor(periodo, periodo2, descricaoSetor);
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/top50")
	public ResponseEntity<List<Producao>> listar50Producoes(){
		List<Producao> producao = producaoRepository.findTop50ByOrderByCodigoDesc();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping
	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	public  ResponseEntity<?> criarProducao(@RequestBody Producao producao, HttpServletResponse response) {
		return producaoService.criarProducao(producao);
	}
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	public ResponseEntity<?> deletarProducao(@PathVariable Long id){
		return producaoService.deletarProducao(id);
	}

}
