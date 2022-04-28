package br.senai.sp.caio.guia_defilmes.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.senai.sp.caio.guia_defilmes.annotation.Publico;
@Component
public class AppInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//variavel para descobrir pra onde estao tentando ir
		String uri = request.getRequestURI();
		//mostrar uri
		System.out.println(uri);
		//verivica se o handler é um HandlerMethod, o que indica que foi encontrado um medoto no controller para a requisição
		if(handler instanceof HandlerMethod) {
			//libera o acesso a pagina incial
			if(uri.equals("/")) {
				return true;
			}
			if(uri.endsWith("/error")) {
				return true;
			}
			
			if(uri.startsWith("/api")) {
				return true;
			}else {
				// fazer um casting para o HandlerMethod
				HandlerMethod metodoChamado = (HandlerMethod) handler;
				//se o metodo é publico
				if(metodoChamado.getMethodAnnotation(Publico.class) != null) {
					return true;
				}
				//verifica se exite um usuario logado
				if(request.getSession().getAttribute("usuarioLogado") != null) {
					return true;
				}else {
					//redireciona para apagina incial
					response.sendRedirect("/");
					return false;
				}
			}
			
			
		}
		return true;
	}
}
