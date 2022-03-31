package br.senai.sp.caio.guia_defilmes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import br.senai.sp.caio.guia_defilmes.util.HashUtil;
import lombok.Data;

//para gerar o set e o get
@Data
@Entity
public class Administrador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	@NotEmpty
	@Column(unique = true)
	private String email;
	@NotEmpty
	private String senha;
	
	//metodo para setar a senha aplicando o hash
	public void setSenha(String senha) {
		//aplica o hash e seta a senha no admin
		this.senha = HashUtil.hash256(senha);
	}
	
	// metodo para setar a senha sem fazer hash
	public void setSenhaComHash(String hash) {
	//"seta" o hash na senha
	this.senha = hash;
	}

} 