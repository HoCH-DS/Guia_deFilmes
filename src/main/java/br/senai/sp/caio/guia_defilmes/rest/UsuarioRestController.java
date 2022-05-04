package br.senai.sp.caio.guia_defilmes.rest;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.senai.sp.caio.guia_defilmes.annotation.Privado;
import br.senai.sp.caio.guia_defilmes.annotation.Publico;
import br.senai.sp.caio.guia_defilmes.model.Erro;
import br.senai.sp.caio.guia_defilmes.model.TokenJWT;
import br.senai.sp.caio.guia_defilmes.model.Usuario;
import br.senai.sp.caio.guia_defilmes.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {
	
	//Constantes para gerar o token
	public static final String EMISSOR = "U4Z4R!0$";
	public static final String SECRET = "R&$TP4r@0T!0K&n4ç";

	@Autowired
	private UsuarioRepository repository;

	//METODO PARA criarUsuario
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
	
	//METODO PARA LOGIN / LOGAR
	@Publico
	@RequestMapping(value="/logar",method = RequestMethod.POST)
	public ResponseEntity<TokenJWT> 
		logar(@RequestBody Usuario usuario){
		
			//buscar o usuario no BD
			usuario = repository.findByEmailAndSenha(usuario.getEmail(),usuario.getSenha());
			
			//verifica se exite o usuario
			if(usuario != null) {
				//valoress adicionais para o Token
				Map<String, Object> payload = new HashMap<String, Object>();
				//Como Saber O Id eo Nome Do User
				payload.put("id_usuario", usuario.getId());
				payload.put("nome_usuario", usuario.getNome());
				//definir a Data de expiração
				Calendar expiracao = Calendar.getInstance();
				//Tempo de Expiracao do Token
				expiracao.add(Calendar.HOUR, 1);
				//algoritimo para assinar o token
				Algorithm algoritimo = Algorithm.HMAC256(SECRET);
				//gerar o token
				TokenJWT tokenJwt = new TokenJWT();
				tokenJwt.setToken
				(JWT.create().withPayload(payload).withIssuer(EMISSOR).
						withExpiresAt(expiracao.getTime()).sign(algoritimo));
				
				return ResponseEntity.ok(tokenJwt);	
			}else {
				return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
			}
		}
		
	
	//METODO PARA atualizarUsuario
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
	
	
	//METODO PARA findUsuario
	@Privado
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
	
	//METODO PARA excluirUsEntity
	@Privado
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirUsEntity(Long id){
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}
}
