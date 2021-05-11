package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;

import com.example.coolweather.models.AppDatabase;
import com.example.coolweather.models.WeatherCondition;

import java.util.List;
import java.util.Vector;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "database").build();
        List<WeatherCondition> result = new Vector<>();
        ChartView chartView = findViewById(R.id.chartView);

        new Thread(() -> {
            result.clear();
            result.addAll(database.weatherConditionDAO().getAll());
            runOnUiThread(() -> {
                int[] temperatures = new int[result.size()];
                int i = 0;
                for(WeatherCondition c : result) {
                    temperatures[i] = c.getTemperature();
                    i++;
                }
                chartView.setTemperatures(temperatures);
                chartView.invalidate();
            });
        }).start();
    }
}