package br.senai.sp.caio.guia_defilmes.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.senai.sp.caio.guia_defilmes.model.Administrador;

@Repository
public interface AdminRepository extends PagingAndSortingRepository<Administrador, Long>{
	public Administrador findByEmailAndSenha(String email, String senha);
}