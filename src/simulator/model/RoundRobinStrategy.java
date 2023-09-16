package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy{

	//atributos
	
	private int timeSlot;
	
	
	//constructor
	
	public RoundRobinStrategy(int time) {
		
		this.timeSlot = time;
	}
	
	
	//metodos
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		
		int green = 0;
		
		if (roads.isEmpty()) green = -1;
		else if(currGreen == -1) green = 0;
		else if(currTime - lastSwitchingTime < this.timeSlot) green = currGreen;
		else if(currGreen == roads.size() - 1) green = 0;
		else green = (currGreen + 1) % roads.size();
		
		return green;
	}

}
