package org.voron.voronimageviewdemo;

import org.voron.voronimageview.EventObject;
import org.voron.voronimageview.OnMatrixChangeListener;
import org.voron.voronimageview.VoronImageView;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class VoronImageViewFragment extends Fragment{
	
	private VoronImageView vImageView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_voron_view, container, false);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paris);
		vImageView = (VoronImageView)view.findViewById(R.id.vImageView);
		vImageView.setImageBitmap(bitmap);
		vImageView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("click", "click");
			}
		});
		
		int orientation = getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_PORTRAIT){
			vImageView.setOnMatrixChangeListener(new OnMatrixChangeListener() {			
				@Override
				public void onEvent(EventObject e) {
					// TODO Auto-generated method stub
					IListener listener = (IListener)getActivity();
					listener.onUpdateMatrixValue(e);
				}
			});
		}
		
				
		return view;
	}
	
	public void doCommand(String command){
		if(command.equalsIgnoreCase("doTranslateLeft")){
			vImageView.doTranslateLeft(5);
		}else if(command.equalsIgnoreCase("doTranslateRight")){
			vImageView.doTranslateRight(5);
		}else if(command.equalsIgnoreCase("doTranslateUp")){
			vImageView.doTranslateUp(5);
		}else if(command.equalsIgnoreCase("doTranslateDown")){
			vImageView.doTranslateDown(5);
		}else if(command.equalsIgnoreCase("doZoomIn")){
			vImageView.doZoomIn(1);
		}else if(command.equalsIgnoreCase("doZoomOut")){
			vImageView.doZoomOut(1);
		}else if(command.equalsIgnoreCase("doRotateLeft")){
			vImageView.doRotateLeft();
		}else if(command.equalsIgnoreCase("doRotateRight")){
			vImageView.doRotateRight();
		}
	}
	
}
