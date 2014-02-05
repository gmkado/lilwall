package com.example.bluetooth;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Color;


public class WallObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String wallName;
	private int numCols,numRows;
	private LedColor[][] ledArray;
	
	public enum LedColor {
		BLACK(Color.BLACK),
		BLUE(Color.BLUE),
		CYAN(Color.CYAN),	
		GREEN(Color.GREEN),	
		MAGENTA(Color.MAGENTA),	
		RED(Color.RED),	
		WHITE(Color.WHITE),
		YELLOW(Color.YELLOW);
		
		private final int colorVal;
		
		LedColor(int c){
			colorVal = c;
		}
		
		 public LedColor getNext() {
			 // method to get next color in list
		     return this.ordinal() < LedColor.values().length - 1? 
		    	LedColor.values()[this.ordinal() + 1]
		    	: LedColor.values()[0];
		   }
	}
	
	static AtomicInteger nextId = new AtomicInteger();
    private int id;

    
	public WallObject(String w, int r, int c)
	{
		wallName = w;
		numCols = c;
		numRows = r;
		
		id = nextId.incrementAndGet();	// set a unique ID for each wall
		
		// create an array of colors
		ledArray = new LedColor[numRows][numCols];
		// fill with all with black (LED off)
		for (int i = 0; i<numRows; i++){
			for(int j = 0; j<numCols; j++){
				ledArray[i][j] = LedColor.BLACK;
				
			}
		}
	}

	public int getID()
	{
		return id;
	}
	
	public void nextColor(int row, int col){
		// set color to the next color in list
		ledArray[row][col] = ledArray[row][col].getNext();
	}
	
	public int getColorVal(int row, int col){
		return ledArray[row][col].colorVal;
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
	
	public LedColor[][] getLedArray(){
		return ledArray;
	}
}
