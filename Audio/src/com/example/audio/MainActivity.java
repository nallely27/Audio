package com.example.audio;

import java.io.File;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    ListView lv;
    String[] items;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.lvPlaylist);
		
		final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
		items = new String [ mySongs.size()];
		
		for(int i = 0; i < mySongs.size(); i ++){
			
			//toast(mySongs.get(i).getName().toString());
			items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
		}
		
		ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.mysong_layout, R.id.textView1, items);
		lv.setAdapter(adp);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", position).putExtra("songlist", mySongs));
				
			}
		});
	}
	
    public ArrayList<File> findSongs(File root){
    	ArrayList<File> al = new ArrayList<File>();
		File[] files = root.listFiles();
		for(File singleFile : files){
			if(singleFile.isDirectory() && !singleFile.isHidden()){
				al.addAll(findSongs(singleFile));
			}
			else{
				if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
					al.add(singleFile);
				}
			}
		}
		return al;
	}
    
    public void toast(String text){
    	Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    

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
