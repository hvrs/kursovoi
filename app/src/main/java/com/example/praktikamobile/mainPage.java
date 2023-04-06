package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class mainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        ArrayList<Bitmap> mImages = new ArrayList<>();
        for (int i = 0; i < 80; i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ppj);
            mImages.add(bitmap);
        }
        GridView gridView = findViewById(R.id.gridViewOoe);
        imageAdapter imageAdapter = new imageAdapter(this,mImages );
        gridView.setAdapter(imageAdapter);
    }
    public void toFavspage(View v)
    {
        Intent i = new Intent(getApplicationContext(),favoritePage.class);
        startActivity(i);
    }
    public void toSettingsPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),settingPage.class);
        startActivity(i);
    }

}