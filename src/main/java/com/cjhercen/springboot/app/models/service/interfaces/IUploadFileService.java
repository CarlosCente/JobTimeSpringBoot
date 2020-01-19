package com.cjhercen.springboot.app.models.service.interfaces;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	public Resource load(String filename) throws MalformedURLException;
	
	public void store(MultipartFile file) throws IOException;
	
	public String copy(MultipartFile file) throws IOException;
	
	public boolean delete(String filename);
	
	public void deleteAll();
	
	public void init() throws IOException;
	
}
