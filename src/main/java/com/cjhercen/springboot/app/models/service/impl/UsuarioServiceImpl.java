package com.cjhercen.springboot.app.models.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl {
	public String getUsername() {
			
			String userName = "";
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	
			userName = auth.getName();
		
			return userName;
	}
}
