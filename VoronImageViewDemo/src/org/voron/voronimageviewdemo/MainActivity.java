package org.voron.voronimageviewdemo;

import org.voron.voronimageview.EventObject;
import org.voron.voronimageview.VoronImageView;
import org.voron.voronimageview.OnMatrixChangeListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity implements IListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	}	
	//fragment communication listeners
	@Override
	public void onSelectViewCommand(String doCommand) {
		// TODO Auto-generated method stub
		FragmentManager fragmentManager = getFragmentManager();
		VoronImageViewFragment viewFragment = (VoronImageViewFragment)fragmentManager.findFragmentById(R.id.fVoronView);
		if(viewFragment != null){
			viewFragment.doCommand(doCommand);
		}
	}

	@Override
	public void onUpdateMatrixValue(EventObject e) {
		// TODO Auto-generated method stub
		FragmentManager fragmentManager = getFragmentManager();
		UIFragment uiFragment = (UIFragment)fragmentManager.findFragmentById(R.id.fUI);
		if(uiFragment!=null){
			uiFragment.updateMatrixValue(e);
		}
	}
	
	
	//Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
