package com.cokejorge.es.imaginechallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Menu1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
        // Enable up icon (MIRAR TAMBIEN EL MANIFEST)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
