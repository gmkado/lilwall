package com.example.lilwall.adapter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.lilwall.BtMessageType;
import com.example.lilwall.MyApp;
import com.example.lilwall.R;
import com.example.lilwall.activity.WallActivity;
import com.larswerkman.holocolorpicker.ColorPicker;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RouteListAdapter<RouteObject> extends ArrayAdapter<RouteObject>{
	private static final String TAG = "RouteListAdapter";
	SparseArray<RouteObject> routeList;
	Context context;
	
	
	public RouteListAdapter(Context context, int textViewResourceId, SparseArray<RouteObject> routeList) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.routeList = routeList;
	}

    public int getCount()
    {
      	return routeList.size();
    }
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.route_list_item, parent, false);
        }        
        TextView tv = (TextView) convertView.findViewById(R.id.route_list_tv);
        Log.v(TAG, "getView");
        
        // set the textview color based on the color stored in the routelist keys
        tv.setTextColor(routeList.keyAt(position));
        tv.setText("Route " + position);
        
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {            	         
                WallActivity mActivity = (WallActivity) getContext();
            	mActivity.setCurrentRouteColor(routeList.keyAt(position));                
            }
        });
             
        return convertView;
    }
}
