package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class formPhoto extends AppCompatActivity {

    private List<String> selectedPhotos = new ArrayList<>();
    private static final int REQUEST_SELECT_PHOTOS = 1;
    private static final int MAX_PHOTOS = 1;

    public static String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_photo);}


    public void openCamera(View v){
        if(selectedPhotos.size() < 1) {
            // Запуск выбора фотографий из галереи
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Выберите фотографии"), REQUEST_SELECT_PHOTOS);
        }else{
            Toast.makeText(this, "Выбрано максимальное количество фотографий", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHOTOS && resultCode == RESULT_OK) {
            if (data != null) {
                // Получение выбранных фотографий
                if (data.getClipData() != null) {
                    int count = Math.min(data.getClipData().getItemCount(), MAX_PHOTOS);
                    if(selectedPhotos.size() + count > 1){
                        Toast.makeText(this, "Выбрано максимальное количество фотографий", Toast.LENGTH_SHORT).show();
                    }else{
                        for (int i = 0; i < count; i++) {
                            selectedPhotos.add(data.getClipData().getItemAt(i).getUri().toString());
                        }
                    }
                } else if (data.getData() != null) {
                    if(selectedPhotos.size() + 1 > 1){
                        Toast.makeText(this, "Выбрано максимальное количество фотографий", Toast.LENGTH_SHORT).show();
                    }else{
                        selectedPhotos.add(data.getData().toString());
                    }
                }

                int maxImages = Math.min(selectedPhotos.size(), 1);
                // Загрузка и отображение выбранных фотографий в каждом ImageView
                for (int i = 0; i < maxImages; i++) {
                    ImageView image = (ImageView) findViewById(R.id.imageViewWW);
                    String photoUri = selectedPhotos.get(i);
                    if (image != null) {
                        ImageView finalImageView = image;
                        Picasso.get().load(photoUri).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                int compressedWidth = 1280;
                                int compressedHeight = 720;

                                Bitmap compressedBitmap = Bitmap.createScaledBitmap(bitmap, compressedWidth, compressedHeight, true);

                                finalImageView.setImageBitmap(compressedBitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });

                    }
                }


            }
        }
    }
    public void saveNewPhoto(View v){
        forAllfunction fawl = new forAllfunction();
        fawl.execute();
        this.finish();

    }
    private class forAllfunction extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params){
            ImageView image = (ImageView) findViewById(R.id.imageViewWW);
            Drawable drawable = image.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            OkHttpClient client = new OkHttpClient();//Пост-запрос типа
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", emailUser)
                    .addFormDataPart("image", "image.jpg",
                            RequestBody.create(MediaType.parse("image/jpeg"),imageBytes))
                    .build();

            Request request = new Request.Builder()
                    .url("https://smtpservers.ru/projects/praktikaMobile/uploadsImage")
                    .post(requestBody)
                    .build();

            try{
                Response response = client.newCall(request).execute();
            }
            catch (IOException e){

            }

            return null;
        }
    }

}
