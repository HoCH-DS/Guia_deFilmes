package br.senai.sp.caio.guia_defilmes.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.senai.sp.caio.guia_defilmes.model.Filme;

@Repository
public interface FilmeRepository extends PagingAndSortingRepository<Filme, Long> {

}
