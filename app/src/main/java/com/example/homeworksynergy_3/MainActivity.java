package com.example.homeworksynergy_3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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
     * строка
     */
    SeekBar seekBar;
    private TextView seekBarHint;


    /**
     * Список названия песен
     */
    String[] arraySong = new String[]{"Asper X, Прозерпина", "Asper X, Шрам"};
    /**
     * Список номеров песен
     */
    int[] arraynumSong = new int[] {R.raw.firstmusic, R.raw.asperxshram};
    int song = 0;
    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBarHint = findViewById(R.id.seekBarHint);
        seekBar = findViewById(R.id.progress);
        namemusic = findViewById(R.id.musicname);
        namemusic.setText(arraySong[song]);
        player1 = MediaPlayer.create(this, arraynumSong[song]);



    }

    public void onResume() {
        super.onResume();
        play = findViewById(R.id.playbutton);
        new Thread(() ->{
            play.setOnClickListener(listener -> {
                player1 = MediaPlayer.create(this, arraynumSong[song]);
                if(!player1.isPlaying()){player1.start();}
            });
        }).start();
        seekBar.setMax(player1.getDuration());
        stop();
        next();
        previous();
        new Thread(() ->{
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBarHint.setVisibility(View.VISIBLE); // установление видимости seekBarHint
                    //seekBarHint.setVisibility(View.INVISIBLE); // установление не видимости seekBarHint

                    // Math.ceil() - округление до целого в большую сторону
                    int timeTrack = (int) Math.ceil(progress/1000f); // перевод времени из миллисекунд в секунды

                    // вывод на экран времени отсчёта трека
                    if (timeTrack < 10) {
                        seekBarHint.setText("00:0" + timeTrack);
                    } else if (timeTrack < 60){
                        seekBarHint.setText("00:" + timeTrack);
                    } else if (timeTrack >= 60) {
                        seekBarHint.setText("01:" + (timeTrack - 60));
                    }
                    double percentTrack = progress / (double) seekBar.getMax(); // получение процента проигранного трека (проигранное время делится на длину трека)
                    // seekBar.getX() - начало seekBar по оси Х
                    // seekBar.getWidth() - ширина контейнера seekBar
                    // 0.92 - поправочный коэффициент (так как seekBar занимает не всю ширину своего контейнера)
                    seekBarHint.setX(seekBar.getX() + Math.round(seekBar.getWidth()*percentTrack*0.92));
                    if (progress > 0 && player1 != null && !player1.isPlaying()) { // если mediaPlayer не пустой и mediaPlayer не воспроизводится
                        stop(); // остановка и очиска MediaPlayer
                        // назначение кнопке картинки play

                        MainActivity.this.seekBar.setProgress(0); // установление seekBar значения 0
                    }


                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (player1.isPlaying()){stop();}
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    player1.seekTo(seekBar.getProgress());
                }




            });


        }).start();
        new Thread(()->{
            while(true){
                while(player1.isPlaying()){
                    seekbarupdate();
                }
            }

        }
                ).start();
    }
    public void next(){
        if (!player1.isPlaying()){
            next = findViewById(R.id.next);
            namemusic = findViewById(R.id.musicname);
            next.setOnClickListener(listener -> {
                song = song + 1;
                namemusic.setText(arraySong[song]);
                player1.stop();
            });
        }
    }
    public void previous(){
        if (!player1.isPlaying()){
            previous = findViewById(R.id.previous);
            namemusic = findViewById(R.id.musicname);
            previous.setOnClickListener(listener -> {
                song = song - 1;
                namemusic.setText(arraySong[song]);
                player1.stop();
            });
        }
    }
    public void stop(){
        stop = findViewById(R.id.stop);
        stop.setOnClickListener(listener -> {
            if(player1.isPlaying()) {
                player1.stop(); // остановка медиа
                player1.release(); // освобождение ресурсов
                player1 = null; // обнуление mediaPlayer
            }
        });
    }
    public void seekbarupdate(){
        seekBar.setProgress(player1.getCurrentPosition());
    }


}