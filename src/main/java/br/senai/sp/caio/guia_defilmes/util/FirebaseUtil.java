package br.senai.sp.caio.guia_defilmes.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

//para spring reconhecer a classe ultiliza o Service
@Service
public class FirebaseUtil {
	// variavel para guardar as credencias do Firebase
	private Credentials credentiais;
	// variavel para acesar o storage
	private Storage storage;
	// constante pra o nome do bucket
	private final String BUCKET_NAME = "guiafilmes-d910f.appspot.com";
	// constante para o prefixo
	private String PREFIX = "https://firebasestorage.googleapis.com/v0/b/"+BUCKET_NAME+"/o/";
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
	
	public String uploadFile(MultipartFile arquivo) throws IOException {
		//gera uma String aleatoria para o nome do arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		
		//criando um BlobID
		BlobId blobid = BlobId.of(BUCKET_NAME, nomeArquivo);
		//criar um blobInfo a partir do BlobId
		BlobInfo blobInfo =  BlobInfo.newBuilder(blobid).setContentType("media").build();
		//manda o blobInfo para o Storage passando os bytes do arquivo pra ele
		storage.create(blobInfo, arquivo.getBytes());
		//retornar a URL para acessar o arquivo
		return String.format(DOWNLOAD_URL, nomeArquivo);
		
	}
	
	// retorna a extensao de um arquivo pelo seu nome
	private String getExtensao(String nomeArquivo) {
		//retorna o trecho da string que vai do ultimo ponto ate o fim
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
	
	//metodo para excluir a foto do FireBase
	public void deletar(String nomeArquivo) {
		//retira o prefixo eo sufixo do nome do arquivo
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SUFFIX, "");
		//pega um blob através do nome do arquivo
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
		//deleta o aquivo
		storage.delete(blob.getBlobId());
	}
	
}
