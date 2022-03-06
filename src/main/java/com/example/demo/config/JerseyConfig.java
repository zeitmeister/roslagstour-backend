package com.example.demo.config;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.demo.controllers.LinesController;
import com.example.demo.controllers.StopsController;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
	
	@PostConstruct
	public void init() {
		configEndPoints();
	}
	
	private void configEndPoints() {
		register(LinesController.class);
		register(StopsController.class);
		register(CorsFilter.class);
	}

}
