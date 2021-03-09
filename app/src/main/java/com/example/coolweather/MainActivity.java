package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Aplicatia a pornit!");
        Toast.makeText(getApplicationContext(), "Aplicatia a pornit!", Toast.LENGTH_LONG).show();

        Button btnAbout = findViewById(R.id.buttonAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"DMC 2021", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void moreDetailsClick(View view) {
        Toast.makeText(getApplicationContext(), "More details soon...", Toast.LENGTH_SHORT).show();
    }
}