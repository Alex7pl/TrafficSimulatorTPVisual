package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

	
	//atributos
	int time_json;
	
	List<Pair<String, Integer>> listaParCont;
	
	
	//constructora:
	public SetContClassEventBuilder() {
		super("set_cont_class");
		
	}


	protected Event createTheInstance(JSONObject data) {

		time_json = data.getInt("time");
		
		JSONArray info = new JSONArray();
		
		info = data.getJSONArray("info");
		
		String parVehicle;
		int parCont;
		
		this.listaParCont = new ArrayList<Pair<String, Integer>>();
		
		for(int i = 0; i < info.length(); i++) {
			//saco parVehicle (el id):
			parVehicle = info.getJSONObject(i).getString("vehicle");
			parCont = info.getJSONObject(i).getInt("class");
			
			//a�ado a mi lista de pares:
			this.listaParCont.add(new Pair<String, Integer>(parVehicle, parCont));
			
		}
		
		//hago el return con todo:
		return new SetContClassEvent(time_json, listaParCont);
	}
	
	
}
