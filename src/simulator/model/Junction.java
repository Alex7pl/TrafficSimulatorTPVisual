package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{
	//clase de los cruces:
	
	//atributos:
	
	private List<Road> carreterasEntrantes;
	
	private Map<Junction,Road> carreterasSalientes;
	
	private List<List<Vehicle>> listaColas;
	
	private Map<Road, List<Vehicle>> carreteraCola;
	
	//-1 para indicar que todas carreteras entrantes tienen semaforo en verde
	private int indiceSemaforoVerde;
	
	private int pasoDeCambio;
	
	private LightSwitchingStrategy estrategiaCambio;
	
	private DequeuingStrategy estrategiaExtraer;
	
	private int x;
	
	private int y;
	
	//constructor
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy
			dqStrategy, int xCoor, int yCoor) {
		
		super(id);
			
		if(lsStrategy != null) this.estrategiaCambio = lsStrategy;
		else throw new IllegalArgumentException(String.format("[ERROR]: ", "La estrategia de cambios no puede ser null"));			
			
		if(dqStrategy != null) this.estrategiaExtraer = dqStrategy;
		else throw new IllegalArgumentException(String.format("[ERROR]: ", "La estrategia de extracción no puede ser null"));
		
		if(xCoor >= 0 && yCoor >= 0) {
			
			this.x = xCoor;
			this.y = yCoor;
		}else {
			
			throw new IllegalArgumentException(String.format("[ERROR]: ", "Las coordenadas dadas deben ser un numero positivo"));
		}
		
		this.carreterasSalientes = new HashMap<Junction,Road>();
		this.carreteraCola = new HashMap<Road, List<Vehicle>>();
		this.carreterasEntrantes = new ArrayList<Road>();
		this.listaColas = new ArrayList<List<Vehicle>>();
		this.pasoDeCambio = 0;
		this.indiceSemaforoVerde = -1;
	}
	
	//metodos
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getGreenLightIndex() {
		return this.indiceSemaforoVerde;
	}
	
	public List<Road> getInRoads() {
		return Collections.unmodifiableList(this.carreterasEntrantes);
	}
	
	public Map<Road, List<Vehicle>> getColas(){
		return Collections.unmodifiableMap(this.carreteraCola);
	}
	
	public void addIncomingRoad(Road r) {
		
		if(r.cruceDest.equals(this)) {
		
			this.carreterasEntrantes.add(r);
			//creamos una cola de vehiculos para r:
		
			List<Vehicle> vehicles = new ArrayList<Vehicle>();
			this.listaColas.add(vehicles);
			this.carreteraCola.put(r, vehicles);
		}
		else throw new IllegalArgumentException("El cruce destino de la carretera no coincide");
		
	}
	
	public void addOutgoingRoad(Road r) {
		
		if(this.carreterasSalientes.containsKey(r.cruceDest)) 
			throw new IllegalArgumentException("Ya existe una carretera que lleva al cruce " + r.cruceDest.getId());
		else if(!r.cruceOrigen.equals(this))
			throw new IllegalArgumentException("El cruce origen de la carretera no coincide");
		else
			this.carreterasSalientes.put(r.cruceDest, r);
	}
	
	void enter(Vehicle v) {
		
		//meto un vehiculo a la cola que le toque, es decir, sobre la que circule.
		
		Road c = v.getRoad();
		
		List<Vehicle> q = carreteraCola.get(c);
		
		q.add(v);
	}
	
	Road roadTo(Junction cruce) {
		
		return this.carreterasSalientes.get(cruce);
	}

	@Override
	void advance(int time) {
		
		if(!listaColas.isEmpty() && this.indiceSemaforoVerde != -1) {
			List<Vehicle> extraer;
			
			extraer = this.estrategiaExtraer.dequeue(this.carreteraCola.get(carreterasEntrantes.get(indiceSemaforoVerde)));
			
			for(Vehicle v : extraer) {
				
				v.moveToNextRoad();
			}
			
			for(Vehicle v1 : extraer) {
				
				this.carreteraCola.get(carreterasEntrantes.get(indiceSemaforoVerde)).remove(v1);
			}
		}
		int cambio;
		
		cambio = this.estrategiaCambio.chooseNextGreen(carreterasEntrantes, listaColas, this.indiceSemaforoVerde, pasoDeCambio, time);
		
		if (cambio != this.indiceSemaforoVerde) {
			
			this.indiceSemaforoVerde = cambio;
			this.pasoDeCambio = time;
		}
	}


	public JSONObject report() {
		
		JSONObject junctionJSON = new JSONObject();
		
		JSONArray queues = new JSONArray();

		junctionJSON.put("id", this._id);
		
		if(this.indiceSemaforoVerde != -1 && !carreterasEntrantes.isEmpty()) {
			junctionJSON.put("green", carreterasEntrantes.get(indiceSemaforoVerde).getId()); //nos da el identificador de la ROAD
		}
		else {
			junctionJSON.put("green", "none");
		}
		
		for(Road c : this.carreterasEntrantes) {
			
			JSONObject carretera = new JSONObject();
			
			carretera.put("road", c.getId());
			
			JSONArray veh = new JSONArray();
			
			for(Vehicle v : this.carreteraCola.get(c)) {
				
				veh.put(v.getId());
			}
			
			carretera.put("vehicles", veh);
			
			queues.put(carretera);
		}
		
								   
		junctionJSON.put("queues", queues);
		
		return junctionJSON;
	}
	
	
}


