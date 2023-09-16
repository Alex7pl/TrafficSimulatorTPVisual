package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveAllStrategy;

public class MoveAllStrategyBuilder extends Builder<DequeuingStrategy>{
		
		//constructora:
	
	
		public MoveAllStrategyBuilder() {
			super("move_all_dqs");
			
		}
		

		protected MoveAllStrategy createTheInstance(JSONObject data) {
			
			//omito el data
			return new MoveAllStrategy();
		}
		
	
	
	
}

