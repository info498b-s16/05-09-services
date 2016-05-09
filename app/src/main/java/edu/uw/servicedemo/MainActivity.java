package edu.uw.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "Main";

    private MusicService service;
    private boolean bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {

        bindService(new Intent(MainActivity.this, MusicService.class),
                this, Context.BIND_AUTO_CREATE);

        super.onStart();
    }

    @Override
    protected void onStop() {

        if(bound) {
            unbindService(this);
        }

        super.onStop();
    }

    //when "Start" button is pressed
    public void handleStart(View v){
        Log.i(TAG, "Start pressed");

        Intent intent = new Intent(MainActivity.this, CountingService.class);
        startService(intent);

    }

    //when "Stop" button is pressed
    public void handleStop(View v){
        Log.i(TAG, "Stop pressed");

        stopService(new Intent(MainActivity.this, CountingService.class));

    }


    private MediaPlayer player;

    /* Media controls */
    public void playMedia(View v){
        startService(new Intent(MainActivity.this, MusicService.class));
    }

    public void pauseMedia(View v){
        if(bound) {
            service.pause();
        }
    }

    public void stopMedia(View v){
//        stopService(new Intent(MainActivity.this, MusicService.class));
        if(bound){
            service.stop();
        }
    }

    //when "Quit" button is pressed
    public void handleQuit(View v){
        finish(); //end the Activity
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "Activity destroyed");
        super.onDestroy();
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder ibinder) {

        MusicService.LocalBinder binder = (MusicService.LocalBinder)ibinder;
        String songName = binder.getSong();

        ((TextView)findViewById(R.id.txtSongTitle)).setText(songName);

        service = binder.getService();
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }
}
