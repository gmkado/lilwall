package com.example.lilwall;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Color;
import android.util.Log;


public class WallObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "WallObject";
	
	private String wallName;
	private int numCols,numRows;
	private Integer[][] ledArray; 		// 2d array of Colors
	private float[] ledHSV = new float[3]; // for incrementing color value 
	private static float hInc = 30;
	
/*	public enum LedColor {
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
	*/
	
	static AtomicInteger nextId = new AtomicInteger();
    private int id;

    
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
	
	public void setColor(int pos, int color){
		int[] xy = posToXY(pos);
		setColor(xy[0],xy[1], color); 
	}
	
	public void setColor(int row, int col, int color){
		ledArray[row][col] = color;
	}
	
	public void nextColor(int row, int col){
		// set color to the next color in list
		Color.colorToHSV(ledArray[row][col], ledHSV);
		ledHSV[0]= (ledHSV[0] + hInc)%360;
		ledHSV[1] = 1;
		ledHSV[2] = 1;
		
		ledArray[row][col] = Color.HSVToColor(ledHSV);
		Log.v(TAG, "Color = "+ledArray[row][col]);
	}
	
	public int getColorVal(int row, int col){
		return ledArray[row][col];
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

	public void nextColor(int position) {
		int[] xy = posToXY(position);
        nextColor(xy[0],xy[1]);
	}

	public int getColorVal(int position) {
		int[] xy = posToXY(position);
		return getColorVal(xy[0], xy[1]);
	}
	
	private int[] posToXY(int position){
		int col = position % getNumRows();
		int row = (position - col)/getNumCols();
		int[] coord = {col,row};
		
		return coord;
	}
	
}
