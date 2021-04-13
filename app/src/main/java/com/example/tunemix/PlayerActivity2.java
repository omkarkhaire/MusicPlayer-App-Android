package com.example.tunemix;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.tunemix.MainActivity.musicfiles;
import static com.example.tunemix.MainActivity.repeatbtn;

import static com.example.tunemix.MainActivity.shufflebtn;
import static com.example.tunemix.albumdetailadapter.albumfiles;

public class PlayerActivity2 extends AppCompatActivity implements MediaPlayer.OnCompletionListener,PlayerAction, ServiceConnection {

      TextView songname,artist,rightnum,leftnum;
      ImageView back,menu,shuffal,repeat,prev,next,coverart;
      FloatingActionButton playpause;
      SeekBar seekBar;
      int position=-1;
      static MediaPlayer mediaPlayer;
      public static Uri uri;
      private Handler handler=new Handler();//ui delays if occur
      public static  ArrayList<Musicfiles> list=new ArrayList<>();
      int n;
      private Thread playthread,prevthread,nextthread;
      MusicService musicService;

    public void back(View v)
    {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        Intent intent=new Intent(this,MusicService.class);//notification
        bindService(intent,this,BIND_AUTO_CREATE);
        playthreadbtn();
        nextthreadbtn();
        prevthreadbtn();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unbindService(this);
    }

    private void nextthreadbtn()
    {
        nextthread=new Thread()
        {
            @Override
            public void run() {
                super.run();
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       nextbtnclicked();
                    }
                });
            }
        };
        nextthread.start();
    }


    private void playthreadbtn()
    {
        playthread=new Thread()
        {
            @Override
            public void run() {
                super.run();
                playpause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playpausebtnclicked();
                    }
                });
            }
        };
        playthread.start();
    }

    public void playpausebtnclicked() {
        if(mediaPlayer.isPlaying())
        {
            playpause.setImageResource(R.drawable.ic_play_arrow_24);
            mediaPlayer.pause();


        }
        else{
            mediaPlayer.start();
            playpause.setImageResource(R.drawable.ic_pause_24);
       }
    }

    private void prevthreadbtn() {
        prevthread=new Thread()
        {
            @Override
            public void run() {
                super.run();
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                           prevbtnclicked();
                    }
                });
            }
        };
        prevthread.start();
    }

    public void prevbtnclicked() {

        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();//relesing the resources
            if(shufflebtn && !repeatbtn) //s on r off then
            {
                position=getrandom(list.size()-1);
            }
            else if(!shufflebtn && !repeatbtn)
            {
                position=((position-1)<0)? list.size()-1:position-1;
            }

            uri=Uri.parse(list.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            songname.setText(list.get(position).getTitle());
            artist.setText(list.get(position).getArtist());
            int totallength=mediaPlayer.getDuration()/1000;
            int minutes=totallength/60;
            String remseconds=Integer.toString(totallength%60);
            if(remseconds.length()==1)
            {
                remseconds+="0";
            }
            rightnum.setText(""+minutes+":"+remseconds);
            PlayerActivity2.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int curr=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(curr);
                        leftnum.setText(formattedtime(curr));

                    }
                    handler.postDelayed(this,1000);

                }

                private String formattedtime(int curr) {
                    String totalout,totalnew;
                    String seconds=""+curr%60;
                    String minutes=""+curr/60;
                    totalout=minutes+":"+seconds;
                    totalnew=minutes+":"+"0"+seconds;
                    if(seconds.length()==1)
                    {
                        return totalnew;
                    }
                    return totalout;

                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playpause.setBackgroundResource(R.drawable.ic_pause_24);
            mediaPlayer.start();

        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();//relesing the resources
            if(shufflebtn && !repeatbtn) //s on r off then
            {
                position=getrandom(list.size()-1);
            }
            else if(!shufflebtn && !repeatbtn)
            {
                position=((position+1)% list.size());
            }

            uri=Uri.parse(list.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            songname.setText(list.get(position).getTitle());
            artist.setText(list.get(position).getArtist());
            int totallength=mediaPlayer.getDuration()/1000;
            int minutes=totallength/60;
            String remseconds=Integer.toString(totallength%60);
            if(remseconds.length()==1)
            {
                remseconds+="0";
            }
            rightnum.setText(""+minutes+":"+remseconds);
            PlayerActivity2.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int curr=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(curr);
                        leftnum.setText(formattedtime(curr));

                    }
                    handler.postDelayed(this,1000);

                }

                private String formattedtime(int curr) {
                    String totalout,totalnew;
                    String seconds=""+curr%60;
                    String minutes=""+curr/60;
                    totalout=minutes+":"+seconds;
                    totalnew=minutes+":"+"0"+seconds;
                    if(seconds.length()==1)
                    {
                        return totalnew;
                    }
                    return totalout;

                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playpause.setBackgroundResource(R.drawable.ic_play_arrow_24);

        }

    }
  public void nextbtnclicked()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();//relesing the resources
            if(shufflebtn && !repeatbtn) //s on r off then
            {
                position=getrandom(list.size()-1);
            }
            else if(!shufflebtn && !repeatbtn)
            {
                position=((position+1)% list.size());
            }

            uri=Uri.parse(list.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            songname.setText(list.get(position).getTitle());
            artist.setText(list.get(position).getArtist());
            int totallength=mediaPlayer.getDuration()/1000;
            int minutes=totallength/60;
            String remseconds=Integer.toString(totallength%60);
            if(remseconds.length()==1)
            {
                remseconds+="0";
            }
            rightnum.setText(""+minutes+":"+remseconds);
            PlayerActivity2.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int curr=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(curr);
                        leftnum.setText(formattedtime(curr));

                    }
                    handler.postDelayed(this,1000);

                }

                private String formattedtime(int curr) {
                    String totalout,totalnew;
                    String seconds=""+curr%60;
                    String minutes=""+curr/60;
                    totalout=minutes+":"+seconds;
                    totalnew=minutes+":"+"0"+seconds;
                    if(seconds.length()==1)
                    {
                        return totalnew;
                    }
                    return totalout;

                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playpause.setBackgroundResource(R.drawable.ic_pause_24);
            mediaPlayer.start();

        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();//relesaing the resources
            if(shufflebtn && !repeatbtn) //s on r off then
            {
                position=getrandom(list.size()-1);
            }
            else if(!shufflebtn && !repeatbtn)
            {
                position=((position+1)% list.size());
            }
            uri=Uri.parse(list.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            songname.setText(list.get(position).getTitle());
            artist.setText(list.get(position).getArtist());
            int totallength=mediaPlayer.getDuration()/1000;
            int minutes=totallength/60;
            String remseconds=Integer.toString(totallength%60);
            if(remseconds.length()==1)
            {
                remseconds+="0";
            }
            rightnum.setText(""+minutes+":"+remseconds);
            PlayerActivity2.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int curr=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(curr);
                        leftnum.setText(formattedtime(curr));

                    }
                    handler.postDelayed(this,1000);

                }

                private String formattedtime(int curr) {
                    String totalout,totalnew;
                    String seconds=""+curr%60;
                    String minutes=""+curr/60;
                    totalout=minutes+":"+seconds;
                    totalnew=minutes+":"+"0"+seconds;
                    if(seconds.length()==1)
                    {
                        return totalnew;
                    }
                    return totalout;

                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playpause.setBackgroundResource(R.drawable.ic_play_arrow_24);
            mediaPlayer.start();

        }
    }

    private int getrandom(int i)
    {
        Random num=new Random();
        int rnum=num.nextInt(i+1);
        return rnum;
    }

//*******************************************************************oncreate behaviour(method)***********************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);

          n=list.size();//size of music file array

        songname=(TextView)findViewById(R.id.songname);
        artist=(TextView)findViewById(R.id.artist);
        rightnum=(TextView)findViewById(R.id.rightnum);
        leftnum=(TextView)findViewById(R.id.leftnum);

        back=(ImageView)findViewById(R.id.backbtn);
        menu=(ImageView)findViewById(R.id.menubtn);
        shuffal=(ImageView)findViewById(R.id.shuffal);
        repeat=(ImageView)findViewById(R.id.repeat);
        prev=(ImageView)findViewById(R.id.previous);
        next=(ImageView)findViewById(R.id.next);
        coverart=(ImageView)findViewById(R.id.cover_art);
        playpause=findViewById(R.id.playpaues);

        seekBar=(SeekBar)findViewById(R.id.seekbar);

        Intent intent=getIntent();
        position=intent.getIntExtra("position",-1);
        String sender=intent.getStringExtra("sender");//from album activity

        if(sender!=null && sender.equals("albumdetails"))
        {
            //from album activity
            list=albumfiles;
        }
        else{
            //from songs activity
            list=musicfiles;
        }

        if(list!=null)
        {
            playpause.setImageResource(R.drawable.ic_pause_24);
            uri= Uri.parse(list.get(position).getPath());// list of object
            //uri initialized
        }
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else{
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
            seekBar.setMax(mediaPlayer.getDuration()/1000);//getduration in ms so divide by 1000 for s
            metadata(uri);//setting image
            songname.setText(list.get(position).getTitle());
             artist.setText(list.get(position).getArtist());
          int totallength=mediaPlayer.getDuration()/1000;
          int minutes=totallength/60;
          String remseconds=Integer.toString(totallength%60);
          if(remseconds.length()==1)
          {
              remseconds+="0";
          }
          rightnum.setText(""+minutes+":"+remseconds);
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b)
                {
                    mediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity2.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null)
                {
                    int curr=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(curr);
                    leftnum.setText(formattedtime(curr));

                }
                handler.postDelayed(this,1000);

            }

            private String formattedtime(int curr) {
                String totalout,totalnew;
                String seconds=""+curr%60;
                String minutes=""+curr/60;
                totalout=minutes+":"+seconds;
                totalnew=minutes+":"+"0"+seconds;
                if(seconds.length()==1)
                {
                    return totalnew;
                }
                return totalout;

            }
        });

        shuffal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               if(shufflebtn)
               {
                   shufflebtn=false;
//                   off
                   Toast.makeText(PlayerActivity2.this, "Shuffle off", Toast.LENGTH_SHORT).show();
                   shuffal.setImageResource(R.drawable.ic_baseline_shuffle_24);
               }
               else{
                   shufflebtn=true;
//                   on
                   Toast.makeText(PlayerActivity2.this, "Shuffle on", Toast.LENGTH_SHORT).show();

                   shuffal.setImageResource(R.drawable.ic_baseline_shuffle_on);


               }
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeatbtn)
                {
                    repeatbtn=false;
                   //off
                    Toast.makeText(PlayerActivity2.this, "Repeat off", Toast.LENGTH_SHORT).show();
                    repeat.setImageResource(R.drawable.ic_baseline_repeat_one_24);

                }else{
                    repeatbtn=true;
                   //on
                    Toast.makeText(PlayerActivity2.this, "Repeat on", Toast.LENGTH_SHORT).show();
                    repeat.setImageResource(R.drawable.ic_baseline_repeat_on);
                }

            }
        });
    }

    private void metadata(Uri uri)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art=retriever.getEmbeddedPicture();
        Bitmap bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
        if(art!=null)
        {
//            Glide.with(this).asBitmap().load(art).into(coverart);
            bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
            Imageanimation(this,coverart,bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener()
            {  //all code to change color like music_art
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch=palette.getDominantSwatch();
                    if(swatch!=null)
                    {
                        ImageView gradient=findViewById(R.id.imageViewgradent);
                        RelativeLayout relativeLayout=findViewById(R.id.mcontainer);
                        RelativeLayout topbar=findViewById(R.id.layouttopbutton);

                        gradient.setBackgroundResource(R.drawable.backgradient);
                        relativeLayout.setBackgroundResource(R.drawable.main_bg);
                        topbar.setBackgroundResource(R.drawable.main_bg);

                        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{
                                swatch.getRgb(),0x00000000});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawablebg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{
                                swatch.getRgb(),0x00000000});

                        relativeLayout.setBackground(gradientDrawablebg);



                        songname.setTextColor(swatch.getTitleTextColor());
                        artist.setTextColor(swatch.getBodyTextColor());

                    }
                    else{
                        ImageView gradient=findViewById(R.id.imageViewgradent);
                        RelativeLayout relativeLayout=findViewById(R.id.mcontainer);

                        gradient.setBackgroundResource(R.drawable.backgradient);
                        relativeLayout.setBackgroundResource(R.drawable.main_bg);

                        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{
                                0xff000000,0x00000000});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawablebg=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{
                                0xff000000,0x00000000});
                        relativeLayout.setBackground(gradientDrawablebg);

                        songname.setTextColor(Color.WHITE);
                        artist.setTextColor(Color.WHITE);
                    }
                }
            });
        }
        else{
            Glide.with(this).asBitmap().load(R.drawable.ic_baseline_music_note_24).into(coverart);
//            Imageanimation(this,coverart,bitmap);

            ImageView gradient=findViewById(R.id.imageViewgradent);
            RelativeLayout relativeLayout=findViewById(R.id.mcontainer);

            gradient.setBackgroundResource(R.drawable.backgradient);
            relativeLayout.setBackgroundResource(R.drawable.main_bg);


            songname.setTextColor(Color.WHITE);
            artist.setTextColor(Color.WHITE);
        }
    }
    //fade in and fade out animation
    public void Imageanimation(Context context,ImageView imageView,Bitmap bitmap)
    {
        Animation aniout= AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation aniin= AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        aniout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                aniin.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(aniin);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(aniout);

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextbtnclicked();

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder)
    {
        MusicService.MyBinder myBinder=(MusicService.MyBinder) iBinder;
        musicService=myBinder.getService();
        Toast.makeText(this,"connected"+musicService,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService=null;
    }
}