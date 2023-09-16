package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{

	//atributos:
	
	private List<Pair<String, Weather>> listaParWeather;

	
	//constructora
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		
		if(ws != null) this.listaParWeather = ws;
		else throw new IllegalArgumentException(String.format("[ERROR]: ", "La lista de pares esta vacia"));		
	}
	
	
	//metodos
	
	void execute(RoadMap map) {

		//recorro la lista ws:
		for(Pair<String, Weather> par : listaParWeather) {
			
			if(map.getRoad(par.getFirst()) == null) throw new IllegalArgumentException(String.format("[ERROR]: ", "La carretera no existe"));
			
			//cada String es un ROAD, luego:
			map.getRoad(par.getFirst()).setWeather(par.getSecond());
		}
	}
	
	public String toString() {
		
		StringBuilder str = new StringBuilder("Change weather: [");
		
		int i = 0;
		
		for(Pair<String, Weather> par : listaParWeather) {
			
			if(i != 0) str.append(", ");
			str.append("(").append(par.getFirst()).append(",").append(par.getSecond()).append(")");
			i++;
		}
		
		str.append("]");
		
		return str.toString();
	}

}
