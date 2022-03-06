package com.example.demo.model;

public class Item<T> {
	
	public int StatusCode;
	public String Message;
	public int ExecutionTime;
	public ResponseBody<T> ResponseData;
}
