package simulator.model;

public class CityRoad extends Road{
	
	//constructor

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	//metodos
	
	void reduceTotalContamination() {
		
		int o;
		
		if(this.ambiente == Weather.WINDY || this.ambiente == Weather.STORM) o = 10;
		else o = 2;
		
		this.totalCont -= o;
		
		if(this.totalCont < 0) this.totalCont = 0;
	}


	void updateSpeedLimit() {
		
		this.limitSpeed = this.maxSpeed;
	}


	int calculateVehicleSpeed(Vehicle v) {
		
		int velocidad = 0;
		
		if(v.getStatus() == VehicleStatus.TRAVELING) {
			
			velocidad = ((11 - v.getContClass())* this.limitSpeed) / 11;
		}
		
		return velocidad;
	}

}
