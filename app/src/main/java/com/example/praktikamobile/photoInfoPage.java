package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class photoInfoPage extends AppCompatActivity {
    public static int idPhoto;
    public boolean isFavo = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_info_page);

        OkHttpClient client = new OkHttpClient();
        String url = "https://smtpservers.ru/projects/praktikaMobile/photoInfoPage?idPhoto="+idPhoto;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);


        Context context = this;
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json.equals("500")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Gson gson = new Gson();
                    Photos[] adv = gson.fromJson(json, Photos[].class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                            String imageUrl = "https://smtpservers.ru/projects/praktikaMobile/uploads/"+adv[0].photoPath;
                            Picasso.get()
                                    .load(imageUrl)
                                    .into(imageView);
                            TextView dateCreate = (TextView) findViewById(R.id.dateCreate);
                            dateCreate.setText(adv[0].date);
                            if(TextUtils.equals(adv[0].isFav, "1")){
                                ImageView isFavIcon = (ImageView) findViewById(R.id.favoritIcon);
                                isFavIcon.setImageResource(R.drawable.fav_heart_on);
                                isFavo = true;
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.toString();

            }
        });
    }

    public  void setFavorite(View v){
        ImageView isFavIcon = (ImageView) findViewById(R.id.favoritIcon);
    if (isFavo){
        isFavIcon.setImageResource(R.drawable.fav_heart_off);
        isFavo = false;
    }
    else {
        isFavIcon.setImageResource(R.drawable.fav_heart_on);
        isFavo = true;
    }
        OkHttpClient client = new OkHttpClient();
        String url = "https://smtpservers.ru/projects/praktikaMobile/updateFavImage?idImage="+idPhoto+"&isFav="+isFavo;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();


                if (responseBody.equals("200")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                          //  Toast.makeText(getApplicationContext(), "В ", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.toString();
            }
        });
    }


    public void deletePhoto(View v){
        OkHttpClient client = new OkHttpClient();
        String url = "https://smtpservers.ru/projects/praktikaMobile/deletePhoto?idPhoto="+idPhoto;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();


                if (responseBody.equals("200")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Фотография удалена", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.toString();
            }
        });
        this.finish();
    }

    public void goBackBtn(View v){
        this.finish();
    }

    public class Photos{
        private String photoPath;
        private String date;
        private String isFav;
    }

}
