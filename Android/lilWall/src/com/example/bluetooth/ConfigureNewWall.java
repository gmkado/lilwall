package com.example.bluetooth;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ConfigureNewWall extends Activity {

	ArrayList<WallObject> wallList;
	MyApp appState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure_new_wall);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_configure_new_wall, menu);
		return true;
	}

	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();		
		appState = ((MyApp)getApplicationContext());
	}

	public void saveWall(View v){
		// onClick callback for save button
		//Intent intent = new Intent(this,MainActivity.class);
		EditText name = (EditText)findViewById(R.id.nameWall);
		String n = name.getText().toString();
		EditText numCol = (EditText)findViewById(R.id.numColEdit);
		int c = Integer.parseInt(numCol.getText().toString());
		EditText numRow = (EditText)findViewById(R.id.numRowEdit);
		int r = Integer.parseInt(numRow.getText().toString());
		
		WallObject wall = new WallObject(n, r, c);
		//wallList.add(wall);
		//appState.setWallList(wallList);
		//intent.putExtras(data);
		
		Intent data = new Intent();
		data.putExtra("newWall", wall);
		
		
		setResult (RESULT_OK, data);
		finish();
		//startActivity(intent);
	}

	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}*/
	
	
}
