package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject{
	
	//Atributos
	
	protected Junction cruceOrigen;
	
	protected Junction cruceDest;
	
	protected int longitud;
	
	protected int maxSpeed;
	
	protected int limitSpeed;
	
	protected int contLimit;
	
	protected Weather ambiente;
	
	protected int totalCont;
	
	protected List<Vehicle> vehiculos;
	
	
	//Constructor

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		
		this.vehiculos = new ArrayList<Vehicle>();
			
		if(maxSpeed <= 0) throw new IllegalArgumentException(String.format("[ERROR]: ", "La velocidad maxima debe ser positiva"));
		else this.maxSpeed = maxSpeed;
		
		if(contLimit < 0) throw new IllegalArgumentException(String.format("[ERROR]: ", "El limite de contaminacion debe ser no negativo"));
		else this.contLimit = contLimit;
		
		if(length <= 0) throw new IllegalArgumentException(String.format("[ERROR]: ", "La longitud de la carretera debe ser mayor a 0"));
		else this.longitud = length;
		
		if(srcJunc == null || destJunc == null) throw new IllegalArgumentException(String.format("[ERROR]: ", "Los cruces deben ser distintos de null"));
		else { 
			this.cruceOrigen = srcJunc; 
			this.cruceDest = destJunc; 
			srcJunc.addOutgoingRoad(this);
			destJunc.addIncomingRoad(this);
		}
		
		if(weather == null) throw new IllegalArgumentException(String.format("[ERROR]: ", "El tiempo debe ser distinto de null"));
		else this.ambiente = weather;
		
		this.limitSpeed = this.maxSpeed;
	}
	
	//getters

	public int getLength() {
		return this.longitud;
	}
	
	public Junction getDest() {
		
		return this.cruceDest;
	}
	
	public Junction getSrc() {
		
		return this.cruceOrigen;
	}
	
	public Weather getWeather() {
		
		return this.ambiente;
	}
	
	public int getContLimit() {
		
		return this.contLimit;
	}
	
	public int getMaxSpeed() {
		
		return this.maxSpeed;
	}
	
	public int getSpeedLimit() {
		
		return this.limitSpeed;
	}
	
	public int getTotalCO2() {
		
		return this.totalCont;
	}
	
	public List<Vehicle> getVehicles(){
		
		return Collections.unmodifiableList(this.vehiculos);
	}
	
	//metodos
	
	void enter(Vehicle v) {
		
		if(v.getSpeed() != 0) throw new IllegalArgumentException(String.format("[ERROR]: ", "La velocidad del vehiculo debe ser mayor a 0"));
		else if(v.getLocation() != 0) throw new IllegalArgumentException(String.format("[ERROR]: ","La localizacion del vehiculo debe ser mayor a 0"));
		else vehiculos.add(v);
		
	}
	
	void exit(Vehicle v) {
		
		vehiculos.remove(v);
	}
	
	void setWeather(Weather w) {
		
		if(w == null) throw new IllegalArgumentException();
		else this.ambiente = w;
	}
	
	void addContamination(int c) {
		
		if(c < 0) throw new IllegalArgumentException();
		else this.totalCont += c;
	}
	
	void advance(int time) {
		
		this.reduceTotalContamination();
		
		this.updateSpeedLimit();
		
		for(Vehicle v : this.vehiculos) {
			
			v.setSpeed(this.calculateVehicleSpeed(v));
			
			v.advance(time);
		}
		
		Collections.sort(this.vehiculos, new SortComparator());
	}
	
	public JSONObject report() {
		
		JSONObject roadJSON = new JSONObject();
		JSONArray veh = new JSONArray();
		
		roadJSON.put("id", this._id);
		roadJSON.put("speedlimit", this.limitSpeed);
		roadJSON.put("weather", this.ambiente.toString());
		roadJSON.put("co2", this.totalCont);
		
		for(Vehicle v : this.vehiculos) {
			
			veh.put(v.getId());
		}
		
		roadJSON.put("vehicles", veh);
		
		return roadJSON;
	}
	
	
	//abstractos
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	
}
