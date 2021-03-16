package com.example.coolweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.models.City;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CITY_REQ_CODE = 1;

    private List<City> cities = new ArrayList<>();

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
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://openweathermap.org/city/" + textViewLocation.getText()));
        startActivity(intent);
    }

    public void addNewCityClick(View view) {
        Intent intent = new Intent(MainActivity.this, NewCityActivity.class);
        startActivityForResult(intent, ADD_CITY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CITY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            City city = (City)data.getSerializableExtra("newCity");
            cities.add(city);
        }
    }
}