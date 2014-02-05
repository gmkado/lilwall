package com.example.bluetooth;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class MyApp extends Application{
	private WallObject myWall;
	private BluetoothSocket btSocket= null;
	private List<WallObject> myWallList;
	
	/****************WallList***************/
	public List<WallObject> getWallList() {
		return myWallList;
	}

	public void setWallList(List<WallObject> wallList) {
		this.myWallList = wallList;
	}
	
	  /************** Wall object *****************/
	  public WallObject getWall(){
		  return myWall;
	  }
	  
	  

	public void setWall(int position)
	  {
		  myWall = myWallList.get(position);
	  }
	  /************** bluetooth socket *****************/
	  public void setBtSocket(BluetoothSocket bts)
	  {
		  btSocket = bts;
	  }
	  
	  public BluetoothSocket getBtSocket()
	  {
		  return btSocket;
	  }
	  
}
