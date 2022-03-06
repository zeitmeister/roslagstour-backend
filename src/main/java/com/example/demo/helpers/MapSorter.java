package com.example.demo.helpers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import com.example.demo.model.Stop;

public class MapSorter implements Comparator<String> {
	
	private Map<String, ArrayList<Stop>> mapToSort;
	
	public MapSorter(Map<String, ArrayList<Stop>> map) {
		mapToSort = map;
	}
	
	

	@Override
	public int compare(String s1, String s2) {
		ArrayList<Stop> list1 = mapToSort.get(s1);
	    ArrayList<Stop> list2 = mapToSort.get(s2);
	    Integer length1 = list1.size();
	    Integer length2 = list2.size();
	    if (length1.equals(length2)) {
	    	return -1;
	    }
	    return length2.compareTo(length1);
	}

}
