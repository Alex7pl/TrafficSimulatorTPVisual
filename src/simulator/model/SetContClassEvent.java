package simulator.model;

import java.util.List;

import simulator.misc.*;

public class SetContClassEvent extends Event{

	//atributo para la lista de pares;
	
	private List<Pair<String, Integer>> listaParCont;

	
	//constructora:
	
	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		
		this.listaParCont = cs;	
	}


	//metodo
	
	void execute(RoadMap map) {

		//recorro la lista cs:
		for(Pair<String, Integer> par : listaParCont) {
			
			if(map.getVehicle(par.getFirst()) == null ) throw new IllegalArgumentException(String.format("[ERROR]: ", "El vehiculo no existe"));
			
			//cada String es un VEHICULO, luego:

			map.getVehicle(par.getFirst()).setContClass(par.getSecond());	
		}	
	}
	
	public String toString() {
		
		StringBuilder str = new StringBuilder("Change CO2 class: [");
		
		int i = 0;
		
		for(Pair<String, Integer> par : listaParCont) {
			
			if(i != 0) str.append(", ");
			str.append("(").append(par.getFirst()).append(",").append(par.getSecond()).append(")");
			i++;
		}
		
		str.append("]");
		
		return str.toString();
	}
	
}
