package simulator.factories;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder{

	public NewCityRoadEventBuilder() {
		super("new_city_road");
		
	}

	@Override
	Event createTheRoad() {
		// TODO Auto-generated method stub
		return new NewCityRoadEvent(this.time, this.id_, this.src, this.dest, this.length, this.co2, this.speed, this.tiempo);
	}
	
	

}
