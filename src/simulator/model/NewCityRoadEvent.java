package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent{
	
	
	//constructor

	public NewCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}


	//metodo
	
	protected Road createRoadObject(Junction srcJ, Junction destJ) {

		return new CityRoad(this.id_, srcJ, destJ, this.speed, this.co2, this.length, this.tiempo);
	}

	public String toString() {
		// TODO Auto-generated method stub
		return "New City Road '" + this.id_ + "'";
	}
}
