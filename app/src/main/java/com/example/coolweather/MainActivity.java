package com.example.coolweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.models.City;
import com.example.coolweather.models.WeatherCondition;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CITY_REQ_CODE = 1;

    private TextView temperatureTextView = null;
    private TextView conditionsTextView = null;
    private ImageView imageView = null;

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
        btnAbout.setOnClickListener(v -> Toast.makeText(getApplicationContext(),"DMC 2021", Toast.LENGTH_LONG).show());

        temperatureTextView = findViewById(R.id.textViewTemperature);
        conditionsTextView = findViewById(R.id.textViewConditions);
        imageView = findViewById(R.id.imageView);

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

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerLocation != null && spinnerLocation.getSelectedItem() != null) {
                    WeatherWorker worker = new WeatherWorker();
                    worker.execute(spinnerLocation.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    class WeatherWorker extends AsyncTask<String, Void, WeatherCondition> {
        private final static String API_KEY = "7b10426ee90376dc3d6525f847128b35";

        @Override
        protected WeatherCondition doInBackground(String... strings) {
            if(strings != null && strings.length > 0) {
                String city = strings[0];
                try {
                    URL url = new URL(String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&format=json&lang=ro",
                            city, API_KEY));
                    HttpsURLConnection connection =  (HttpsURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    String jsonText = stringBuilder.toString();
                    JSONObject json = new JSONObject(jsonText);
                    WeatherCondition condition = new WeatherCondition();
                    JSONArray weatherArray = json.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    condition.setDescription(weatherObject.getString("description"));
                    String icon = weatherObject.getString("icon");
                    JSONObject mainObject = json.getJSONObject("main");
                    condition.setTemperature((int)mainObject.getDouble("temp"));
                    condition.setDate(new Date());

                    url = new URL(String.format("https://openweathermap.org/img/wn/%s@4x.png", icon));
                    connection = (HttpsURLConnection)url.openConnection();
                    Bitmap img = BitmapFactory.decodeStream(connection.getInputStream());
                    condition.setImage(img);

                    return condition;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherCondition weatherCondition) {
            super.onPostExecute(weatherCondition);
            if(weatherCondition != null) {
                conditionsTextView.setText(weatherCondition.getDescription());
                temperatureTextView.setText(String.format("%d° C", weatherCondition.getTemperature()));
                imageView.setImageBitmap(weatherCondition.getImage());
            } else {
                Toast.makeText(getApplicationContext(),
                        "The weather data cannot be refreshed right now. Try again later!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

