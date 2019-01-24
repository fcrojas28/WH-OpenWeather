package com.fcrojas.utility.weather;

import org.json.JSONObject;

/**
 *
 * @author frojas
 *
 */
public interface OpenWeather {

	public JSONObject getWindInfoByZipCode(String zipCode);

	public JSONObject getWindInfoByGeoCoordinates(String latitude, String longitude);

	public JSONObject connectToOpenWeatherServer(String apiUrl);

}
