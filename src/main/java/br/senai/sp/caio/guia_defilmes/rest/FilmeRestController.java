package br.senai.sp.caio.guia_defilmes.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.caio.guia_defilmes.annotation.Publico;
import br.senai.sp.caio.guia_defilmes.model.Filme;
import br.senai.sp.caio.guia_defilmes.repository.FilmeRepository;

@RestController
@RequestMapping("/api/filme")
public class FilmeRestController {
	@Autowired
	private FilmeRepository repository; 

	@Publico
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Filme> getQuadrinho() {
		return repository.findAll();
	}

	@Publico
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Filme> findHQ(@PathVariable("id") Long id) {
		// busca o quadrinho
		Optional<Filme> quadrinho = repository.findById(id);
		if (quadrinho.isPresent()) {
			return ResponseEntity.ok(quadrinho.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@Publico
	@RequestMapping(value = "/tipo/{idTipo}", method = RequestMethod.GET)
	public List<Filme> getQuadrinhoByTipo(@PathVariable("idTipo") Long idTipo) {
		return repository.findByTipoId(idTipo);
	}

	@RequestMapping(value = "classificacaoIndicativa/{classIndicativa}")
	public List<Filme> getQuadrinhoByClassInd(@PathVariable("classIndicativa")String classInd){
		return repository.findByClassIndicativa(classInd);
	}
}

