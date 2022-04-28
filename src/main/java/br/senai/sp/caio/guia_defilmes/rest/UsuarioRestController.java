package br.senai.sp.caio.guia_defilmes.rest;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.caio.guia_defilmes.annotation.Privado;
import br.senai.sp.caio.guia_defilmes.model.Erro;
import br.senai.sp.caio.guia_defilmes.model.Usuario;
import br.senai.sp.caio.guia_defilmes.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository repository;

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<Object> criarUsuario(Usuario usuario) {
		try {
			// salvar o usuario no banco de dados
			repository.save(usuario);
			// retorna codigo 201, com a URL para acesso no location
			return ResponseEntity.created(URI.create("/" + usuario.getId())).body(usuario);
		} catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			e.printStackTrace();
			Erro erro = new Erro();
			erro.setStatuscode(500);
			erro.setMensagem("Deu erro KRALIOO");
			erro.setException(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			Erro erro = new Erro();
			erro.setStatuscode(500);
			erro.setMensagem("Erro :" + e.getMessage());
			erro.setException(e.getClass().getName());
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarUsuario(@RequestBody Usuario usuario, @PathVariable("id") Long id) {
		// valida id
		if (id != usuario.getId()) {
			throw new RuntimeException("ID Invalido");
		}
		// salva o usuário
		repository.save(usuario);
		// criar um cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/usuario"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> findUsuario(@PathVariable("id") Long id) {
		System.out.println("PASSOU AQUI");
		// Busca os Usuários cadastrados
		Optional<Usuario> usuario = repository.findById(id);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@Privado
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirUsEntity(Long id){
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}
}