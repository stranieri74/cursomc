package com.nelioalves.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	// fazer upload do arquivo
	public URI uploadFile(MultipartFile multipartFile) {
		try {
			// extrai o nome do arquivo
			String fileName = multipartFile.getOriginalFilename();
			// encapsula os objetos
			InputStream is = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();

			return uploadFile(is, fileName, contentType);
		} catch (IOException e) {
			throw new RuntimeException("Erro de IO: "+ e.getMessage());
		}
		// pega a extensão do arquivo

	}

	// metodo sobrecarregado
	public URI uploadFile(InputStream is, String fileName, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			// s3client.putObject(new PutObjectRequest(bucketName, "teste1.jpg", file));
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload Finalizado");

			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao converter URL para URI");

		}
		/*
		 * } catch (AmazonServiceException e) { LOG.info("AmazonServiceException: " +
		 * e.getErrorMessage()); // retor a o status do servidor
		 * LOG.info("Status code: " + e.getErrorCode()); } catch (AmazonClientException
		 * e) { LOG.info("AmazonClientException: " + e.getMessage()); }
		 */
	}
}
