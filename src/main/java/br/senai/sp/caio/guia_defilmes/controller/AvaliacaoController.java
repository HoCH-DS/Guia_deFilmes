package br.senai.sp.caio.guia_defilmes.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.caio.guia_defilmes.annotation.Publico;
import br.senai.sp.caio.guia_defilmes.model.Avaliacao;

import br.senai.sp.caio.guia_defilmes.repository.AvaliacaoRepository;

@RestController
@RequestMapping("/api/avaliacao")
public class AvaliacaoController {
	@Autowired
	private AvaliacaoRepository repository;
	

	@RequestMapping(value="", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Avaliacao> criarAvaliacao( @RequestBody Avaliacao avaliacao){
		repository.save(avaliacao);
		return ResponseEntity.created(URI.create("/avaliacao/"+avaliacao.getId())).body(avaliacao);
	}
	
	
	@Publico
	@RequestMapping(value = "/avaliacao/{id}", method =  RequestMethod.GET )
	public List<Avaliacao> AvaliacaoPorFilme (@PathVariable("id") Long id) {
		return repository.findByFilmeId(id);
	}
	
	
	@Publico
	@RequestMapping(value = "/{id}", method =  RequestMethod.GET)
	public Avaliacao getAvaliacao(@PathVariable ("id") Long id) {
		return repository.findById(id).get();
	}
}
