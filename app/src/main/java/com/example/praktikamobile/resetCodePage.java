package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class resetCodePage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_code_page);
    }
    int SendCode;
    int countErrorAttemps = 0;
    public void toAuth(View v){
        startActivity(new Intent(this, authorizationPage.class));
    }
    public void toNewPassword(View v){
        startActivity(new Intent(this, resetPassword_newp.class));
    }

    public void checkCode(View v){
        EditText myEditText = findViewById(R.id.tb_recovers_code);
        int code = Integer.parseInt(myEditText.getText().toString());
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT code FROM (SELECT * FROM RecoveryPassword ORDER BY id DESC LIMIT 1) t ORDER BY id;", null);
        while(query.moveToNext()) {
            SendCode = Integer.parseInt(query.getString(0));
        }

        if(countErrorAttemps >= 2){
            this.deleteDatabase("app.db");
            SendErrorNotification();
            toAuth(v);
        }
        if(code == SendCode){
            toNewPassword(v);
        }
        else{
            countErrorAttemps += 1;
            SendErrorCodeInputNotification();
        }
    }
    public void SendErrorNotification() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Неверно введен код восстановления! Попробуйте позже.",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public void SendErrorCodeInputNotification() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Неверно введен код восстановления!",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

}
