package com.arpitonline.worldclock;


import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CitySearchSuggestionProvider extends ContentProvider {
	
	public static final String AUTHORITY = "com.arpitonline.worldclock.citysearchsuggestionprovider";
	public static final Uri CONTENT_URI = 
        Uri.parse("content://"+AUTHORITY);
	
	private static final int GET_CITY = 1;
	private static final UriMatcher sURIMatcher = buildUriMatcher();
	
	@Override
	public boolean onCreate() {
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String query = uri.getLastPathSegment().toLowerCase();
		return getSuggestions(query);
	}
	
	 private Cursor getSuggestions(String query) {	
	      query = query.toLowerCase();
	      TimeZoneLookupService service = TimeZoneLookupService.getInstance(getContext());
	      return service.getCursorForQuery(query);
	    }
	

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private static UriMatcher buildUriMatcher(){
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(AUTHORITY, null, GET_CITY);
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, GET_CITY);
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY+"/*", GET_CITY);
		return matcher;
	}
	
	// WE DONT DO THESE ANYWAY:
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

}
