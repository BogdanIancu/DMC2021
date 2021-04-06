package com.example.coolweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.models.City;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CITY_REQ_CODE = 1;

    private List<City> cities = new ArrayList<>();
    private Spinner spinnerLocation = null;
    private ArrayAdapter<City> adapter = null;

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

//        try {
//            FileInputStream file = openFileInput("cities.dat");
//            ObjectInputStream inputStream = new ObjectInputStream(file);
//            cities = (List<City>) inputStream.readObject();
//            inputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        SharedPreferences sharedPreferences = getSharedPreferences("cities", MODE_PRIVATE);
        Set<String> cityNames = sharedPreferences.getStringSet("names", null);
        if(cityNames != null) {
            cities.clear();
            for(String name : cityNames) {
                String[] values = name.split(", ");
                City city = new City(values[0], values[1]);
                cities.add(city);
            }
        }

        spinnerLocation = findViewById(R.id.spinnerLocation);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, cities);
        spinnerLocation.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            FileOutputStream file = openFileOutput("cities.dat", MODE_PRIVATE);
//            ObjectOutputStream outputStream = new ObjectOutputStream(file);
//            outputStream.writeObject(cities);
//            outputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        SharedPreferences sharedPreferences = getSharedPreferences("cities", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new TreeSet<>();
        for(City city : cities) {
            set.add(city.toString());
        }
        editor.putStringSet("names", set);
        editor.commit();
    }

    public void moreDetailsClick(View view) {
        Spinner spinnerLocation = findViewById(R.id.spinnerLocation);
        if(spinnerLocation != null && spinnerLocation.getSelectedItem() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://openweathermap.org/city/" + spinnerLocation.getSelectedItem().toString()));
            startActivity(intent);
        }
    }

    public void addNewCityClick(View view) {
        Intent intent = new Intent(MainActivity.this, NewCityActivity.class);
        startActivityForResult(intent, ADD_CITY_REQ_CODE);
    }

    public void forecastClick(View view) {
        Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
        if(spinnerLocation != null && spinnerLocation.getSelectedItem() != null) {
            String location = spinnerLocation.getSelectedItem().toString();
            intent.putExtra("location", location);
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CITY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            City city = (City)data.getSerializableExtra("newCity");
            cities.add(city);
            if(adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}