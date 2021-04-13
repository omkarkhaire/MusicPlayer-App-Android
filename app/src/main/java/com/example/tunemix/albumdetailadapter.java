package com.example.tunemix;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class albumdetailadapter extends RecyclerView.Adapter<albumdetailadapter.Myholder>
{
    private Context context;
    static ArrayList<Musicfiles> albumfiles;
    View view;

    public albumdetailadapter(Context context, ArrayList<Musicfiles> albumfiles) {
        this.context = context;
        this.albumfiles = albumfiles;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);



        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {


        holder.songname.setText(albumfiles.get(position).getTitle());
        byte[] art=getalbumart(albumfiles.get(position).getPath());
        if(art!=null)
        {
            Glide.with(context).asBitmap().load(art).into(holder.album_art);
        }
        else{
            Glide.with(context).asBitmap().load(R.drawable.ic_baseline_music_note_24).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PlayerActivity2.class);
                intent.putExtra("sender","albumdetails");//to identify sender class to playeractivity
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return albumfiles.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {

        TextView songname;
        ImageView album_art;

        public Myholder(View itemview) {
            super(itemview);


            songname=itemview.findViewById(R.id.musicname);
            album_art=itemview.findViewById(R.id.musicimage);
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
