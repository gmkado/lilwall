package com.example.lilwall.activity;


import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

import com.example.lilwall.MyApp;
import com.example.lilwall.R;
import com.example.lilwall.R.id;
import com.example.lilwall.R.layout;
import com.example.lilwall.R.menu;
import com.example.lilwall.model.WallObject;

public class ConfigureWallActivity extends Activity {

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
		getMenuInflater().inflate(R.menu.configure_wall_menu, menu);
		return true;
	}

	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();		
		appState = ((MyApp)getApplicationContext());
	}

	public void saveWall(View v)
	{
		try
		{
			// onClick callback for save button
			EditText name = (EditText)findViewById(R.id.nameWall);
			String n = name.getText().toString();
			EditText numCol = (EditText)findViewById(R.id.numColEdit);
			int c = Integer.parseInt(numCol.getText().toString());
			EditText numRow = (EditText)findViewById(R.id.numRowEdit);
			int r = Integer.parseInt(numRow.getText().toString());

			WallObject wall = new WallObject(n, r, c);

			Intent data = new Intent();
			data.putExtra("newWall", wall);


			setResult(RESULT_OK, data);
			finish();
		}
		catch (Exception e)
		{ 
			Toast.makeText(this, "Missing fields", Toast.LENGTH_LONG).show();
			//finish();
		}
	}

	/*@Override
	 public void onBackPressed() {
	 // TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}*/
	
	
}
