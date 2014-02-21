package com.example.lilwall.adapter;

import java.util.List;

import com.example.lilwall.R;
import com.example.lilwall.R.id;
import com.example.lilwall.R.layout;
import com.example.lilwall.model.WallObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class WallListAdapter extends ArrayAdapter<WallObject>{
	List<WallObject> wallList;
	Context context;
	
	public WallListAdapter(Context context, int textViewResourceId,List<WallObject> wallList) {
		super(context, textViewResourceId, wallList);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.wallList = wallList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// assign the view we are converting to a local variable
		View v = convertView;

        
        if (v == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            v = inflater.inflate(R.layout.wall_list_item, parent, false);
        }
        TextView tv = (TextView) v.findViewById(R.id.wall_list_tv);
		String name = (wallList.get(position)).getWallName();		
		tv.setText(name);
		
		// the view must be returned to our activity
		return v;

	}
}
