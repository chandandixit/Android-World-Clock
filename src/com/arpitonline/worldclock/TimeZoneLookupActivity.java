package com.arpitonline.worldclock;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

import com.arpitonline.worldclock.models.LocationVO;
import com.example.android.apis.R;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class TimeZoneLookupActivity extends ListActivity {
	
	private TimeZoneLookupService lookup;
	private ArrayList<LocationVO> searchResults = new ArrayList<LocationVO>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.search);
	    Log.i(WorldClock.WORLD_CLOCK, "------beginning search-----"); 
	    
	    setListAdapter(new CountriesAdapter(this, R.layout.search_result_item, searchResults,R.layout.search_result_item));
	    
	    TimeZoneLookupService.DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.arpitonline.worldclock/";
		lookup = new TimeZoneLookupService(this);
		  
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      lookupTimeZone(query);
	    }
	    
	    ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				LocationVO location = searchResults.get(position);
				TimeZone tz = location.getTimeZone();
				
				if(tz==null){
					Toast.makeText(view.getContext(), "Timezone not found", Toast.LENGTH_SHORT).show();
				}
				else{
					DateTimeZone dtz = DateTimeZone.forID(tz.getID());
					DateTime dt = new DateTime();
					DateTime current = dt.withZone(dtz);
					
					LocalTime current2 = current.toLocalTime();
//					
					String out = "";
					out += "Time: "+current2.getHourOfDay()+":"+current2.getMinuteOfHour();
					out+="\ntimezone: "+dtz.getID();
					out+="\nDaylight Savings? "+dtz.toTimeZone().inDaylightTime(new Date());
					
					Toast.makeText(view.getContext(), out, Toast.LENGTH_LONG).show();
				}
				
//		    	
			}
		});
	}
	
	private void lookupTimeZone(String s){
		ArrayList<LocationVO> alist = lookup.getTimeZoneForCity(s);
		if(alist == null){
			Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
			return;
		}
		searchResults.addAll(alist);
	}
}
