package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event>{
	
	//atributos
	
		protected int time;
	
		protected String id_;
		
		protected String src;
		
		protected String dest;
		
		protected int length;
		
		protected int co2;
		
		protected int speed;
		
		protected Weather tiempo;
		
		//constructor

	NewRoadEventBuilder(String type) {
		super(type);
	}
	
	//metodos

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		this.time = data.getInt("time");
		this.id_ = data.getString("id");
		this.src = data.getString("src");
		this.dest = data.getString("dest");
		this.length = data.getInt("length");
		this.co2 = data.getInt("co2limit");
		this.speed = data.getInt("maxspeed");
		this.tiempo = Weather.valueOf(data.getString("weather"));
		
		return createTheRoad();
	}
	
	abstract Event createTheRoad();
}
