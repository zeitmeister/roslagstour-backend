package com.example.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Item;
import com.example.demo.model.Stop;
import com.example.demo.model.StopDetail;
import com.example.demo.model.StopDetailItem;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@Component
@Path("/stops")
@Produces("application/json")
public class StopsController {
	private StopDetailItem<StopDetail> responseItem = null;
	
	@POST
	@Produces("application/json")
	@Path("/lineStopDetails")
	public Response GetStopDataFromLine(@RequestBody String[] stops) throws ParseException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		
		//just to make fewer requests to the SL API to prevent the request threshold being reached
		if (responseItem == null) {
			String url = "https://api.sl.se/api2/linedata.json?key=[INSERT KEY HERE]&model=stop&DefaultTransportModeCode=BUS";
			HttpGet request = new HttpGet(url);
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			
			responseItem = objectMapper.readValue(result, StopDetailItem.class);
		}
		
		StopDetail[] stopDetails = responseItem.ResponseData.Stops;
		ArrayList<ArrayList<StopDetail>> stopDetailWrapper = new ArrayList<ArrayList<StopDetail>>();
		ArrayList<StopDetail> stopDetailsList = null;
		for (String stopNumber : stops) {
			
			for (StopDetail stopDetail : stopDetails) {
				
				if (stopDetail.StopPointNumber.equals(stopNumber)) {
					if (stopDetailsList == null) {
						stopDetailsList = new ArrayList<StopDetail>();
						stopDetailsList.add(stopDetail);
						stopDetailWrapper.add(stopDetailsList);

					} else {
						stopDetailsList.add(stopDetail);
						stopDetailWrapper.add(stopDetailsList);
					}
				} else {
					continue;
				}
				
			}
		}
		
		
		String jsonString = objectMapper.writeValueAsString(stopDetailsList);
		return Response
			      .status(200)
			      .entity(jsonString)
			      .build();
		
	}

}
