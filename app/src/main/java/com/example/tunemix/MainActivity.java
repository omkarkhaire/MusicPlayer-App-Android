package com.example.tunemix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE=1;
    public static ArrayList<Musicfiles> musicfiles;
    static boolean shufflebtn=false;
    static boolean repeatbtn=false;
    static ArrayList<Musicfiles> albums=new ArrayList<>();



    //for menu we created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);


        return super.onCreateOptionsMenu(menu);
    }
    //
//  when  we select menu options


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.about:
                startActivity(new Intent(getApplicationContext(),about.class));

                return  true;

        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getpermission();
//        initviewpager();//setting up tabs and fragments

    }

    private void getpermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else{
            musicfiles=getallaudios(this);
            initviewpager();//setting up tabs and fragments

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE)
        {
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);

            }
            else{
                musicfiles=getallaudios(this);
                initviewpager();//setting up tabs and fragments

            }


        }
    }

    private void initviewpager() {
        ViewPager viewPager=(ViewPager) findViewById(R.id.viewpager);//view pager
        TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        Viewpageradapter viewpageradapter=new Viewpageradapter(getSupportFragmentManager());

        viewpageradapter.addFragment(new songsfragment(),"Songs");
        viewpageradapter.addFragment(new albumfragment(),"Albums");

        viewPager.setAdapter(viewpageradapter);
        tabLayout.setupWithViewPager(viewPager);

    }
    public static class Viewpageradapter extends FragmentPagerAdapter// viewpager adapter
    {

          private ArrayList<Fragment> fragments;
          private  ArrayList<String> titles;
        public Viewpageradapter(@NonNull FragmentManager fm) {
            super(fm);
           this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            titles.add(title);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @NonNull
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }


    }

    public static ArrayList<Musicfiles> getallaudios(Context context)
    {
        ArrayList<String> duplicate=new ArrayList<>();
        ArrayList<Musicfiles> tempfiles=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projeciton={
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,//path
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor=context.getContentResolver().query(uri,projeciton,null,null,null);
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                String album=cursor.getString(0);
                String titel=cursor.getString(1);
                String duration=cursor.getString(2);
                String path=cursor.getString(3);
                String artist=cursor.getString(4);

                Musicfiles musicfiles=new Musicfiles(path,titel,artist,album,duration);
                tempfiles.add(musicfiles);

                if(!duplicate.contains(album))
                {
                    albums.add(musicfiles);  //this all for album fragment
                    duplicate.add(album);
                }

            }
            cursor.close();
        }
        return tempfiles;
    }


}