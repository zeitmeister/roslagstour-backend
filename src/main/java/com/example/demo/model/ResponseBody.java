package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseBody <T>{
	public String Version;
	public String Type;
	@JsonProperty("Result")
	public Stop[] Stops;
}
