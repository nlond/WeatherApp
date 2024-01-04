package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetWeatherData {

    float Ftemp, Ctemp, feelsTemp;
    int humidity;
    String weatherDesc, cityName, countryName, continent;

    GetWeatherData (String city, String country, String continentName) {

        // unique key found on website
        String apiKey = "9589fd5c7f3525b123bd22f1aeca9d61";

        /*
        String city = "Port of Spain";
        String country = "TT";
        continent = "America";
        */

        continent = continentName;
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + apiKey;

            // open connection to api
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Check if the response code indicates success (200)
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                // add string to one long json string
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Handle the JSON response
                handleJsonResponse(response.toString(), continent);
                //System.out.println(response);
            } else {
                System.out.println("Error: " + connection.getResponseMessage());
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleJsonResponse(String jsonResponse, String continentName) {
        // Handle the JSON response using gson
        // print the json string as a whole
        System.out.println("JSON Response:\n" + jsonResponse);

        // handle with google's gson
        Gson gson = new Gson();

        JsonElement jsonElement = gson.fromJson(jsonResponse, JsonElement.class);

        // check json formatting
        if (jsonElement.isJsonObject()) {

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // get info under title "name"
            cityName = jsonObject.get("name").getAsString();
            System.out.println(cityName);

            // get info under title "country"
            JsonObject countryObject = jsonObject.getAsJsonObject("sys");
            countryName = countryObject.get("country").getAsString();

            // weather is an array of one which contains all the objects
            JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
            JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();
            weatherDesc = weatherObject.get("description").getAsString();

            System.out.println(cityName + " has " + weatherDesc + " today");

            // get temperature in "main" object
            JsonObject tempObject = jsonObject.getAsJsonObject("main");
            float temp = tempObject.get("temp").getAsFloat();
            Ftemp = (float) ((temp - 273.15) * 1.8 + 32);
            Ctemp = (float) (temp - 273.15);

            System.out.println("temp in Celsius: " + Math.round(Ctemp));
            System.out.println("temp in Fahrenheit: " + Math.round(Ftemp));

            // get humidity in "main" object
            JsonObject humObject = jsonObject.getAsJsonObject("main");
            humidity = humObject.get("humidity").getAsInt();

            // get feels like temp in "main" object
            JsonObject feelObject = jsonObject.getAsJsonObject("main");
            temp = feelObject.get("feels_like").getAsFloat();
            feelsTemp = (float) ((temp - 273.15) * 1.8 + 32);

            // DisplayWeather has the GUI
            //DisplayWeather display = new DisplayWeather(Math.round(Ftemp), weatherDesc, cityName, countryName, continentName, humidity);
            System.out.println(countryName);

        } else {
            System.out.println("Invalid JSON format: Not a JSON object.");
        }
    }
}
