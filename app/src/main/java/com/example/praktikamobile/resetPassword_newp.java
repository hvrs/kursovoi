package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class resetPassword_newp extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_newp);
    }
    public void saveNewPassword(View v){
        SuccessUpdatePassword(v);
    }
    public void SuccessUpdatePassword(View view){
        Toast toast = Toast.makeText(getApplicationContext(),
                "Пароль успешно обновлён!",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();

        this.deleteDatabase("app.db");
        toAuthorization(view);


    }
    public void toAuthorization(View view){
        startActivity(new Intent(this, authorizationPage.class));
    }
}
