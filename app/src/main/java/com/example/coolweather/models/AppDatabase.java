package com.example.coolweather.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.coolweather.models.daos.WeatherConditionDAO;

@Database(entities = {WeatherCondition.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WeatherConditionDAO weatherConditionDAO();
}
