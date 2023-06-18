package com.example.praktikamobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;

public class mainPage extends AppCompatActivity {
    Button btnCam;
    Bitmap photo;
    ArrayList<Bitmap> mImages = new ArrayList<>();
    final int TYPE_PHOTO = 1;
    final int REQUEST_CODE_PHOTO = 1;
    private static final int pic_id = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

      //  ArrayList<Bitmap> mImages = new ArrayList<>();
        for (int i = 0; i < 80; i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ppj);
            mImages.add(bitmap);
        }
        GridView gridView = findViewById(R.id.gridViewOoe);
        imageAdapter imageAdapter = new imageAdapter(this,mImages );
        gridView.setAdapter(imageAdapter);

        btnCam = (Button) findViewById(R.id.btn_cameraStart);
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        photo = (Bitmap)data.getExtras().get("data");
        mImages.add(photo);

    }

    public void openCameras(View view) {

    }

    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onPause() {
        super.onPause();
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