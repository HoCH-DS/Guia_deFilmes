package br.senai.sp.caio.guia_defilmes.controller;

import java.io.IOException;
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

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import br.senai.sp.caio.guia_defilmes.model.Filme;
import br.senai.sp.caio.guia_defilmes.repository.CategoriasRepository;
import br.senai.sp.caio.guia_defilmes.repository.FilmeRepository;
import br.senai.sp.caio.guia_defilmes.util.FirebaseUtil;


@Controller
public class FilmeController {
	@Autowired
	private CategoriasRepository filTipo;
	
	@Autowired
	private FilmeRepository filRep;
	
	@Autowired
	private FirebaseUtil firebaseUtil;

	@RequestMapping("Filme")
	public String form(Model model) {
		model.addAttribute("tipos", filTipo.findAllByOrderByNomeAsc());
		return "Filme/formFilme";
	}	
	
	//SALVAR FILMES
		@RequestMapping(value = "salvarFilmes")
		public String salvarFilmes(@Valid Filme film, BindingResult result,
				RedirectAttributes attr, @RequestParam("fileFotos") MultipartFile[] fileFotos) {
			//String para a URL das fotos
			String fotos = film.getFotos();
			
			//percore cada arquivo que foi submetido no formulario
			for (MultipartFile arquivo : fileFotos) {
				//verificar se o arquivo seta vazio
				if(arquivo.getOriginalFilename().isEmpty()) {
					//vai para o próximo arquivo
					continue;
				}
				//faz o upload para a nuvem e obtém a url gerada
				try {
					fotos += firebaseUtil.uploadFile(arquivo)+";";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//atribui a String fotos ao objeto Filmes
			film.setFotos(fotos);
			filRep.save(film);
			
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
		
		//LISTAR PAGINA
		@RequestMapping("listarFilmes/{page}")
		public String Listar(Model model, @PathVariable("page") int page) {
			
			//cria um pageble por pagina ordenando os objetos pelo nome  
			PageRequest pageble = PageRequest.of(page-1, 6, Sort.by(Sort.Direction.ASC, "nome"));
					
			//cria a pagina atual atraves so repository
			Page<Filme> pagina =  filRep.findAll(pageble);  
					
			//descobrir quantidade total de paginas
			int totalPages = pagina.getTotalPages();
					
			//cria uma listade de inteiros para representar um pagina
			List<Integer> pageNumbers = new ArrayList<Integer>();
					
			//preence a lista com as paginas
			for(int i = 0 ; i < totalPages; i++) {
				pageNumbers.add(i + 1);
			}
			
			//ADICIMONA AS VARIAVEIS NA MODEL
			model.addAttribute("filmes", pagina.getContent());
			model.addAttribute("PaginaAtual", page);
			model.addAttribute("totalPaginas", totalPages);
			model.addAttribute("numPaginas", pageNumbers);
			
			
			//RETORNA PARA A LISTA
			return "Filme/ListaFilmes";
		}
		
			
		//ALTERAR FILMES
		@RequestMapping("alterarFilm")
		public String alterarFilm(Model model , Long id) {	
			Filme film = filRep.findById(id).get();
			model.addAttribute("film", film);		
			return "forward:Filme";
		}
		
		//EXCLUIR FIMES
		@RequestMapping("excluirFilm")
		public String excluirFilm(Long id) {
			Filme film = filRep.findById(id).get();
			if(film.getFotos().length() > 0 ) {
				for (String foto : film.verFotos()) {
					firebaseUtil.deletar(foto);
				}
			}
			filRep.delete(film);
			return "redirect:listarFilmes/1";
		}
		
		//METODO PARA EXCLUIR FOTO
		@RequestMapping("excluirFotoFilm")
		public String excluirFoto(Long id, int numFoto, Model model) {
			//busca o Filme no Banco de dados
			Filme film = filRep.findById(id).get();
			//pegar a String da foto a der Excluida
			String fotoUrl = film.verFotos()[numFoto];
			//excluir do fireBase
			firebaseUtil.deletar(fotoUrl);
			//retira a foto da String Fotos
			film.setFotos(film.getFotos().replace(fotoUrl+";", ""));
			//salvar no BD o objeto Film
			model.addAttribute("filme", film);
			//ecaminhar para o form	
			return "forward:/Filme";
			
		}
		
}
