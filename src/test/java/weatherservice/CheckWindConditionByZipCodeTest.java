package weatherservice;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.fcrojas.Application;
import com.fcrojas.utility.weather.OpenWeatherClient;

/**
 * 
 * @author frojas
 *
 */
@SpringBootTest(classes=Application.class)
public class CheckWindConditionByZipCodeTest {
	
	private JSONObject result;
	
	private OpenWeatherClient client = Mockito.mock(OpenWeatherClient.class);
	
	@Before
	public void create() {
		result = new JSONObject();
		result.put("deg", 350);
		result.put("speed", 10.3);
		Mockito.when(client.getWindInfoByZipCode("89178")).thenReturn(result);
		Mockito.when(client.getWindInfoByGeoCoordinates("36.0326233", "-115.30237350000002")).thenReturn(result);
	}
	
	@Test
	public void getWindInfoUsingZipCode() {
		assertEquals(result, client.getWindInfoByZipCode("89178"));
	}
	
	@Test
	public void getWindInfoUsingCoordinates() {
		assertEquals(result, client.getWindInfoByGeoCoordinates("36.0326233", "-115.30237350000002"));
	}


}
