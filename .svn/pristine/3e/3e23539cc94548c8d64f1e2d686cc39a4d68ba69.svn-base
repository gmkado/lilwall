package com.example.bluetooth;

import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Color;

public class LedObject {
	LedColor color;
	
	private enum LedColor {
		BLACK(Color.BLACK),
		 WHITE(Color.WHITE), 
		 RED(Color.RED), 
		 YELLOW(Color.YELLOW),
		 BLUE(Color.BLUE);
		
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
	
	public LedObject()
	{
		color = LedColor.BLACK;
	}	
	
	public void nextColor(int row, int col){
		// set color to the next color in list
		color = color.getNext();
	}
	
	public int getColorVal(int row, int col){
		return color.colorVal;
	}

}
