package com.cokejorge.es.imaginechallenge;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    EditText nickText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);      // Solo orientacion vertical
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // Mantener pantalla encendida
        setContentView(R.layout.activity_main_screen);
        nickText = (EditText) findViewById(R.id.editText);
    }

    public void comenzarJuego (View v){
        String nick = nickText.getText().toString();
        if(nick.equals("Nick") | nick.equals("")){
            nick = "Sin nombre";
        }
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("nick",nick);
        startActivity(intent);
    }
}
