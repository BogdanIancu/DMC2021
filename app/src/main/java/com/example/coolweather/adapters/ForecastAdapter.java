package com.example.coolweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coolweather.R;
import com.example.coolweather.models.WeatherCondition;

import java.text.SimpleDateFormat;
import java.util.List;

public class ForecastAdapter extends ArrayAdapter<WeatherCondition> {
    public ForecastAdapter(@NonNull Context context, int resource, @NonNull List<WeatherCondition> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.forecast_item, null);
        }
        WeatherCondition condition = getItem(position);

        TextView temperatureTextView = convertView.findViewById(R.id.textViewItemTemperature);
        temperatureTextView.setText(String.format("%d *C", condition.getTemperature()));

        ImageView imageView = convertView.findViewById(R.id.imageViewItem);
        imageView.setImageBitmap(condition.getImage());

        TextView descriptionTextView = convertView.findViewById(R.id.textViewItemConditions);
        descriptionTextView.setText(condition.getDescription());

        TextView dateTextView = convertView.findViewById(R.id.textViewItemDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTextView.setText(simpleDateFormat.format(condition.getDate()));

        return convertView;
    }
}
