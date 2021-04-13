package com.example.tunemix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.tunemix.MainActivity.musicfiles;

public class AlbumDetails extends AppCompatActivity {


    ImageView imageView;
    RecyclerView recyclerView;
    String albumname;
    ArrayList<Musicfiles> files=new ArrayList<>();
   albumdetailadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        imageView=( ImageView)findViewById(R.id.photo);
        recyclerView=(RecyclerView )findViewById(R.id.Rlist);
        Intent intent=getIntent();
        albumname=intent.getStringExtra("Albumname");

        int j=0;
        for(int i=0;i<musicfiles.size();i++)
        {
            if(albumname.equals(musicfiles.get(i).getAlbum()))
            {
                files.add(j,musicfiles.get(i));
                j++;
            }
        }
        byte[] image=getalbumart(files.get(0).getPath());
        if(image!=null)
        {
            Glide.with(this).load(image).into(imageView);

        }
        else{
            Glide.with(this).load(R.drawable.ic_baseline_music_note_24).into(imageView);

        }


    }
    private byte[] getalbumart(String uri)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(files.size()<1))
        {
            adapter=new albumdetailadapter(this,files);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
    }
}