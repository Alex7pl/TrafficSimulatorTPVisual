package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{
	
	//atributos
	
	private int timeSlot;

	public RoundRobinStrategyBuilder() {
		super("round_robin_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		
		if(data != null && data.has("timeslot")) {
			timeSlot = data.getInt("timeslot");
		}
		else {
			timeSlot = 1;
		}
		
		return new RoundRobinStrategy(timeSlot);
	}

}
