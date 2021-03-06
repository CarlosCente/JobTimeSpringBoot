package com.cjhercen.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cjhercen.springboot.app.auth.handler.LoginSuccessHandler;
import com.cjhercen.springboot.app.models.service.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	      authenticationManagerBuilder
	              .userDetailsService(userDetailsService)
	              .passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/images/**").permitAll()
		.antMatchers("/listar/**").hasAnyRole("ADMIN")
		.antMatchers("/incidencias/**").hasAnyRole("ADMIN")
		.antMatchers("/controlhorario/**").hasAnyRole("ADMIN")
		.antMatchers("/fichar/**").hasAnyRole("USER")
		/*.antMatchers("/uploads/**").hasAnyRole("USER")*/
		/*.antMatchers("/form/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and()
		    .formLogin()
		        .successHandler(successHandler)
		        .loginPage("/login")
		    .permitAll()
		.and()
		.logout()
        	.permitAll()
        	.logoutSuccessUrl("/login")
        	.invalidateHttpSession(true)
        	.deleteCookies("JSESSIONID");
	}

}
