package com.liao.weatherapp.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liao.weatherapp.Playerfactory.MediaAdapter;
import com.liao.weatherapp.Playerfactory.MediaFile;
import com.liao.weatherapp.Playerfactory.MediaService;
import com.liao.weatherapp.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 100;

    private RecyclerView recyclerView;
    private SeekBar seekBar;
    private TextView tvCurrentSong;
    private Button btnPlayPause, btnStop, btnNext, btnPrevious,movefast,moveslow;

    private List<MediaFile> mediaList;
    private MediaAdapter adapter;
    private Intent serviceIntent;

    private boolean isPlaying = false;
    private boolean isSpeedUp = false;

    private int currentPosition = 0;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // 初始化数据列表
        mediaList = new ArrayList<>();

        // 初始化视图
        initViews();

        // 初始化适配器
        initAdapter();

        // 初始化服务
        initService();

        // 检查权限
        checkPermissions();
    }
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        seekBar = findViewById(R.id.seekBar);

        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnStop = findViewById(R.id.btnStop);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        movefast=findViewById(R.id.movefast);
        moveslow=findViewById(R.id.moveslow);

        btnPlayPause.setOnClickListener(v -> togglePlayPause());
        btnStop.setOnClickListener(v -> stopMedia());
        btnNext.setOnClickListener(v -> playNext());
        btnPrevious.setOnClickListener(v -> playPrevious());
        movefast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fastForward();
                        return true;
                    case MotionEvent.ACTION_UP:
                        resetSpeed();
                        return true;
                }
                return false;
            }
        });

        moveslow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        slowRewind();
                        return true;
                    case MotionEvent.ACTION_UP:
                        resetSpeed();
                        return true;
                }
                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Intent intent = new Intent(MediaService.ACTION_SEEK);
                    intent.putExtra(MediaService.EXTRA_SEEK_POSITION, progress);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    private void initAdapter() {
        adapter = new MediaAdapter(mediaList, position -> {
            currentPosition = position;
            playMedia(mediaList.get(position).getPath());
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initService() {
        serviceIntent = new Intent(this, MediaService.class);
        startService(serviceIntent);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        } else {
            loadMediaFiles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMediaFiles();
            } else {
                Toast.makeText(this, "需要存储权限才能扫描音乐文件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadMediaFiles() {
        if (adapter == null) {
            Log.e("TAG", "Adapter is null when trying to load media files");
            return;
        }
        mediaList.clear();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        try (Cursor cursor = getContentResolver().query(uri, projection, selection, null, sortOrder)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String title = cursor.getString(1);
                    String artist = cursor.getString(2);
                    long duration = cursor.getLong(3);
                    String path = cursor.getString(4);

                    mediaList.add(new MediaFile(id, title, artist, duration, path));
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "没有找到音乐文件", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("TAG", "Error loading media files", e);
            Toast.makeText(this, "加载音乐文件出错", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
    private void fastForward() {
        Intent intent = new Intent(MediaService.ACTION_FAST_FORWARD);
        sendBroadcast(intent);
        Toast.makeText(this, "2倍速播放", Toast.LENGTH_SHORT).show();
    }

    private void slowRewind() {
        Intent intent = new Intent(MediaService.ACTION_SLOW_REWIND);
        sendBroadcast(intent);
        Toast.makeText(this, "0.5倍速播放", Toast.LENGTH_SHORT).show();
    }

    private void resetSpeed() {
        Intent intent = new Intent(MediaService.ACTION_RESET_SPEED);
        sendBroadcast(intent);
    }
    private void playMedia(String path) {
        if (path == null || path.isEmpty()) {
            Toast.makeText(this, "无效的音乐文件路径", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MediaService.ACTION_PLAY);
        intent.putExtra(MediaService.EXTRA_MEDIA_PATH, path);
        sendBroadcast(intent);
        isPlaying = true;
        btnPlayPause.setText("暂停");
    }

    private void togglePlayPause() {
        if (isPlaying) {
            pauseMedia();
        } else {
            resumeMedia();
        }
    }

    private void pauseMedia() {
        Intent intent = new Intent(MediaService.ACTION_PAUSE);
        sendBroadcast(intent);
        isPlaying = false;
        btnPlayPause.setText("播放");
    }

    private void resumeMedia() {
        if (mediaList.isEmpty()) {
            Toast.makeText(this, "没有可播放的音乐", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPosition >= 0 && currentPosition < mediaList.size()) {
            playMedia(mediaList.get(currentPosition).getPath());
        }
    }

    private void stopMedia() {
        Intent intent = new Intent(MediaService.ACTION_STOP);
        sendBroadcast(intent);
       finish();
    }

    private void playNext() {
        if (mediaList.isEmpty()) {
            Toast.makeText(this, "没有可播放的音乐", Toast.LENGTH_SHORT).show();
            return;
        }

        currentPosition = (currentPosition + 1) % mediaList.size();
        playMedia(mediaList.get(currentPosition).getPath());
    }

    private void playPrevious() {
        if (mediaList.isEmpty()) {
            Toast.makeText(this, "没有可播放的音乐", Toast.LENGTH_SHORT).show();
            return;
        }

        currentPosition = (currentPosition - 1 + mediaList.size()) % mediaList.size();
        playMedia(mediaList.get(currentPosition).getPath());
    }


    private void updateUI(Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        if (MediaService.ACTION_UPDATE.equals(action)) {
            int current = intent.getIntExtra(MediaService.EXTRA_CURRENT_POSITION, 0);
            int duration = intent.getIntExtra(MediaService.EXTRA_DURATION, 0);
            String title = intent.getStringExtra(MediaService.EXTRA_MEDIA_TITLE);

            if (seekBar != null) {
                seekBar.setMax(duration);
                seekBar.setProgress(current);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MediaService.ACTION_UPDATE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            Log.w("TAG", "Receiver not registered", e);
        }
    }

    @Override
    protected void onDestroy() {
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        super.onDestroy();

    }

}