package br.senai.sp.caio.guia_defilmes.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.api.Http;

import br.senai.sp.caio.guia_defilmes.annotation.Privado;
import br.senai.sp.caio.guia_defilmes.annotation.Publico;
import br.senai.sp.caio.guia_defilmes.rest.UsuarioRestController;

@Component
public class AppInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// variavel para descobrir pra onde estao tentando ir
		String uri = request.getRequestURI();
		// mostrar uri
		System.out.println(uri);
		// verivica se o handler é um HandlerMethod, o que indica que foi encontrado um
		// medoto no controller para a requisição
		if (handler instanceof HandlerMethod) {
			// libera o acesso a pagina incial
			if (uri.equals("/")) {
				return true;
			}
			if (uri.endsWith("/error")) {
				return true;
			}

			// fazer o cating para handlerMethod
			HandlerMethod metodoChamado = (HandlerMethod) handler;
			if (uri.startsWith("/api")) {
				String token = null;
				if (metodoChamado.getMethodAnnotation(Privado.class) != null) {
					try {
						// obtem o token da request
						token = request.getHeader("Authorization");
						Algorithm algoritimo = Algorithm.HMAC256(UsuarioRestController.SECRET);
						JWTVerifier verifier = JWT.require(algoritimo).
								withIssuer(UsuarioRestController.EMISSOR).build();
						
						//token Valido
						DecodedJWT jwt = verifier.verify(token);
						
						//extrair os dados do payload
						Map<String, Claim> payload = jwt.getClaims();
						
						//Imprime a msg
						System.out.println(payload.get("nome_usuario"));
						return true;
						
					} catch (Exception e) {
						if(token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
							
						}else {
							response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
						}
						return false;
					}
				}
				return true;
				
			} else {	
				
				// se o metodo é publico
				if (metodoChamado.getMethodAnnotation(Publico.class) != null) {
					return true;
				}
				// verifica se exite um usuario logado
				if (request.getSession().getAttribute("usuarioLogado") != null) {
					return true;
				} else {
					// redireciona para apagina incial
					response.sendRedirect("/");
					return false;
				}
			}

		}
		return true;
	}
}
