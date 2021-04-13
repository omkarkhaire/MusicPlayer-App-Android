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

public class Albumadapter extends RecyclerView.Adapter<Albumadapter.Myholder>
{
    private Context context;
    private ArrayList<Musicfiles> albumfiles;
    View view;

    public Albumadapter(Context context, ArrayList<Musicfiles> albumfiles) {
        this.context = context;
        this.albumfiles = albumfiles;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      view= LayoutInflater.from(context).inflate(R.layout.album_item,parent,false);



        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {


        holder.textView.setText(albumfiles.get(position).getTitle());
        byte[] art=getalbumart(albumfiles.get(position).getPath());
        if(art!=null)
        {
            Glide.with(context).asBitmap().load(art).into(holder.imageView);
        }
        else{
            Glide.with(context).asBitmap().load(R.drawable.ic_baseline_music_note_24).into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,AlbumDetails.class);
                intent.putExtra("Albumname",albumfiles.get(position).getAlbum());
                context.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        return albumfiles.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {

             ImageView imageView;
             TextView textView;

        public Myholder(View itemview) {
            super(itemview);

            imageView=itemview.findViewById(R.id.albumimage);
            textView=itemview.findViewById(R.id.albumname);
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
