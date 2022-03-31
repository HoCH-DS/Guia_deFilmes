package br.senai.sp.caio.guia_defilmes.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.caio.guia_defilmes.model.Administrador;
import br.senai.sp.caio.guia_defilmes.repository.AdminRepository;
import br.senai.sp.caio.guia_defilmes.util.HashUtil;



@Controller
public class AdministradorController {
	@Autowired
	private AdminRepository repository; 
	
	@RequestMapping("formAdmin")
	public String formAdmin(Model model) {
		return "Administrador/Admin";
	}
	//Salvar Admin
	@RequestMapping(value = "salvarAdministrador", method = RequestMethod.POST)
	public String salvarAdmin(@Valid Administrador admin, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			attr.addFlashAttribute("mensagem_erro", "Verifique os Campos...");
			return "redirect:formAdmin";
		}
		
		// verifica se esta sendo feita uma alteração ao inves de uma inserção
		boolean alteracao = admin.getId() != null ? true : false;

		
		// verifica se a senha esta vazia
		if (admin.getSenha().equals(HashUtil.hash256(""))) {
		if (!alteracao) {
		// extrai a parte do email antes do @
		String parte = admin.getEmail().substring(0, admin.getEmail().indexOf("@"));


		// defina a senha dio admin
		admin.setSenha(parte);
		} else {
		//busca a senha atual
		String hash = repository.findById(admin.getId()).get().getSenha();
		//seta a senha com hash
		admin.setSenhaComHash(hash);
		}
		}
		
		try {
			repository.save(admin);
			attr.addFlashAttribute("mensagem_sucesso","Admin Cadastrado com Sucesso ID:" + admin.getId());
			return "redirect:formAdmin";
		} catch (Exception e) {
			attr.addFlashAttribute("mensagem_erro", "houve um erro ao cadastrar o administrador" + e.getMessage());
		}
		return "redirect:formAdmin";
	}
	//RequestMapping para listar a pagina 
	@RequestMapping("listarAdmin/{page}")
	//associando o parametro com a variavel 
	public String Listar(Model model, @PathVariable("page") int page) {
		//cria um pageble por pagina ordenando os objetos pelo nome  
		PageRequest pageble = PageRequest.of(page-1, 6, Sort.by(Sort.Direction.ASC, "nome"));
		//cria a pagina atual atraves so repository
		Page<Administrador> pagina = repository.findAll(pageble);  
		//descobrir quantidade total de paginas
		int totalPages = pagina.getTotalPages();
		//cria uma listade de inteiros para representar um pagina
		List<Integer> pageNumbers = new ArrayList<Integer>();
		//preence a lista com as paginas
		for(int i = 0 ; i < totalPages; i++) {
			pageNumbers.add(i + 1);
		}
		//adiciona as variaveis no Model
		model.addAttribute("admins", pagina.getContent());
		model.addAttribute("PaginaAtual", page);
		model.addAttribute("totalPaginas", totalPages);
		model.addAttribute("numPaginas", pageNumbers);
		//retorna para o HTML de lista
		return "Administrador/Lista";
	}
	//Alterar
	@RequestMapping("alterarAdmin")
	public String AlterarAdmin(Model model, Long id) {		
		Administrador admin = repository.findById(id).get();
		model.addAttribute("admin", admin);
		return "forward:formAdmin";
	}
	//Excluir
	@RequestMapping("excluirAdmin")
	public String ExcluirAdmin(Long id) {
		repository.deleteById(id);
		return "redirect:listarAdmin/1";		
	}
}
