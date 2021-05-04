package com.example.coolweather.models.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.coolweather.models.WeatherCondition;

import java.util.List;

@Dao
public interface WeatherConditionDAO {
    @Query("SELECT * FROM weathercondition")
    List<WeatherCondition> getAll();

    @Insert
    void insert(WeatherCondition... conditions);

    @Delete
    void delete(WeatherCondition... conditions);

    @Query("DELETE FROM weathercondition")
    void deleteAll();
}
