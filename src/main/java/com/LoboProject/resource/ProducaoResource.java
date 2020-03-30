package com.LoboProject.resource;

import java.util.List;
import java.util.Optional;
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
import com.LoboProject.domain.Produto;
import com.LoboProject.domain.Relatorios;
import com.LoboProject.repository.ProducaoRepository;
import com.LoboProject.repository.ProdutoRepository;
import com.LoboProject.service.ProducaoService;

@RestController
@RequestMapping("/producao")
public class ProducaoResource {

	@Autowired
	private ProducaoRepository producaoRepository;
	
	@Autowired
	private ProducaoService producaoService;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping("/{username}")
	public ResponseEntity<List<Producao>> listarProducao(@PathVariable String username){
		List<Producao> producao = producaoRepository.findAllByOrderByCodigoDesc();
		producao = producaoService.listarProducaoporUser(username);
		producao = producaoService.ordenarProducao(producao);
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/relatorioSetor")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public  ResponseEntity<List<?>> listarProducao30diasSetor(){
		List<Relatorios> producao = producaoService.agruparComUltimosDiasPorSetor();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/relatorioProdutos/{periodo}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public  ResponseEntity<List<?>> listarProducao30diasProdutoSetor(@PathVariable String periodo){
		List<Relatorios> producao = producaoService.agruparComUltimosDiasPorProdutoSetor(periodo);
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/top50")
	public ResponseEntity<List<Producao>> listar50Producoes(){
		List<Producao> producao = producaoRepository.findTop50ByOrderByCodigoDesc();
		return !producao.isEmpty() ? ResponseEntity.ok(producao) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/naoUsar")
	public ResponseEntity<String> buscarDemandas(){
		Producao prod = new Producao();
		int k = 1000;
		Optional<Produto> x = produtoRepository.findById("e");
		for(int i =0; i < 10000; i++) {
			prod.setCodigo((long)k);
			prod.setNome("aa");
			prod.setProduto(x.get());
			prod.setData(new java.util.Date(System.currentTimeMillis()));
			k++;
			producaoRepository.save(prod);
		}
		return ResponseEntity.ok().body("ok");
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
