package com.example.lilwall;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.lilwall.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private List<WallObject> wallList;
	MyApp appState;
	WallListViewAdapter m_adapter;
	static final int CONFIGURE_NEW_WALL = 0;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
								
		
        // load tasks from preference
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		// the wall list is saved in shared preferences as a serialized string.  try deserializing
        // return an empty arraylist if the pref doesnt exist
		try {
            wallList = (ArrayList<WallObject>) deserialize(prefs.getString("WALL_LIST", serialize(new ArrayList<WallObject>())));
        } catch (IOException e) {
            e.printStackTrace();
        	wallList = new ArrayList<WallObject>();
        } 
		
		//set the appstate to store the wallList.  this is how well pass it between classes  
		appState = (MyApp) getApplicationContext();
		appState.setWallList(wallList);	
		
		// instantiate our listview adapter which populates the view with our wall objects
		m_adapter = new WallListViewAdapter(this, R.layout.wall_list_item, wallList);
        ListView m_listview = (ListView)findViewById(R.id.wallListView);
		
		//add a new listener for the listview
        m_listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent intent = new Intent(view.getContext(),LedGridActivity.class);	  
				
				//set the appstate to store the selected wall and start the ledgrid activity
	        	appState.setWall(position);
	    		startActivity(intent);
          
            }
        });
        
		// use our custom listview adapter with our listview
        m_listview.setAdapter(m_adapter);
        registerForContextMenu(m_listview);
	}
 
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// returning from an activity...
		
		// check which activity
		if (requestCode == CONFIGURE_NEW_WALL) {
            if (resultCode == RESULT_OK) {
                // wall was successfully created
				// get the wall object from the intent and add to our wall list
            	wallList.add((WallObject) data.getSerializableExtra("newWall"));
				
				// update stored wall list and listview
				appState.setWallList(wallList);
				m_adapter.notifyDataSetChanged();
			}
        }
		
		
		
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
			//save wall list to prefs as a serialized string
            editor.putString("WALL_LIST", serialize((Serializable) wallList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId()==R.id.wallListView) {
			// user has long pressed on a wall item
	    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    	menu.setHeaderTitle(((WallObject)wallList.get(info.position)).getWallName());
			
			// populate the context menu
	    	String[] menuItems = getResources().getStringArray(R.array.menu);
	    	for (int i = 0; i<menuItems.length; i++) {
	      		menu.add(Menu.NONE, i, i, menuItems[i]);
	    	}
	  	}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  switch(menuItemIndex)
	  {
	  	case 0: 	
			// Edit selected wall
	  		// TODO: Launch edit activity	  
	  		break;
	  	case 1:
			// delete selected wall
	  		wallList.remove(info.position);	
	  		appState.setWallList(wallList);
			m_adapter.notifyDataSetChanged();
	  		break;
	  		
	  }
	  return true;
	}
	
	public boolean configureNewWall(MenuItem item){
		// launch activity to configure new wall
		Intent intent = new Intent(this,ConfigureNewWall.class);
		startActivityForResult(intent,CONFIGURE_NEW_WALL);
		return true;
	}

	
	
    /************* code for serializing and deserializing our wall list **********/
	
    public static String serialize(Serializable obj) throws IOException {
        if (obj == null) return "";
        try {
            ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
            objStream.writeObject(obj);
            objStream.close();
            return encodeBytes(serialObj.toByteArray());
        } catch (Exception e) {
            throw new IOException();//WrappedIOException.wrap("Serialization error: " + e.getMessage(), e);
        }
    }
    
    public static Object deserialize(String str) throws IOException {
        if (str == null || str.length() == 0) return null;
        try {
            ByteArrayInputStream serialObj = new ByteArrayInputStream(decodeBytes(str));
            ObjectInputStream objStream = new ObjectInputStream(serialObj);
            return objStream.readObject();
        } catch (Exception e) {
            throw new IOException();//WrappedIOException.wrap("Deserialization error: " + e.getMessage(), e);
        }
    }
    
    public static String encodeBytes(byte[] bytes) {
        StringBuilder strBuf = new StringBuilder();
    
        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }
        
        return strBuf.toString();
    }
    
    public static byte[] decodeBytes(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i+=2) {
            char c = str.charAt(i);
            bytes[i/2] = (byte) ((c - 'a') << 4);
            c = str.charAt(i+1);
            bytes[i/2] += (c - 'a');
        }
        return bytes;
    }
}
