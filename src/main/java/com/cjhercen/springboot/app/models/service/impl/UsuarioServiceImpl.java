package com.cjhercen.springboot.app.models.service.impl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cjhercen.springboot.app.models.dao.IUsuarioDao;
import com.cjhercen.springboot.app.models.entity.Usuario;

@Primary
@Service
public class UsuarioServiceImpl implements IUsuarioDao {
	
	@Autowired
	IUsuarioDao iUsuarioDao;
	
	public String getUsername() {
			
			String userName = "";
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	
			userName = auth.getName();
		
			return userName;
	}

	
	
	@Override
	public <S extends Usuario> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Usuario> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Usuario> findAll() {
		return iUsuarioDao.findAll();
	}

	@Override
	public Iterable<Usuario> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Usuario entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Usuario> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Usuario findByUsername(String username) {
		return iUsuarioDao.findByUsername(username);
	}
	
	
	
}
