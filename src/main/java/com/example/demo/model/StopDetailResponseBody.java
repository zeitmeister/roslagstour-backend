package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StopDetailResponseBody<T> {
	public String Version;
	public String Type;
	@JsonProperty("Result")
	public StopDetail[] Stops;
}

