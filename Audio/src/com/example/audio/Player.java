package com.example.audio;

import java.io.File;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

public class Player extends ActionBarActivity implements OnClickListener{
	static MediaPlayer mp;
	ArrayList<File> mySongs;
	Uri u;
	int position;
	SeekBar sb;
	Button Play, FF,  FB, Nxt, Pv;
	Thread updateSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		Play = (Button) findViewById(R.id.Play);
		FB = (Button) findViewById(R.id.FB);
		FF = (Button) findViewById(R.id.FF);
		Pv = (Button) findViewById(R.id.Pv);
		Nxt = (Button) findViewById(R.id.Nxt);
		
		Play.setOnClickListener(this);
		FB.setOnClickListener(this);
		FF.setOnClickListener(this);
		Pv.setOnClickListener(this);
		Nxt.setOnClickListener(this);
		
		sb = (SeekBar) findViewById(R.id.seekBar1);
		
		updateSeekBar = new Thread(){
			public void run(){
				int totalDuration = mp.getDuration();
				int currentPosition = 0;
				//sb.setMax(totalDuracion);
				while(currentPosition < totalDuration){
					try{
						sleep(500);
						currentPosition = mp.getCurrentPosition();
						sb.setProgress(currentPosition);
						
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
				
		};
		
		if(mp != null){
			mp.stop();
			mp.release();
		    }
		
		Intent i = getIntent();
		Bundle b = i.getExtras();
		 mySongs = (ArrayList) b.getParcelableArrayList("songlist");
		position = b.getInt("pos", 0);
		
		 u = Uri.parse(mySongs.get(position).toString());
		mp = MediaPlayer.create(getApplicationContext(), u);
		
		mp.start();
		sb.setMax(mp.getDuration());
		
		updateSeekBar.start();
		
		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				mp.seekTo(seekBar.getProgress());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.Play:
			if (mp.isPlaying()) {
				Play.setText(">");
				mp.pause();
			} else{
				mp.start();
				Play.setText("||");
			}
			break;
		case R.id.FF:
		//R.id.FF:
			mp.seekTo(mp.getCurrentPosition() + 5000);
			break;
		case R.id.FB:
			mp.seekTo(mp.getCurrentPosition() - 5000);
			break;
		
		case R.id.Nxt:
			mp.stop();
			mp.release();
			position = (position + 1) % mySongs.size();
			u = Uri.parse(mySongs.get(position).toString());
			mp = MediaPlayer.create(getApplicationContext(), u);
			
			mp.start();
			sb.setMax(mp.getDuration());
			break;
		case R.id.Pv:
			mp.stop();
			mp.release();
			position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
			/*
			 * if(position -1 < 0){ position = mySongs.size() - 1; }else{
			 * position = position -1; }
			 */
			u = Uri.parse(mySongs.get(position).toString());
			mp = MediaPlayer.create(getApplicationContext(), u);
			
			mp.start();
			sb.setMax(mp.getDuration());
			break;
		}

		}
}

	
