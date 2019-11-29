package com.cjhercen.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cjhercen.springboot.app.models.service.interfaces.IUploadFileService;


@SpringBootApplication
public class SpringBootJobtimeApplication implements CommandLineRunner{

	@Autowired
	IUploadFileService uploadFileService;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootJobtimeApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
	}

}
