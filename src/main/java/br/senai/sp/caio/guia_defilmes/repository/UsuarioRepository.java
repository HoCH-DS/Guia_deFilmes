package br.senai.sp.caio.guia_defilmes.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import br.senai.sp.caio.guia_defilmes.model.Usuario;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>{
	public Usuario findByEmailAndSenha(String email, String senha);
}
