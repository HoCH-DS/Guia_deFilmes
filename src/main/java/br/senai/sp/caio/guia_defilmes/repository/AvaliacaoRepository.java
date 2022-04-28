package br.senai.sp.caio.guia_defilmes.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.senai.sp.caio.guia_defilmes.model.Avaliacao;

@Repository
public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long> {
	public List<Avaliacao> findByFilmeId(Long id);
}
