package com.example.coolweather.models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class WeatherCondition {
    private int temperature;
    private String description;
    private Bitmap image;
    private Date date;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static List<WeatherCondition> genetateRandomData() {
        Random random = new Random();
        List<WeatherCondition> list = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            WeatherCondition condition = new WeatherCondition();
            condition.temperature = random.nextInt(25);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            condition.date = calendar.getTime();
            int value = random.nextInt(4);
            switch (value) {
                case 0 : {
                    condition.description = "sunny"; break;
                }
                case 1 : {
                    condition.description = "cloudy"; break;
                }
                case 2 : {
                    condition.description = "rain"; break;
                }
                case 3 : {
                    condition.description = "storm"; break;
                }
            }
            list.add(condition);
        }
        return list;
    }
}
