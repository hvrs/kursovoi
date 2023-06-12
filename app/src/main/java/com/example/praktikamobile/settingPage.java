package com.example.praktikamobile;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.IOException;

public class settingPage extends AppCompatActivity{
    ImageButton userPhoto;
    Bitmap btmAp;
    private final int GALLERY_REQ_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        userPhoto = (ImageButton) findViewById(R.id.tb_photo_user);
    }
    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    public void exitFromApp(View v){
        try{
            this.deleteDatabase("users.db");
            finish();
            startActivity(new Intent(this, authorizationPage.class));
            finish();
        }
        catch (Exception e){
        }

    }
    public void toMainPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),mainPage.class);
        startActivity(i);
    }
    public void toFavspage(View v)
    {
        Intent i = new Intent(getApplicationContext(),favoritePage.class);
        startActivity(i);
    }
    public void uploadImage(View v){
    Intent iGallery = new Intent(Intent.ACTION_PICK);
    iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(iGallery, GALLERY_REQ_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (requestCode==GALLERY_REQ_CODE){


            }
        }
    }
}