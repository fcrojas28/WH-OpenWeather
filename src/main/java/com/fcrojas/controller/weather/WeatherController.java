package com.fcrojas.controller.weather;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fcrojas.utility.weather.OpenWeatherClient;

/**
 * 
 * @author frojas
 *
 */
@RestController
@RequestMapping("/api/v1")
public class WeatherController {

	@Autowired
	private OpenWeatherClient openWeatherClient;
	
	/**
	 * This method return the current wind condition from the giving zip code
	 * @param zipCode
	 * @return A String with the wind speed and direction for the giving zip code
	 * @throws IOException 
	 */
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/wind/geo", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@Cacheable(value = "windCache", key = "{#latitude, #longitude}", sync = true)
	public String getWindInfoByUsingBrowserCurrentLocation(@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude) throws IOException {
		System.out.println("Getting the info using the coordinates");
		JSONObject resultJson = openWeatherClient.getWindInfoByGeoCoordinates(latitude, longitude);
		if (resultJson.getInt("cod") != 200) {
			return resultJson.toString();
		}
		
		JSONObject windData = resultJson.getJSONObject("wind");
		JSONObject windJson = new JSONObject();
		
		if (windData.has("speed")) {
			windJson.put("speed", windData.getInt("speed"));
		}
		if (windData.has("deg")) {
			windJson.put("direction", windData.getInt("deg") );
		}
		
		return windJson.toString();
	}
	
	/**
	 * This method return the current wind condition from the giving zip code
	 * @param zipCode
	 * @return A String with the wind speed and direction for the giving zip code
	 */
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/wind/{zipCode}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@Cacheable(value = "windCache", key = "#zipCode", sync = true)
	public String getWindInfoByZipCode(@PathVariable("zipCode") String zipCode) {
		System.out.println("Getting the info using the zip code");
		if(isZipCodeValid(zipCode)) {
			JSONObject resultJson = openWeatherClient.getWindInfoByZipCode(zipCode);
			if (resultJson.getInt("cod") != 200) {
				return resultJson.toString();
			}
			
			JSONObject windData = resultJson.getJSONObject("wind");
			JSONObject windJson = new JSONObject();
			
			if (windData.has("speed")) {
				windJson.put("speed", windData.getInt("speed"));
			}
			if (windData.has("deg")) {
				windJson.put("direction", windData.getInt("deg") );
			}
			
			return windJson.toString();
		} else {
			JSONObject error = new JSONObject();
			error.put("errorCode", 1);
			error.put("errorMsg", "Invalid zip code, please try again. Only five digit zip code is allow");
			return error.toString();
		}
	}
	
	/**
	 * This method clears all the entries from 'windCache' cache
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value="/cache/evict", method=RequestMethod.POST)
	@CacheEvict(value = "windCache", allEntries = true)
	public String evictCache() {
		System.out.println("cache cleared");
		return "cache cleared";
	}
	
	/**
	 * This function checks for a good zip code.
	 * A good zip code is String with 5 digits. Example 12345
	 * @param zipCode
	 * @return
	 */
	private boolean isZipCodeValid(String zipCode) {
		String regex = "^[0-9]{5}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(String.valueOf(zipCode));
		return matcher.matches();
	}
}
