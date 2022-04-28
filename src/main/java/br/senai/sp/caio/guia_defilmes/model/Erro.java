package br.senai.sp.caio.guia_defilmes.model;

import lombok.Data;

@Data
public class Erro {
	private int statuscode;
	private String mensagem;
	private String exception;
}