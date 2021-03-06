package com.arpitonline.worldclock;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.arpitonline.worldclock.models.LocationVO;

public class MyLocationsFragment extends SherlockListFragment {

	private MyLocationsDataAdapter adapter;
	private Timer t;
	private Handler handler;
	private ListView lv;
	
	private String[] menuItems = new String[]{"Remove"};
	
	public static final String TAG = "MyLocationsFragment";
	
	final Runnable doUpdateView = new Runnable() {
		public void run() {
			adapter.notifyDataSetChanged();
		}
	};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		lv = getListView();

		lv.setTextFilterEnabled(true);
		
		TimelyPiece app = (TimelyPiece) getActivity().getApplication();
		
		ArrayList<LocationVO> locations = app.getMyLocations();
		
		
		
		
		
		adapter = new MyLocationsDataAdapter(
				(this.getActivity()),
				R.layout.world_list_item, locations, R.layout.world_list_item);
		setListAdapter(adapter);

		// if(locations.size() == 0){
		// if(introDialog == null){
		// //, android.R.style.Theme_Translucent_NoTitleBar
		// introDialog = new Dialog(this, android.R.style.Theme_Panel);
		// introDialog.setContentView(R.layout.intro);
		//
		// Window window = introDialog.getWindow();
		// window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
		// WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		// window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		// //window.setGravity(Gravity.BOTTOM);
		//
		//
		// }
		// introDialog.show();
		// }
		// else{
		// if(introDialog != null){
		// introDialog.dismiss();
		// introDialog = null;
		// }
		// }

//		lv.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//			}
//		});
//
		registerForContextMenu(lv);
		handler = new Handler();
		createUpdateTimer();
	}
	
	public void clearLocations(){
		adapter.clear();
    	((TimelyPiece)getActivity().getApplication()).savePreferences();
	}

	private void createUpdateTimer() {
		TimerTask updateTimerTask = new TimerTask() {
			public void run() {
				handler.post(doUpdateView);
			}
		};

		t = new Timer();
		t.scheduleAtFixedRate(updateTimerTask, 60000, 60000);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (t == null) {
			try {
				createUpdateTimer();
			} catch (Exception e) {
				Log.e(TimelyPiece.WORLD_CLOCK, e.getMessage());
			}
		}
	}

	@Override
	public void onStop() {
		t.cancel();
		t = null;
		super.onStop();

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v == this.getListView()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			ArrayList<LocationVO>locations = ((TimelyPiece)getActivity().getApplication()).getMyLocations();
			Log.d(TAG, "=> locations size: "+locations.size()+", index: "+info.position);
			LocationVO loc = locations.get(info.position);
			menu.setHeaderTitle(loc.cityName);
			for(int i=0; i<menuItems.length; i++){
		    	menu.add(Menu.NONE, i, i, menuItems[i]);
		    }
		}
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		if(menuItemIndex==0){
			TimelyPiece app = ((TimelyPiece)getActivity().getApplication());
			LocationVO loc = app.getMyLocations().get(info.position);
			 app.removeLocation(loc);
			 adapter.removeFromAnimatedObjects(loc);
			 adapter.notifyDataSetChanged();
		}
		return true;
	}

}
