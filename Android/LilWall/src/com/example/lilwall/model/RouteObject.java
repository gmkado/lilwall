package com.example.lilwall.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RouteObject {
	/* This class contains information for route
	 * 
	 * 
	 */
	private static final String TAG = "RouteObject";
	
	/***************   object variables ****************************/
	private List<Integer> rPositions;
	private String rName;
	
	/*************** for making incremental id's **********/
	private static AtomicInteger nextId = new AtomicInteger();
    final private int rId;
    
	public RouteObject()
	{
		rPositions = new ArrayList<Integer>();
		rId = nextId.incrementAndGet();	// set a unique ID for each wall
		rName = "Route "+rId;
	}
	

	public List<Integer> getrPositions() {
		return rPositions;
	}

	public void setrPositions(List<Integer> rPositions) {
		this.rPositions = rPositions;
	}

	public void addPosition(int position){
		rPositions.add(Integer.valueOf(position));
	}
	
	public String getRouteName() {
		return rName;
	}

	public void setRouteName(String rName) {
		this.rName = rName;
	}

	public int getId() {
		return rId;
	}
	
	
}
