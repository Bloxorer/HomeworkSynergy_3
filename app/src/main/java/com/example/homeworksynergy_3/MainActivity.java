package com.example.homeworksynergy_3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    Button stop;
    private final String DATA_STREAM = "http://ep128.hostingradio.ru:8030/ep128";
    Button radioplayer;
    Button next;

    Button previous;
    /**
     * медиа плеер
     */
    public MediaPlayer player1;
    /**
     * кнопка старт
     */
    public Button play;

    /**
     * название песни
     */
    public TextView namemusic;

    /**
     * Список названия песен
     */
    String[] arraySong = new String[]{"AsperX, Прозерпина", "AsperX, Шрам"};
    /**
     * Список номеров песен
     */
    int[] arraynumSong = new int[] {R.raw.firstmusic, R.raw.asperxshram};
    int song = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namemusic = findViewById(R.id.musicname);
        namemusic.setText(arraySong[song]);
        try {
            loop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void next(){
        next = findViewById(R.id.next);
        namemusic = findViewById(R.id.musicname);
        next.setOnClickListener(listener -> {
            song = song + 1;
            namemusic.setText(arraySong[song]);
        });
    }
    public void previous(){
        previous = findViewById(R.id.previous);
        namemusic = findViewById(R.id.musicname);
        previous.setOnClickListener(listener -> {
            song = song - 1;
            namemusic.setText(arraySong[song]);
        });
    }


    public void button() {
        play = findViewById(R.id.playbutton);
        play.setOnClickListener(listener -> {
            player1 = MediaPlayer.create(this, arraynumSong[song]);
            player1.start();

        });
        stop = findViewById(R.id.stop);
        stop.setOnClickListener(listener -> {
            player1 = MediaPlayer.create(this, arraynumSong[song]);
            player1.stop();

        });

    }
    public void radio() throws IOException {
        radioplayer = findViewById(R.id.Radio);
        radioplayer.setOnClickListener(listener ->{
            player1 = new MediaPlayer(); // создание объекта медиа-плеера
            try {
                player1.setDataSource(DATA_STREAM); // указание источника аудио
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            player1.setAudioStreamType(AudioManager.STREAM_MUSIC); // задает аудио-поток, который будет использован для проигрывания
            player1.setOnPreparedListener(this); // ассинхронная подготовка плеера к проигрыванию
            player1.prepareAsync(); // ассинхронная подготовка плейера к проигрыванию
        });



    }
    public void loop() throws IOException {
            button();
            next();
            previous();
            radio();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}