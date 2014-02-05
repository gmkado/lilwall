package com.example.bluetooth;

public enum BtMessageType {
	CHANGE_COLOR(0);
	
	private final int value;
	private BtMessageType(int value)
	{
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
}
