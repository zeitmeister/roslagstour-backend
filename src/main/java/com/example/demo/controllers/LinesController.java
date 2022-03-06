package com.example.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Item;
import com.example.demo.model.Stop;
import com.example.demo.helpers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RestController
@Path("/lines")
@Produces("application/json")
public class LinesController {

	@GET
	
	@Produces("application/json")
	@Path("/top10StoppingLines")
	public String get10LinesWithMostStops() throws InterruptedException, ExecutionException, ClientProtocolException, IOException {
		String url = "https://api.sl.se/api2/linedata.json?key=[INSERT KEY HERE]&model=jour&LineNumber=1&DefaultTransportModeCode=BUS";
		HttpGet request = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity);
		ObjectMapper objectMapper = new ObjectMapper();
		Item<Stop> responseItem = objectMapper.readValue(result, Item.class);
		
		//adding the stops to a map to make the data easier to work with
		Map<String, ArrayList<Stop>> stopMap = new HashMap<String, ArrayList<Stop>>();
		Stop[] resBody = responseItem.ResponseData.Stops;
		var stops = responseItem.ResponseData.Stops;
		for (Stop stop : stops) {
			
			ArrayList<Stop> stopToCheck = stopMap.get(stop.LineNumber);
			if (stopToCheck == null) {
				stopToCheck = new ArrayList<Stop>();
				
				stopToCheck.add(stop);
				stopMap.put(stop.LineNumber, stopToCheck);
			} else {
				stopMap.get(stop.LineNumber).add(stop);
				
			}
		}
		
		//Sorting based on most stops, using a custom comparer
		TreeMap<String, ArrayList<Stop>> mapSorted = new TreeMap<String, ArrayList<Stop>>(new MapSorter(stopMap));
		mapSorted.putAll(stopMap);
		
		//Get just the top 10
		HashMap<String,ArrayList<Stop>> top10 =  mapSorted.entrySet().stream()
				.limit(10)
				.collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
		
		//sorting again because limiting messes up the sorting for some reason
		TreeMap<String, ArrayList<Stop>> mapSortedAgain = new TreeMap<String, ArrayList<Stop>>(new MapSorter(top10));
		mapSortedAgain.putAll(top10);
		
		//Send the sorted map as json
		String jsonString = objectMapper.writeValueAsString(mapSortedAgain);
		return jsonString;
	}
}
