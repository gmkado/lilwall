package com.example.lilwall.model;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Color;


public class WallObject implements Serializable{
	/**
	 * 
	 */
	private static final String TAG = "WallObject";
	private static final long serialVersionUID = 1L;
	
	/****************** object variables *****************************/
	private String wallName;
	private int numCols,numRows;
	private Integer[][] ledArray; 		    // 2d array of Colors
	
	/*********** for making incremental id's **********/
	private static AtomicInteger nextId = new AtomicInteger();
    private final int id;
    
	public WallObject(String w, int r, int c)
	{
		wallName = w;
		numCols = c;
		numRows = r;
		
		id = nextId.incrementAndGet();	// set a unique ID for each wall
		
		// create an array of colors
		ledArray = new Integer[numRows][numCols];
		clearAll();
	}

	public int getID()
	{
		return id;
	}
	
	
	public void setLED(int color, int position){
		int[] xy = posToXY(position);
		
		// update the array color
		ledArray[xy[0]][xy[1]] = color;
	}
	
	public int getColorVal(int position){
		int[] xy = posToXY(position);
		return ledArray[xy[0]][xy[1]];
	}
	
	public int getNumRows()
	{
		return numRows;
	}
	public int getNumCols()
	{
		return numCols;
	}
	
	public String getWallName()
	{
		return wallName;
	}
	
	public Integer[][] getLedArray(){
		return ledArray;
	}
	
	public void clearAll(){
		// fill with all with black (LED off)
		for (int i = 0; i<numRows; i++){
			for(int j = 0; j<numCols; j++){
				ledArray[i][j] = Color.BLACK;

			}
		}
	}
	
	private int[] posToXY(int position){
		int col = position % getNumRows();
		int row = (position - col)/getNumCols();
		int[] coord = {col,row};
		
		return coord;
	}
	
}
