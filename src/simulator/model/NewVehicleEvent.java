package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{

	//atributos para crear el vehiculo:
	
	private String id_v;
	
	private int maxSpeed_v;
	
	private int contClass_v;
	
	private List<String> itinerario_str;
	
	
	//constructor
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
			
		this.id_v = id;
		this.maxSpeed_v = maxSpeed;
		this.contClass_v = contClass;
		this.itinerario_str = itinerary;
	}
	
	
	//metodos
	
	void execute(RoadMap map)  {

		//crea vehículo, para ello antes tengo que llenar su lista de itinerarios de tipo Junction:
		
		List<Junction> itinerarioCruces = new ArrayList<>();
		
		for(String id : itinerario_str) {
			
			//busco en el map y añado:
			itinerarioCruces.add(map.getJunction(id));	
		}
		
		Vehicle v = new Vehicle(this.id_v, this.maxSpeed_v, this.contClass_v, itinerarioCruces);
		map.addVehicle(v);
			
		//llama a moveToNext
		v.moveToNextRoad();	
	}
	
	public String toString() {
		
		return "New Vehicle '"+ id_v +"'";
	}
}
