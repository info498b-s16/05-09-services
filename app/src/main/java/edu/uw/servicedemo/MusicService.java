package edu.uw.servicedemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by class on 5/9/16.
 */
public class MusicService extends Service {

    private static final String TAG = "Music";

    @Override
    public void onCreate() {
        Log.v(TAG, "Service started");
        super.onCreate();
    }

    private MediaPlayer player;

    private String songName = "The Entertainer";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(player == null) {
            player = MediaPlayer.create(this, R.raw.scott_joplin_the_entertainer_1902);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    MusicService.this.stopSelf();
                }
            });
        }
        player.start();


        //make foreground service
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,
                new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("Music Player")
                .setContentText("Now playing: "+songName)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        startForeground(1, notification); //show notification

        return START_NOT_STICKY;
    }

    public void pause() {
        if(player != null) {
            player.pause();
        }
    }

    public void stop() {
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }


    private final IBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder{

        //public interface to be called
        public String getSong(){
            return songName;
        }

        public MusicService getService() {
            return MusicService.this; //get access to the service to call its methods
        }
    }

}
