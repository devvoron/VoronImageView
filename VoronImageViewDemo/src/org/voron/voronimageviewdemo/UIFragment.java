package org.voron.voronimageviewdemo;

import org.voron.voronimageview.EventObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class UIFragment extends Fragment{
	
	private TextView tView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_buttons, container, false);
		
		//Buttons
		Button bTranslateLeft = (Button)view.findViewById(R.id.tLeft);
        bTranslateLeft.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doTranslateLeft");				
			}
		});
        Button bTranslateRight = (Button)view.findViewById(R.id.tRight);
        bTranslateRight.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doTranslateRight");			
			}
		});
        Button bTranslateUp = (Button)view.findViewById(R.id.tUp);
        bTranslateUp.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doTranslateUp");				
			}
		});
        Button bTranslateDown = (Button)view.findViewById(R.id.tDown);
        bTranslateDown.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doTranslateDown");				
			}
		});        
        Button bZoomIn = (Button)view.findViewById(R.id.zIn);
        bZoomIn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doZoomIn");				
			}
		});
        Button bZoomOut = (Button)view.findViewById(R.id.zOut);
        bZoomOut.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doZoomOut");				
			}
		});
        Button bRotateLeft = (Button)view.findViewById(R.id.rLeft);
        bRotateLeft.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doRotateLeft");			
			}
		});
        Button bRotateRight = (Button)view.findViewById(R.id.rRight);
        bRotateRight.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onSelectCommand("doRotateRight");	
			}
		});
        
        //Text View
        tView = (TextView)view.findViewById(R.id.layoutTextView);
		
		return view;
	}
	
	private void onSelectCommand(String command){
		IListener listener = (IListener)getActivity();
		listener.onSelectViewCommand(command);
	}
	
	public void updateMatrixValue(EventObject e){
		tView.setText("x: "+String.format("%.02f", e.getX())+"; y: "+String.format("%.02f", e.getY())+"; angle: "+e.getRotateAngle()+"; scale: "+String.format("%.02f", e.getScaleFactor()));
	}
	
	
}
