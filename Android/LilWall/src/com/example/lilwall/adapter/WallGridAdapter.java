/**
 * 
 */
package com.example.lilwall.adapter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.example.lilwall.BtMessageType;
import com.example.lilwall.MyApp;
import com.example.lilwall.R;
import com.example.lilwall.R.id;
import com.example.lilwall.R.layout;
import com.example.lilwall.activity.WallActivity;
import com.example.lilwall.model.WallObject;
import com.larswerkman.holocolorpicker.ColorPicker;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author Grant
 *
 */
public class WallGridAdapter<Integer> extends ArrayAdapter<Integer> {
	int colorVal;
	WallObject myWall;
	private MyApp appState;

	private static final String TAG ="LEDGridAdapter";
	
    public WallGridAdapter(Context context, int textViewResourceId, WallObject wo) {
        super(context, textViewResourceId);
        myWall = wo;   
    }
    
    public int getCount()
    {
      	return myWall.getNumCols()*myWall.getNumRows();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View cellView = convertView;

        
        if (cellView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            cellView = inflater.inflate(R.layout.led_grid_item, parent, false);
        }
        
        Button b = (Button) cellView.findViewById(R.id.led_grid_button);
        Log.v(TAG, "getView");
        b.setBackgroundColor(myWall.getColorVal(position));
        
        // what to do when led button is clicked
        // CURRENTLY: cycle color value
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {            	         
                WallActivity mActivity = (WallActivity) getContext();
            	int colorVal = mActivity.getCurrentRouteColor();
        		
                myWall.setLED(colorVal, position);                
                mActivity.addRoutePosition(colorVal, position);
                
                v.setBackgroundColor(colorVal);
                
                // write to bluetooth
                appState = (MyApp) mActivity.getApplicationContext();
                BluetoothSocket socket = appState.getBtSocket();
                if(socket == null)
                {
                	//Toast.makeText(appState, "No bluetooth socket saved", Toast.LENGTH_SHORT).show();
                }else{
                	OutputStream mmOutStream;
                    try {
        				mmOutStream = socket.getOutputStream();
        				
        				BtMessageType mt = BtMessageType.CHANGE_COLOR;
        				byte msglength = 5;
        				byte[] message =  new byte[] {msglength, (byte) mt.getValue(), (byte) position,  
        						(byte) Color.red(colorVal) ,(byte) Color.green(colorVal),(byte) Color.blue(colorVal)};        				
        				mmOutStream.write(message);
        			} catch (IOException e) {
        			}
                }
                
            }
        });
             
        return cellView;
    }

}
