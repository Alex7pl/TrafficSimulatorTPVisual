package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.*;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	//atributos:
	int time_json;
	
	List<Pair<String, Weather>> listaParWeather;
	
	//constructora:
	public SetWeatherEventBuilder() {
		super("set_weather");
	}
	
	
	protected Event createTheInstance(JSONObject data) {

		time_json = data.getInt("time");
		
		JSONArray info = new JSONArray();
		
		info = data.getJSONArray("info"); //se trata de un array de JSONobject's:
				
		String parRoad;
		String weatherStr;
		Weather parWeather;
		
		this.listaParWeather = new ArrayList<Pair<String, Weather>>();
		
		for(int i = 0; i < info.length(); i++) {
			//primer jsonOBJECT
			//saco parROAD
			parRoad = info.getJSONObject(i).getString("road");
			weatherStr = info.getJSONObject(i).getString("weather");
			
			//saco parWEATHER
			parWeather = Weather.valueOf(weatherStr.toUpperCase());
			
			//a�ado a mi lista de pares:
			this.listaParWeather.add(new Pair<String, Weather>(parRoad, parWeather));
			
		}
		//una vez todo construido, hago el return:
		
		return new SetWeatherEvent(time_json, listaParWeather);
	}
	
	
	
	

}

