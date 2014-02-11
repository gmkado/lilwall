package com.example.lilwall;

public enum BtMessageType {
	CHANGE_COLOR(0),
	CLEAR_ALL(1);
	
	private final int value;
	private BtMessageType(int value)
	{
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
}
