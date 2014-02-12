/**
 * 
 */
package com.example.lilwall;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.example.lilwall.R;
import com.example.lilwall.ColorPickerFragment.ColorPickerDialogListener;
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
public class LEDGridAdapter<Integer> extends ArrayAdapter<Integer> {
	int colorVal;
	WallObject myWall;
	private MyApp appState;

	private static final String TAG ="LEDGridAdapter";
	
    public LEDGridAdapter(Context context, int textViewResourceId, WallObject wo) {
        super(context, textViewResourceId);
        myWall = wo;   
        Log.v(TAG, "Got to constructor");
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
        
        TextView tv = (TextView) cellView.findViewById(R.id.led_grid_item);
        Log.v(TAG, "getView");
        tv.setBackgroundColor(myWall.getColorVal(position));
        
        // what to do when led button is clicked
        // CURRENTLY: cycle color value
        /*b.setOnClickListener(new View.OnClickListener() {
            

			@Override
            public void onClick(View v) {            	         
                myWall.nextColor(position);
				colorVal = myWall.getColorVal(position);
                Log.v(TAG, "colorval = "+colorVal);
                v.setBackgroundColor(colorVal);
                
                // write to bluetooth
                appState = (MyApp) getContext().getApplicationContext();
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
        });*/
             
        return cellView;
    }

}
