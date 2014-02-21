package com.example.lilwall.activity;

import android.app.*;
import android.bluetooth.*;
import android.graphics.Color;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemLongClickListener;

import com.example.lilwall.BtConnection;
import com.example.lilwall.BtMessageType;
import com.example.lilwall.MyApp;
import com.example.lilwall.R;
import com.example.lilwall.R.id;
import com.example.lilwall.R.layout;
import com.example.lilwall.R.menu;
import com.example.lilwall.adapter.RouteListAdapter;
import com.example.lilwall.adapter.WallGridAdapter;
import com.example.lilwall.model.RouteObject;
import com.example.lilwall.model.WallObject;
import com.example.lilwall.model.WallObject.*;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;

import java.io.*;
import java.util.*;

public class WallActivity extends Activity implements OnColorChangedListener  {
	private static final String TAG = "LEDGridActivity";
	
	
	/*********************************************************/
	private WallObject myWall;
	private BtConnection mybluetooth;
	private MyApp appState;
	private SparseArray<RouteObject> routeList;	// dictionary of <color -> routes>, linked for listadapter
	private Integer currentRouteColor;
	private ColorPicker picker;
	
	/******* View Adapaters ****************/
	private WallGridAdapter<Integer> gAdapter;
	private RouteListAdapter<RouteObject> lAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ledgrid);
		
		appState = (MyApp) getApplicationContext();
		myWall = appState.getWall();
		GridView gridview = (GridView)findViewById(R.id.ledgrid);
		
		// setup bluetooth
		mybluetooth = new BtConnection(this, appState);
		mybluetooth.connectBlueTooth(); // try connecting
		
		// add borders to the gridview
		gridview.setBackgroundColor(Color.WHITE);
		gridview.setVerticalSpacing(1);
		gridview.setHorizontalSpacing(1);
		
		// add adapter to ledgrid view
		gridview.setNumColumns(myWall.getNumCols());
		gAdapter=new WallGridAdapter<Integer>(this,R.layout.led_grid_item,myWall);
		gridview.setAdapter(gAdapter);

		// add adapter to routelist view
		ListView listview = (ListView)findViewById(R.id.routelist);
		routeList = new SparseArray<RouteObject>();
		lAdapter = new RouteListAdapter<RouteObject>(this, R.layout.route_list_item, routeList);
		listview.setAdapter(lAdapter);		
		
		picker = (ColorPicker) findViewById(R.id.picker);
		picker.setOnColorChangedListener(this);
		currentRouteColor = picker.getColor();
		/* Color picker fragment found here: 
		 https://github.com/LarsWerkman/HoloColorPicker   */
     } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wallgrid_menu, menu);
		return true;
	}
	
	public boolean clearAllLEDs(MenuItem item){
		// launch activity to configure new wall
		myWall.clearAll();
		gAdapter.notifyDataSetChanged();
		
		BluetoothSocket socket = appState.getBtSocket();
		if(socket == null)
		{
			//Toast.makeText(appState, "No bluetooth socket saved", Toast.LENGTH_SHORT).show();
		}else{
			OutputStream mmOutStream;
			try {
				mmOutStream = socket.getOutputStream();

				BtMessageType mt = BtMessageType.CLEAR_ALL;
				byte msglength = 0;
				byte[] message =  new byte[] {msglength, (byte) mt.getValue()};        				
				mmOutStream.write(message);
			} catch (IOException e) {
			}
		}
		return true;
	}
	
	public void addRoutePosition(int color, int position)
	{		
		/* when a grid button is pushed, addRoutePosition checks if the color matches any existing routes
		*    -> if not, then create new route with a new ID
		*	-> if it does, add to that existing route
		*   This allows the routes to be saved by ID (no attached color) but tracked by this activity using color (no two climbs should have the same color)
		*/
		if (color != currentRouteColor)
		{
			// new color was selected in WallGridAdapter
			currentRouteColor = color;
		}
		
		if(routeList.indexOfKey(color)<0){
			// the color isn't part of our routelist, create new route
			routeList.put(color, new RouteObject());	
			lAdapter.notifyDataSetChanged();
		}
		routeList.get(color).addPosition(position);
	}
	
    @Override 
	protected void onDestroy() {
		super.onDestroy();
		mybluetooth.stop();
	}
    
    
    public void setCurrentRouteColor(int nColor)
    {
    	currentRouteColor = nColor;
    }
    
    public int getCurrentRouteColor()
    {
    	return currentRouteColor;
    }

	@Override
	public void onColorChanged(int color) {
		setCurrentRouteColor(color);
	}
}
