package com.example.coolweather.workers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ListView;

import androidx.room.Room;

import com.example.coolweather.R;
import com.example.coolweather.adapters.ForecastAdapter;
import com.example.coolweather.models.AppDatabase;
import com.example.coolweather.models.WeatherCondition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ForecastWorker extends Thread {
    private Activity activity;
    private String location;
    private AppDatabase database;

    public ForecastWorker(Activity activity, String location) {
        this.activity = activity;
        this.location = location;
        database = Room.databaseBuilder(activity, AppDatabase.class, "database").build();
    }

    @Override
    public void run() {
        super.run();
        final String API_KEY = "YOUR_API_KEY_HERE";
        final List<WeatherCondition> result = new ArrayList<>();

        try {
            URL url = new URL(String.format("https://api.openweathermap.org/data/2.5/forecast/daily?q=%s&appid=%s&mode=xml&units=metric&cnt=7&lang=ro",
                    location, API_KEY));
            HttpsURLConnection connection =  (HttpsURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.parse(inputStream);
            Node forecastNode = xml.getElementsByTagName("forecast").item(0);
            NodeList days = forecastNode.getChildNodes();
            database.weatherConditionDAO().deleteAll();
            for(int i = 0; i < days.getLength(); i++) {
                WeatherCondition condition = new WeatherCondition();
                Node node = days.item(i);
                String date = node.getAttributes().getNamedItem("day").getNodeValue();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                condition.setDate(simpleDateFormat.parse(date));
                Element element = (Element)node;
                Node symbol = element.getElementsByTagName("symbol").item(0);
                String description = symbol.getAttributes().getNamedItem("name").getNodeValue();
                String icon = symbol.getAttributes().getNamedItem("var").getNodeValue();
                condition.setDescription(description);
                Node temp = element.getElementsByTagName("temperature").item(0);
                String temperature = temp.getAttributes().getNamedItem("day").getNodeValue();
                double value = Double.parseDouble(temperature);
                condition.setTemperature((int)value);
                url = new URL(String.format("https://openweathermap.org/img/wn/%s@4x.png", icon));
                connection = (HttpsURLConnection) url.openConnection();
                Bitmap img = BitmapFactory.decodeStream(connection.getInputStream());
                condition.setImage(img);

                database.weatherConditionDAO().insert(condition);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result.clear();
        result.addAll(database.weatherConditionDAO().getAll());

        activity.runOnUiThread(() -> {
            ForecastAdapter adapter = new ForecastAdapter(activity, R.layout.forecast_item, result);
            ListView listView = activity.findViewById(R.id.listView);
            listView.setAdapter(adapter);
        });
    }
}
