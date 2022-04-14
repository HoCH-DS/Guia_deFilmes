package br.senai.sp.caio.guia_defilmes.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.caio.guia_defilmes.model.Administrador;

public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long>{
	public Administrador findByEmailAndSenha(String email, String senha);
}