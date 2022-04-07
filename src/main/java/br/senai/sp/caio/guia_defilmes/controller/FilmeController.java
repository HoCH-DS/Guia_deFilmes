package br.senai.sp.caio.guia_defilmes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.caio.guia_defilmes.model.Filme;
import br.senai.sp.caio.guia_defilmes.repository.CategoriasRepository;
import br.senai.sp.caio.guia_defilmes.repository.FilmeRepository;


@Controller
public class FilmeController {
	@Autowired
	private CategoriasRepository filTipo;
	
	@Autowired
	private FilmeRepository filRep;

	@RequestMapping("Filme")
	public String form(Model model) {
		model.addAttribute("tipos", filTipo.findAllByOrderByNomeAsc());
		return "Filme/formFilme";
	}

	//SALVAR FILMES
		@RequestMapping(value = "salvarFilmes")
		public String salvarFilmes(@Valid Filme film, BindingResult result,
				RedirectAttributes attr, @RequestParam("fileFotos") MultipartFile[] fileFotos) {
			System.out.println(fileFotos.length); 
			
			if(result.hasErrors()) {
				attr.addFlashAttribute("mensagem_erro", "Verifique os Campos...");
				return "redirect:Filme";
			}
			
			try {
				filRep.save(film);
				attr.addFlashAttribute("mensagem_sucesso","Filme Cadastrado com Sucesso ID:" + film.getId());
				return "redirect:Filme";
			} catch (Exception e) {
				attr.addFlashAttribute("mensagem_erro", "houve um erro ao cadastraro filme" + e.getMessage());
			}
			
			return "redirect:Filme";
		}

	

}
