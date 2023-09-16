package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{
	
	//atributos que necesito:
	//String id_v; int maxSpeed_v; int contClass_v; List<String> itinerario_str;
	
	private int time_json;
	
	private String id_json;
	
	private int max_speed_json;
	
	private int cont_class_json;
	
	private List<String> itinerario_json;
	
	
	//constructor
	
	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		time_json = data.getInt("time");
		
		id_json = data.getString("id");
		
		max_speed_json = data.getInt("maxspeed");
		
		cont_class_json = data.getInt("class");
		
			JSONArray itinerary = new JSONArray();
		
			itinerary = data.getJSONArray("itinerary");
			
			this.itinerario_json = new ArrayList<String>();
			
			for(int i = 0; i < itinerary.length(); i++) {
				this.itinerario_json.add(itinerary.getString(i));
				
			}
			
		return new NewVehicleEvent(time_json, id_json, max_speed_json, cont_class_json, itinerario_json);
	}
	
}
