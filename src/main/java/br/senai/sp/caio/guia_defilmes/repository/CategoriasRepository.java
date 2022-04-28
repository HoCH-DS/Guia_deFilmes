package br.senai.sp.caio.guia_defilmes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senai.sp.caio.guia_defilmes.model.Categorias;

@Repository
public interface CategoriasRepository extends PagingAndSortingRepository<Categorias, Long>{
	
	//METODO PARA FAZER UMA BUSCA PELA PALAVRA CHAVE
	@Query("SELECT ctg FROM Categorias ctg WHERE ctg.palavra_chave LIKE %:p%")
	public List<Categorias> BuscarPalavraChaveLike(@Param("p") String palavra_chave);
	
	
	//METODO PARA FAZER UMA BUSCA PELO NOME
	@Query("SELECT ctg FROM Categorias ctg WHERE ctg.nome LIKE %:p%")
	public List<Categorias> BuscarNome(@Param("p") String palavra_chave);
		

	//METODO PARA FAZER UMA BUSCA PELA DESCRICAO
	@Query("SELECT ctg FROM Categorias ctg WHERE ctg.descricao LIKE %:p%")
	public List<Categorias> BuscarDescricao(@Param("p") String palavra_chave);
	
	
	public List<Categorias> findAllByOrderByNomeAsc();
}
