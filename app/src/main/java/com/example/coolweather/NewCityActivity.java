package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coolweather.models.City;

public class NewCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);
    }

    public void addCityClick(View view) {
        EditText cityNameEditText = findViewById(R.id.editTextCityName);
        EditText countyNameEditText = findViewById(R.id.editTextCountryName);

        String city = cityNameEditText.getText().toString();
        String country = countyNameEditText.getText().toString();

        if(city.isEmpty() || country.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ambele campuri sunt obligatorii", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            City newCity = new City(city, country);
            intent.putExtra("newCity", newCity);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}