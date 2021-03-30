package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.coolweather.adapters.ForecastAdapter;
import com.example.coolweather.models.WeatherCondition;

import java.util.List;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        List<WeatherCondition> conditions = WeatherCondition.genetateRandomData();
        ForecastAdapter adapter = new ForecastAdapter(getApplicationContext(), R.layout.forecast_item, conditions);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}