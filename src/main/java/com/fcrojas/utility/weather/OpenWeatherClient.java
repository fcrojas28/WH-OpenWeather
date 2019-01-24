package com.fcrojas.utility.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 *
 * @author frojas
 *
 */
@Service
public class OpenWeatherClient implements OpenWeather {

	public static final String WEATHER_MAP_API_KEY = "0a09254875a3edfff978648d53a72fd2";

	private String openWeatherApiEndpoint = "http://api.openweathermap.org/data/2.5/weather";

	/**
	 * This method uses the OpenWeather API to get the current wind info
	 * for a giving zip code
	 */
	@Override
	public JSONObject getWindInfoByZipCode(String zipCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(openWeatherApiEndpoint);
		sb.append("?zip=").append(zipCode).append(",us");
		sb.append("&APPID=").append(OpenWeatherClient.WEATHER_MAP_API_KEY);

		return connectToOpenWeatherServer(sb.toString());
	}

	@Override
	public JSONObject getWindInfoByGeoCoordinates(String latitude, String longitude) {
		StringBuilder sb = new StringBuilder();
		sb.append(openWeatherApiEndpoint);
		sb.append("?lat=").append(latitude).append("&lon=").append(longitude);
		sb.append("&APPID=").append(OpenWeatherClient.WEATHER_MAP_API_KEY);

		return connectToOpenWeatherServer(sb.toString());
	}

	@Override
	public JSONObject connectToOpenWeatherServer(String apiUrl) {
		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader br;
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				// If we are here, it mean that even if the zip code is 5 digit number is not a good us zip code
				// and the open weather service return a 404 Error
				// It is also possible we are getting some other error code from the server and we are catching the error here
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			String line;
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			br.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put("errorCode", 1);
			error.put("errorMsg", e.getMessage());
			return error;
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put("errorCode", 1);
			error.put("errorMsg", e.getMessage());
			return error;
		}

		return new JSONObject(result.toString());
	}

}
