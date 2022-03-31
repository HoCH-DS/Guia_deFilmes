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


import br.senai.sp.caio.guia_defilmes.model.Categorias;
import br.senai.sp.caio.guia_defilmes.repository.CategoriasRepository;

@Controller
public class CategoriasController {
	
	//CHAMA O CategoriasRepository
	@Autowired
	private CategoriasRepository repository;
	
	
	//FORM PARA SALVAR CATEGORIAS
	@RequestMapping("formCategorias")
	public String formCategorias(Model model) {
		return "Categorias/Categ";
	}
	
	
	//SALVAR CATEGORIAS
	@RequestMapping(value = "salvarCategorias", method = RequestMethod.POST)
	public String salvarCategorias(@Valid Categorias categ, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			attr.addFlashAttribute("mensagem_erro", "Verifique os Campos...");
			return "redirect:Categorias";
		}
		
		try {
			repository.save(categ);
			attr.addFlashAttribute("mensagem_sucesso","Categoria Cadastrada com Sucesso ID:" + categ.getId());
			return "redirect:formCategorias";
		} catch (Exception e) {
			attr.addFlashAttribute("mensagem_erro", "houve um erro ao cadastrar a categoria" + e.getMessage());
		}
		
		return "redirect:formCategorias";
	}

	
	//LISTAR PAGINA
	@RequestMapping("listarCategorias/{page}")
	public String Listar(Model model, @PathVariable("page") int page) {
		
		//cria um pageble por pagina ordenando os objetos pelo nome  
		PageRequest pageble = PageRequest.of(page-1, 6, Sort.by(Sort.Direction.ASC, "nome"));
				
		//cria a pagina atual atraves so repository
		Page<Categorias> pagina = repository.findAll(pageble);  
				
		//descobrir quantidade total de paginas
		int totalPages = pagina.getTotalPages();
				
		//cria uma listade de inteiros para representar um pagina
		List<Integer> pageNumbers = new ArrayList<Integer>();
				
		//preence a lista com as paginas
		for(int i = 0 ; i < totalPages; i++) {
			pageNumbers.add(i + 1);
		}
		
		//ADICIMONA AS VARIAVEIS NA MODEL
		model.addAttribute("categs", pagina.getContent());
		model.addAttribute("PaginaAtual", page);
		model.addAttribute("totalPaginas", totalPages);
		model.addAttribute("numPaginas", pageNumbers);
		
		
		//RETORNA PARA A LISTA
		return "Categorias/ListarCateg";
	}
	
	
	//ALTERAR CATEGORIAS
	@RequestMapping("alterarCateg")
	public String alterarCateg(Model model , Long id) {	
		Categorias categ = repository.findById(id).get();
		model.addAttribute("categ", categ);		
		return "forward:formCategorias";
	}
	
		
	//EXCLUIR CATEGORIAS
	@RequestMapping("excluirCateg")
	public String excluirCateg (Long id) {
		repository.deleteById(id);
		return "redirect:listarCategorias/1";
	}
	
	//CHAMA O FORMULARIO PARA BUSCAR
	@RequestMapping("formBuscar")
	public String Buscar(String palavra_chave) {
		return "Categorias/BuscarCateg";
	}
	
	
	//BUSCAR CATEGORIAS PELA PALAVRA CHAVE
	@RequestMapping("BuscarCateg")
	public String buscarCateg(String select, String palavra_chave , Model model) {
		
		if(select.equals("palavra_chave")) {
			model.addAttribute("categs", repository.BuscarPalavraChaveLike(palavra_chave));
			return "Categorias/ListarCateg";
		}else if(select.equals("nome")) {
			model.addAttribute("categs", repository.BuscarNome(palavra_chave));
			return "Categorias/ListarCateg";
		}else {
			model.addAttribute("categs", repository.BuscarDescricao(palavra_chave));
			return "Categorias/ListarCateg";
		}
		
		
	}
}
