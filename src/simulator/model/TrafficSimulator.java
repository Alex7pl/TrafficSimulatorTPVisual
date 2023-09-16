package simulator.model;


import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	
	//atributos
	
	private RoadMap map;
	
	private SortedArrayList<Event> eventList;
	
	private int time;
	
	private List<TrafficSimObserver> observadores; //Lista de observadores ya que controller los va añadiendo
	
	//constructor
	
	public TrafficSimulator() {
		
		this.time = 0;
		this.map = new RoadMap();
		this.eventList = new SortedArrayList<Event>();
		this.observadores = new LinkedList<TrafficSimObserver>();
	}
	
	//metodos
	
	public void reset() {
		
		this.time = 0;
		this.map.reset();
		this.eventList.clear();
		
		for(TrafficSimObserver o : this.observadores) {
			o.onReset(map, eventList, time);
		}
	}
	
	public void addEvent(Event e) {
		
		this.eventList.add(e);
		
		for(TrafficSimObserver o : this.observadores) {
			o.onEventAdded(map, eventList, e, time);
		}
	}
	
	public void advance() {
		
		try {
		
			this.time++;
		
			for(TrafficSimObserver o : this.observadores) {
				o.onAdvanceStart(map, eventList, time);
			}
		
			int i = 0;
		
			while(i < eventList.size()) {
			
				if(eventList.get(i).getTime() == time) {
				
					eventList.get(i).execute(map);
					eventList.remove(i);
				}
				else i++;
			}
		
			for(Junction j : map.getJunctions()) {
			
				j.advance(time);
			}
		
			for(Road r : map.getRoads()) {
			
				r.advance(time);
			}
		
			for(TrafficSimObserver o : this.observadores) {
				o.onAdvanceEnd(map, eventList, time);
			}
		} catch (Exception ex) {
			
			for(TrafficSimObserver o : this.observadores) {
				o.onError(ex.getMessage());
			}
			
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
	
	public JSONObject report() {
		
		JSONObject sim = new JSONObject();
			
		sim.put("time", this.time);
		
		sim.put("state", this.map.report());
		
		return sim;
				
	}

	//metodos para añadir o quitar observadores
	
	public void addObserver(TrafficSimObserver o) {
		
		this.observadores.add(o);
		o.onRegister(this.map, this.eventList, this.time);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		
		if(!this.observadores.isEmpty()) {
			
			if(this.observadores.contains(o)) {
				
				this.observadores.remove(o);
			}
		}
	}

}
