package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{

	
	//atributos
	
	private int timeSlot;
	
	
	//constructor
	
	public MostCrowdedStrategy(int time) {
		
		this.timeSlot = time;
	}
	
	
	//metodos
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		
		int green = 0;
		
		if(roads.isEmpty()) green = -1;
		else if(currTime-lastSwitchingTime < timeSlot) green = currGreen;
		else if(currGreen == -1) green = this.search(0, qs);
		else green = this.search((currGreen + 1) % roads.size(), qs);
		
		
		return green;
	}
	
	private int search(int pos, List<List<Vehicle>> qs) {
		
		int e = pos;
		
		int g = qs.get(e).size();
		
		for(int i = e + 1; i < qs.size(); i ++) {
			
			if(g < qs.get(i).size()) { 
				
				g = qs.get(i).size();
				e = i;
			}
		}
		
		if(pos != 0) {
			
			for(int j = 0; j < pos; j++) {
				
				if(g < qs.get(j).size()) {
					
					g = qs.get(j).size();
					e = j;
				}
			}
		}
		
		return e;
	}

}
