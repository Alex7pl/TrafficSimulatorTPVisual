package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	//atributos
	
	private List<Junction> cruces;
	
	private List<Road> carreteras;
	
	private List<Vehicle> vehiculos;
	
	private Map<String, Junction> mapaCruces;
	
	private Map<String,Road> mapaCarreteras;
	
	private Map<String, Vehicle> mapaVehiculos;
	
	//constructor
	
	RoadMap(){
		
		this.cruces = new ArrayList<Junction>();
		this.carreteras = new ArrayList<Road>();
		this.vehiculos = new ArrayList<Vehicle>();
		this.mapaCruces = new HashMap<String, Junction>();
		this.mapaCarreteras = new HashMap<String, Road>();
		this.mapaVehiculos = new HashMap<String, Vehicle>();
	}
	
	//getters
	
	public Junction getJunction(String id) {
		
		return this.mapaCruces.get(id);
	}
	
	public Road getRoad(String id) {
		
		return this.mapaCarreteras.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		
		return this.mapaVehiculos.get(id);
	}
	
	public List<Junction> getJunctions(){
		
		return Collections.unmodifiableList(this.cruces);
	}
	
	public List<Road> getRoads(){
		
		return Collections.unmodifiableList(this.carreteras);
	}
	
	public List<Vehicle> getVehicles(){
		
		return Collections.unmodifiableList(this.vehiculos);
	}
	
	//metodos
	
	void addJunction(Junction j) {
		
		if(mapaCruces.containsKey(j.getId())) throw new IllegalArgumentException(String.format("[ERROR]: ", "El cruce añadido ya existe"));
		else {
			
			mapaCruces.put(j.getId(), j);
			cruces.add(j);
		}
	}
	
	void addRoad(Road r) {
		
		if(mapaCarreteras.containsKey(r.getId())) throw new IllegalArgumentException(String.format("[ERROR]: ", "La carretera añadida ya existe"));
		else if(!je(r)) throw new IllegalArgumentException(String.format("[ERROR]: ", "Los cruces de la carretera añadida no existen"));
		else {
			
			mapaCarreteras.put(r.getId(), r);
			this.carreteras.add(r);
		}
	}
	
	private boolean je(Road r) {
		
		boolean existe = false;
		
		if(this.mapaCruces.containsKey(r.getDest().getId()) && this.mapaCruces.containsKey(r.getSrc().getId())) existe = true;
		
		return existe;
	}
	
	void addVehicle(Vehicle v) {
		
		if(mapaVehiculos.containsKey(v.getId())) throw new IllegalArgumentException(String.format("[ERROR]: ", "El vehiculo añadido ya existe"));
		else if(!itineraryExists(v.getItinerary())) throw new IllegalArgumentException(String.format("[ERROR]: ", "El vehiculo añadido no dispone de un itinerario valido"));
		else {
			//añado el vehiculo:
			
			mapaVehiculos.put(v.getId(), v);
			this.vehiculos.add(v);
		}

	}
	
	private boolean itineraryExists(List<Junction> itinerario) {
		boolean valid = true;

		int i = 0;

		while(valid && i < itinerario.size()){
			
			if(i != itinerario.size()-1){
				//no estoy en ultimo junction, compruebo si en el Map del junction actual esta el junction siguiente:
				if(!this.mapaCarreteras.containsKey(itinerario.get(i).roadTo(itinerario.get(i+1)).getId())) {
						
					valid = false;
				}
			}	

			i++;
		}
		
		return valid;
		
	}
	
	void reset() {
		
		this.carreteras.clear();
		this.cruces.clear();
		this.mapaCarreteras.clear();
		this.mapaCruces.clear();
		this.mapaVehiculos.clear();
		this.vehiculos.clear();
	}
	
	public JSONObject report() {
		
		JSONObject roadMapJSON = new JSONObject();
		
		JSONArray j = new JSONArray();
		
		JSONArray r = new JSONArray();
		
		JSONArray v = new JSONArray();
		
		for(Junction jun : this.cruces) {
			
			j.put(jun.report());
		}
		
		roadMapJSON.put("junctions", j);
		
		for(Road roa : this.carreteras) {
			
			r.put(roa.report());
		}
		
		roadMapJSON.put("roads", r);
		
		for(Vehicle veh : this.vehiculos) {
			
			v.put(veh.report());
		}
		
		
		roadMapJSON.put("vehicles", v);
		
		return roadMapJSON;
	}
}
