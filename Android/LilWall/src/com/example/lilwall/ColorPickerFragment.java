/**
 * 
 */
package com.example.lilwall;

import com.larswerkman.holocolorpicker.ColorPicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * @author Grant
 *
 */
public class ColorPickerFragment extends DialogFragment {
	private LinearLayout mLayout;
	private int mColor;
	
	public int getmColor(){
		return mColor;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    mLayout = (LinearLayout)inflater.inflate(R.layout.color_picker, null);
	    
	    builder.setView(mLayout)
	    // Add action buttons
	           .setPositiveButton(R.string.choose_color, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the positive button event back to the host activity
                	   ColorPicker picker = (ColorPicker) mLayout.findViewById(R.id.picker);
                	   mColor = picker.getColor();
                       mListener.onDialogPositiveClick(ColorPickerFragment.this);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Send the negative button event back to the host activity
                       mListener.onDialogNegativeClick(ColorPickerFragment.this);
                   }
               });	               
	    return builder.create();
	}
	
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ColorPickerDialogListener {
        public void onDialogPositiveClick(ColorPickerFragment dialog);
        public void onDialogNegativeClick(ColorPickerFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    ColorPickerDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the listener so we can send events to the host
            mListener = (ColorPickerDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ColorPickerDialogListener");
        }
    }
}
