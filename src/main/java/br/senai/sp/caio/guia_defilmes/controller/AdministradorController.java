package br.senai.sp.caio.guia_defilmes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.caio.guia_defilmes.model.Administrador;
import br.senai.sp.caio.guia_defilmes.repository.AdminRepository;

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
		try {
			repository.save(admin);
			attr.addFlashAttribute("mensagem_sucesso","Admin Cadastrado com Sucesso ID:" + admin.getId());
			return "redirect:formAdmin";
		} catch (Exception e) {
			attr.addFlashAttribute("mensagem_erro", "houve um erro ao cadastrar o administrador" + e.getMessage());
		}
		return "redirect:formAdmin";
	}
}
