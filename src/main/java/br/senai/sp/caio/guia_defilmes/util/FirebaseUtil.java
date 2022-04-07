package br.senai.sp.caio.guia_defilmes.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class FirebaseUtil {
	// variavel para guardar as credencias do Firebase
	private Credentials credentiais;
	// variavel para acesar o storage
	private Storage storage;
	// constante pra o nome do bucket
	private final String BUCKET_NAME = "guiafilmes-d910f.appspot.com";
	// constante para o prefixo
	private String PREFIX = "https://firebasestorage.googleapis.com/v0/b/"+"BUCKET_NAME"+"/o/";
	// constante para o sufixo da URL
	private final String SUFFIX = "?alt=media";
	// constante para a URL
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;
	
	
	public FirebaseUtil() {
		// buscar as credencias (aquivo JSON)
		Resource resource = new ClassPathResource("chaveFireBase.json");		
		try {
			// ler  o arquivo para obter as credemciais
			credentiais = GoogleCredentials.fromStream(resource.getInputStream());
			// acessa o serviço de storage
			storage = StorageOptions.newBuilder().setCredentials(credentiais).build().getService();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String uploadFile(MultipartFile arquivo) {
		//gera uma String aleatoria para o nome do arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		
		
		return nomeArquivo;
		
	}
	
	// retorna a extensao de um arquivo pelo seu nome
	private String getExtensao(String nomeArquivo) {
		//retorna o trecho da string que vai do ultimo ponto ate o fim
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
}
