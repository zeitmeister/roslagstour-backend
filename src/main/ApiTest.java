package com.example.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Path("/clients")
@Produces("application/json")
@Api(value = "ApiTest Resource", produces = "application/json")
public class ApiTest {
	
	@GET
	@Path("/hello")
	public Response getClient() {
		System.out.println("heajsan");
		Response.ok().entity("{"Im alive and wee"}").build();
		return null;
	}

}
