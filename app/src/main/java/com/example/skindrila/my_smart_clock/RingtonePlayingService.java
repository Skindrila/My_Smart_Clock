package com.example.skindrila.my_smart_clock;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.skindrila.my_smart_clock.R;

public class RingtonePlayingService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isRinging = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(isRinging == false) {
            Bundle data = intent.getExtras();
            String tone = data.getString("Tone");
            String Qs = data.getString("Qs");

            switch (tone)
            {
                case "Rock":
                    mediaPlayer = MediaPlayer.create(this, R.raw.ringtone1);
                    break;
                case "Hybrid":
                    mediaPlayer = MediaPlayer.create(this, R.raw.ringtone2);
                    break;
                case "Nature":
                    mediaPlayer = MediaPlayer.create(this, R.raw.ringtone3);
                    break;
                default:
                    mediaPlayer = MediaPlayer.create(this, R.raw.ringtone4);
                    break;

            }
            mediaPlayer.setLooping(true);

            Intent intent1 = new Intent(this, SetTaskActivity.class);
            intent1.putExtra("Qs",Qs);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            isRinging = true;
            mediaPlayer.start();
        }

        else {
            isRinging = false;
            mediaPlayer.stop();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
