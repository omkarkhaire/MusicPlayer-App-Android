package com.example.tunemix;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class musicadapter extends RecyclerView.Adapter<musicadapter.MyViewHolder>
{
    private Context mycontext;
    private ArrayList<Musicfiles> mFiles;
    musicadapter(Context mycontext,ArrayList<Musicfiles> mFiles)
    {
        this.mycontext=mycontext;
        this.mFiles=mFiles;

    }


    @NonNull
    @Override
    public musicadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mycontext).inflate(R.layout.music_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull musicadapter.MyViewHolder holder, int position) {
        holder.songname.setText(mFiles.get(position).getTitle());
        byte[] art=getalbumart(mFiles.get(position).getPath());
        if(art!=null)
        {
            Glide.with(mycontext).asBitmap().load(art).into(holder.album_art);
        }
        else{
            Glide.with(mycontext).asBitmap().load(R.drawable.ic_baseline_music_note_24).into(holder.album_art);
        }
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(mycontext,PlayerActivity2.class);
               intent.putExtra("position",position);
               mycontext.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView songname;
        ImageView album_art;
        MyViewHolder(View view)
        {
            super(view);
            songname=view.findViewById(R.id.musicname);
            album_art=view.findViewById(R.id.musicimage);

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

}
