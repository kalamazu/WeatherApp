package com.liao.weatherapp.Playerfactory;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
public class MediaService extends Service {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_RESUME = "action_resume";
    public static final String ACTION_STOP = "action_stop";
    public static final String ACTION_SEEK = "action_seek";
    public static final String ACTION_UPDATE = "action_update";
    public static final String ACTION_FAST_FORWARD = "action_fast_forward";
    public static final String ACTION_SLOW_REWIND = "action_slow_rewind";
    public static final String ACTION_RESET_SPEED = "action_reset_speed";

    public static final String EXTRA_MEDIA_PATH = "extra_media_path";
    public static final String EXTRA_SEEK_POSITION = "extra_seek_position";
    public static final String EXTRA_CURRENT_POSITION = "extra_current_position";
    public static final String EXTRA_DURATION = "extra_duration";
    public static final String EXTRA_MEDIA_TITLE = "extra_media_title";
    public static final String EXTRA_PLAYBACK_SPEED = "extra_playback_speed";
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private float currentSpeed = 1.0f;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) return;

            switch (intent.getAction()) {
                case ACTION_PLAY:
                    String path = intent.getStringExtra(EXTRA_MEDIA_PATH);
                    playMedia(path);
                    break;
                case ACTION_PAUSE:
                    pauseMedia();
                    break;
                case ACTION_RESUME:
                    resumeMedia();
                    break;
                case ACTION_STOP:
                    stopMedia();
                    break;
                case ACTION_SEEK:
                    int position = intent.getIntExtra(EXTRA_SEEK_POSITION, 0);
                    seekTo(position);
                    break;
                case ACTION_FAST_FORWARD:
                    setPlaybackSpeed(2.0f);
                    break;
                case ACTION_SLOW_REWIND:
                    setPlaybackSpeed(0.5f);
                    break;
                case ACTION_RESET_SPEED:
                    setPlaybackSpeed(1.0f);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_RESUME);
        filter.addAction(ACTION_STOP);
        filter.addAction(ACTION_SEEK);
        filter.addAction(ACTION_FAST_FORWARD);
        filter.addAction(ACTION_SLOW_REWIND);
        filter.addAction(ACTION_RESET_SPEED);
        registerReceiver(receiver, filter);

        setupSeekBarUpdater();
    }

    private void setupSeekBarUpdater() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    Intent intent = new Intent(ACTION_UPDATE);
                    intent.putExtra(EXTRA_CURRENT_POSITION, mediaPlayer.getCurrentPosition());
                    intent.putExtra(EXTRA_DURATION, mediaPlayer.getDuration());
                    intent.putExtra(EXTRA_PLAYBACK_SPEED, currentSpeed);

                    sendBroadcast(intent);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateSeekBar);
    }

    private void playMedia(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

            setPlaybackSpeed(1.0f);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            retriever.release();

            // 发送更新广播
            sendUpdateBroadcast(title);

            mediaPlayer.setOnCompletionListener(mp -> {
                sendUpdateBroadcast(null);
            });
        } catch (IOException e) {
            Log.e("MediaService", "播放失败", e);
        }
    }

    private void pauseMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void resumeMedia() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        unregisterReceiver(receiver);
        handler.removeCallbacks(updateSeekBar);
    }
    private void sendUpdateBroadcast(String title) {
        Intent intent = new Intent(ACTION_UPDATE);
        intent.putExtra(EXTRA_CURRENT_POSITION, mediaPlayer.getCurrentPosition());
        intent.putExtra(EXTRA_DURATION, mediaPlayer.getDuration());
        intent.putExtra(EXTRA_MEDIA_TITLE, title != null ? title : "未知歌曲");
        intent.putExtra(EXTRA_PLAYBACK_SPEED, currentSpeed);
        sendBroadcast(intent);
    }
    private void setPlaybackSpeed(float speed) {
        if (mediaPlayer == null) return;

        // 检查API版本，变速需要API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
                currentSpeed = speed;
                sendUpdateBroadcast(null); // 更新UI
            } catch (IllegalStateException e) {
                Log.e("MediaService", "变速失败", e);
            }
        } else {
            Log.w("MediaService", "设备不支持变速功能(需要API 23+)");
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}