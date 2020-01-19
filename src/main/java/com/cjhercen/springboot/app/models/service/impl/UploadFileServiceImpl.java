package com.cjhercen.springboot.app.models.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cjhercen.springboot.app.models.service.interfaces.IUploadFileService;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	private final static String UPLOADS_FOLDER = "C:\\jobtime\\uploads\\imagenes";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);
		Resource recurso = null;

		recurso = new UrlResource(pathFoto.toUri());
		if (!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
		}

		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFileName);

		log.info("rootPath: " + rootPath);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {

		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();
		
		if(archivo.exists() && archivo.canRead()) {
			if(archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());		
	}

	@Override
	public void init() throws IOException {
		Files.createDirectories(Paths.get(UPLOADS_FOLDER));
	}
	
	@Override
	public void store(MultipartFile file) {
		 String filename = StringUtils.cleanPath(file.getOriginalFilename());
		 try {
		 if (file.isEmpty()) {
		 throw new RuntimeException("Fallo al intentar guardar archivo no existente: " + filename);
		 }
		 
		 // This is a security check
		 if (filename.contains("..")) {
		 throw new RuntimeException("No se puede guardar el archivo con una ruta fuera del directorio: " + filename);
		 }
		 
		 try (InputStream inputStream = file.getInputStream()) {
		 Files.copy(inputStream, Paths.get(UPLOADS_FOLDER) , StandardCopyOption.REPLACE_EXISTING);
		 }
		 } catch (IOException e) {
		 throw new RuntimeException("Failed to store file " + filename, e);
		 }
		}

}
