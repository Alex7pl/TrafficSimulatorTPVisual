package simulator.control;

import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.*;

import simulator.model.*;

public class Controller {
	
	
	//ATRIBUTOS:
	
	TrafficSimulator traffic_simulator;
	
	Factory<Event> factoria_eventos;
	
	
	//constrcutor recibe una interface.
	public Controller(TrafficSimulator ts, Factory<Event> evento) {
		
		if(ts != null) this.traffic_simulator = ts;
		else throw new IllegalArgumentException(String.format("[ERROR]: ","El simulador de trafico es null"));
		
		if(evento != null) this.factoria_eventos = evento;
		else throw new IllegalArgumentException(String.format("[ERROR]: ","No existe factoria de eventos"));	
	}
	
	//metodos de la clase:
	
	public void reset() {
		
		this.traffic_simulator.reset();
	}
	
	public void loadEvents(InputStream in){
		
		JSONObject jo = new JSONObject(new JSONTokener(in));
		
		JSONArray ar = new JSONArray();
		
		if(jo.has("events")) {
			
			ar = jo.getJSONArray("events");
		
			JSONObject ev = new JSONObject();
		
			for(int i = 0; i < ar.length(); i++) {
			
				ev = ar.getJSONObject(i);
			
				this.traffic_simulator.addEvent(this.factoria_eventos.createInstance(ev));
			}
		}
		else {
			
			throw new IllegalArgumentException(String.format("[ERROR]: ", "El JSON input no dispone de datos de eventos"));
		}
	}
	
	public void run(int n, OutputStream fileSalida) {
		
		//ejecuta el simulador n "ticks":
		
		int i = 0;
		
		if(fileSalida != null) {
		
			JSONObject salida = new JSONObject();
		
			JSONArray estados = new JSONArray();
		
			while(i < n) {
			
				this.traffic_simulator.advance();
			
				estados.put(this.traffic_simulator.report());
			
				i++;
			}
		
			salida.put("states", estados);
		
			//ahora tengo que transformar esto en string y escribirlo en fileSalida
		
			PrintStream p = new PrintStream(fileSalida);
		
			p.println(salida.toString(3));
		}
		else {
			
			while(i < n) {
				
				this.traffic_simulator.advance();
				
				i++;
			}
		}
	}
	
	public void addObserver(TrafficSimObserver o) {
		
		this.traffic_simulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		
		this.traffic_simulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		
		this.traffic_simulator.addEvent(e);
	}
	
}
