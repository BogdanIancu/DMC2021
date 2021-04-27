package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.coolweather.adapters.ForecastAdapter;
import com.example.coolweather.models.WeatherCondition;
import com.example.coolweather.workers.ForecastWorker;

import java.util.List;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        String location = getIntent().getStringExtra("location");

        ForecastWorker worker = new ForecastWorker(this, location);
        worker.start();
    }
}