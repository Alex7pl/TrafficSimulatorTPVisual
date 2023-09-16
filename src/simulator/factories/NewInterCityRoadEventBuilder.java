package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
		// TODO Auto-generated constructor stub
	}

	@Override
	Event createTheRoad() {
		// TODO Auto-generated method stub
		return new NewInterCityRoadEvent(this.time, this.id_, this.src, this.dest, this.length, this.co2, this.speed, this.tiempo);
	}
}
