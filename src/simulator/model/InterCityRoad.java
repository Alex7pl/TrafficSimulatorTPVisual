package simulator.model;

public class InterCityRoad extends Road{
	
	//constructor

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);	
	}


	//metodos
	
	void reduceTotalContamination() {
		
		this.totalCont = ((100 - tiempo())*this.totalCont)/100;
	}
	
	private int tiempo() {
		
		int i = 0;
		
		if(this.ambiente == Weather.SUNNY)
			i = 2;

		if(this.ambiente == Weather.CLOUDY)
			i = 3;

		if(this.ambiente == Weather.RAINY)
			i = 10;

		if(this.ambiente == Weather.WINDY)
			i = 15;

		if(this.ambiente == Weather.STORM)
			i = 20;
		
		return i;
	}


	void updateSpeedLimit() {
		
		if(this.totalCont > this.contLimit) this.limitSpeed = maxSpeed / 2;
		else this.limitSpeed = this.maxSpeed;
	}


	int calculateVehicleSpeed(Vehicle v) {
		
		if(this.ambiente == Weather.STORM) return (this.limitSpeed*8)/10;
		else return this.limitSpeed;
	}

}
