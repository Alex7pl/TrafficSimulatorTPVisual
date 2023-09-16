package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveFirstStrategy;

//xxx mirar de quien extende y tipo de clase Builder.
public class MoveFirstStrategyBuilder extends Builder<DequeuingStrategy>{
	
	//constructora:
	public MoveFirstStrategyBuilder() {
		super("move_first_dqs");
		
	}
	

	protected MoveFirstStrategy createTheInstance(JSONObject data) {

		//omito el data
		return new MoveFirstStrategy();
	}
	
}

