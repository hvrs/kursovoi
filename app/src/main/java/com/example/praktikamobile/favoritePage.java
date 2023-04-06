package com.example.praktikamobile;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

public class favoritePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_image_page);

        ArrayList<Bitmap> mImages = new ArrayList<>();
        for (int i = 0; i < 80; i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ppj);
            mImages.add(bitmap);
        }
        GridView gridView = findViewById(R.id.gridViewFav);
        imageAdapter imageAdapter = new imageAdapter(this,mImages );
        gridView.setAdapter(imageAdapter);
    }
    public void toMainPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),mainPage.class);
        startActivity(i);
    }
    public void toSettingsPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),settingPage.class);
        startActivity(i);
    }
}