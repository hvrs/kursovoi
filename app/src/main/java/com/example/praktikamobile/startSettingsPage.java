package com.example.praktikamobile;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class startSettingsPage extends AppCompatActivity{
public static String Login;

    ImageView userPhoto = null;
    private static final int REQUEST_SELECT_PHOTOS = 1;
    private static final int MAX_PHOTOS = 1;
    private List<String> selectedPhotos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_settings_page);

    }


    public void SetMainPhoto(View v){
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
                    String photoUri = selectedPhotos.get(i);
                    userPhoto = (ImageView) findViewById(R.id.set_photoO);
                    Picasso.get().load(photoUri).into(userPhoto);
                }


            }
        }
    }


    public String name = null;
    public String surname = null;
    public String bday = null;
    public void SaveSettings(View view){
            EditText tb_name = (EditText) findViewById(R.id.tb_name);
            name = tb_name.getText().toString();

            EditText tb_surname = (EditText) findViewById(R.id.tb_surname);
            surname = tb_surname.getText().toString();

            EditText tb_bday = (EditText) findViewById(R.id.tb_bday);
            bday = tb_bday.getText().toString();

            if (!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(surname) &&!TextUtils.isEmpty(bday) && selectedPhotos.size() >= 1){
            forAllfunction fAwl = new forAllfunction();
            fAwl.execute();

            toMainPage();

            }
    } //какава)00)
    private class forAllfunction extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params){
            Drawable drawable = userPhoto.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            OkHttpClient client = new OkHttpClient();//Пост-запрос типа
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("login",Login)
                    .addFormDataPart("name", name)
                    .addFormDataPart("surname", surname)
                    .addFormDataPart("bday", bday)
                    .addFormDataPart("image", "image.jpg",
                            RequestBody.create(MediaType.parse("image/jpeg"),imageBytes))
                    .build();

            Request request = new Request.Builder()
                    .url("https://smtpservers.ru/projects/praktikaMobile/startSettings")
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



    public void toMainPage(){
        Intent i = new Intent(getApplicationContext(), mainPage.class);
        startActivity(i);
    }
}
