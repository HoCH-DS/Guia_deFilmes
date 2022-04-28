package br.senai.sp.caio.guia_defilmes.rest;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.caio.guia_defilmes.annotation.Publico;
import br.senai.sp.caio.guia_defilmes.model.Categorias;
import br.senai.sp.caio.guia_defilmes.repository.CategoriasRepository;



@RequestMapping("/api/tipo")
@RestController
public class TipoRestController {

	@Autowired
	private CategoriasRepository tiporepository;

	@Publico
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Categorias> getTipo() {
		return tiporepository.findAll();
	}

	@Publico
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categorias> findHQ(@PathVariable("id") Long id) {
		// busca o quadrinho
		Optional<Categorias> tipo = tiporepository.findById(id);
		if (tipo.isPresent()) {
			return ResponseEntity.ok(tipo.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
