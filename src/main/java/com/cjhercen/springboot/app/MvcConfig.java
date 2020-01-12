package com.cjhercen.springboot.app;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
//		super.addResourceHandlers(registry);
//		
//		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
//		log.info(resourcePath);
//		
//		registry.addResourceHandler("/uploads/**")
//		.addResourceLocations(resourcePath);
		
		registry
        .addResourceHandler("/uploads/imagenes/**")
        .addResourceLocations("file:///C:/jobtime/uploads/imagenes/");
		
	}
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
	}
	
}
