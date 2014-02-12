package com.example.lilwall;

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

import com.example.lilwall.ColorPickerFragment.ColorPickerDialogListener;
import com.example.lilwall.R;
import com.example.lilwall.WallObject.*;
import com.larswerkman.holocolorpicker.ColorPicker;

import java.io.*;
import java.util.*;

public class LEDGridActivity extends Activity implements ColorPickerDialogListener {
	private BluetoothAdapter bluetooth = null;
	private ConnectThread mConnectThread;
	private static ConnectedThread mConnectedThread;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_DEVICE_NAME = 2;
    public static final int MESSAGE_TOAST = 3;
    public static final String TOAST = "toast";
	private static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final String TAG = "LEDGridActivity";
	private String connectedDeviceName = null;
	
	private WallObject myWall;
	private MyApp appState;
	LEDGridAdapter<Integer> mAdapter;
	int mLEDpos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallgrid);

		bluetooth = BluetoothAdapter.getDefaultAdapter();
		
		if (bluetooth == null) {
			Toast.makeText(this, "No Bluetooth adapter detected on device.", Toast.LENGTH_LONG).show();
			//finish();
			return;
		}
		else {
			BluetoothDevice device = bluetooth.getRemoteDevice("00:06:66:64:3C:37");
			Log.v(TAG, "connecting to 00:06:66:64:3C:37");
			mConnectThread = new ConnectThread(device);
			mConnectThread.start();
		}
		
		appState = (MyApp) getApplicationContext();
		myWall = appState.getWall();
		GridView gridview = (GridView)findViewById(R.id.wallgrid);
		
		// add borders to the gridview
		gridview.setBackgroundColor(Color.WHITE);
		gridview.setVerticalSpacing(1);
		gridview.setHorizontalSpacing(1);
		
		// add adapter to gridview
		gridview.setNumColumns(myWall.getNumCols());
		mAdapter=new LEDGridAdapter<Integer>(this,R.layout.led_grid_item,myWall);
		
		gridview.setAdapter(mAdapter);

		registerForContextMenu(gridview);
		
     } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wallgrid_menu, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if (v.getId()==R.id.wallgrid) {
			// user has long pressed on an led
			DialogFragment newFragment = new ColorPickerFragment();
		    newFragment.show(getFragmentManager(), "colorpicker");
		    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		    mLEDpos = info.position;
			
			/*// get which LED was pressed
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			menu.setHeaderTitle("Change LED "+ info.position);
			
			// populate the context menu
	    	String[] menuItems = getResources().getStringArray(R.array.ledgrid_context);
	    	for (int i = 0; i<menuItems.length; i++) {
	      		menu.add(Menu.NONE, i, i, menuItems[i]);
	    	}	*/		
	  	}
	}
	
/*	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  switch(menuItemIndex)
	  {
	  	case 0: 	
			// Choose color
	  		// show color picker fragment
	    	DialogFragment newFragment = new ColorPickerFragment();
		    newFragment.show(getFragmentManager(), "colorpicker");	  
		    mLEDpos = info.position;
	  		break;
	  	case 1:
			// reset color of hold
	  		// TODO: set to black
	  		break;
	  		
	  }
	  return true;
	}*/

	public boolean clearAllLEDs(MenuItem item){
		// launch activity to configure new wall
		myWall.clearAll();
		mAdapter.notifyDataSetChanged();
		
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
	
	public void bluetoothSendMessage(byte[] messageToSend) {
		mConnectedThread.write(messageToSend);
	}
	
	private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
			if (mmDevice.getUuids() != null) {
				uuid = mmDevice.getUuids()[0].getUuid();
			} else {
				uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
			}
			
			Log.v(TAG, "Connecting with UUID: " + uuid.toString());

	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(uuid);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
				Log.v(TAG, "couldn't connect device in run()");
	        	try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
			Log.v(TAG, "connected");
	        connected(mmSocket, mmDevice);
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
			
	        Log.v(TAG,"ConnectThread.cancel()");

	    }
	}	
	
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		Log.v(TAG, socket.toString());
		
		// save socket to global variable
		appState.setBtSocket(socket);
		
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// Send the name of the connected device back to the UI Activity
		Message msg = messageHandler.obtainMessage(MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString("device_name", device.getName());
		msg.setData(bundle);
		messageHandler.sendMessage(msg);
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					// Send the obtained bytes to the UI activity
					messageHandler.obtainMessage(MESSAGE_READ, bytes, -1,
							buffer).sendToTarget();
				} catch (IOException e) {
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			Log.v(TAG,"Sending Bluetooth message");
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private final Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
			switch (msg.what) {
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                connectedDeviceName = msg.getData().getString("device_name");
                Toast.makeText(getApplicationContext(), "Connected to "
                               + connectedDeviceName, Toast.LENGTH_LONG).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };	
    
    @Override 
	protected void onDestroy() {
		super.onDestroy();
		stop();
	}
    
	public synchronized void stop() {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

	}

	@Override
	public void onDialogPositiveClick(ColorPickerFragment dialog) {
		// get color and set mywall hold
		myWall.setColor(mLEDpos, dialog.getmColor());
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDialogNegativeClick(ColorPickerFragment dialog) {
		// TODO Auto-generated method stub
		
	}
}
