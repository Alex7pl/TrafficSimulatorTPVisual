package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{
	
	
	//atributos
	
	private int timeSlot;
	
	
	//constructor

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	
	//metodos
	
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		
		if(data != null && data.has("timeslot")) {
			timeSlot = data.getInt("timeslot");
		}
		else {
			timeSlot = 1;
		}
		
		return new MostCrowdedStrategy(timeSlot);
	}

}
