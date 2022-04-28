package br.senai.sp.caio.guia_defilmes.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
@Data
@Entity
public class Filme {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	@Column(columnDefinition = "TEXT")
	private String fotos;
	@ManyToOne
	private Categorias tipo;
	private String classIndicativa;
	private int tempoFilme;
	@OneToMany(mappedBy = "filme")
	private List<Avaliacao> avaliacoes;
	
	
	//metodo para criar um Vetor de fotos
	public String[] verFotos() {
		return this.fotos.split(";");
	}
}
