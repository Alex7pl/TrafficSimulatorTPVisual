package simulator.model;

public abstract class NewRoadEvent extends Event{
	
	//atributos
	
	protected String id_;
	
	protected String src;
	
	protected String dest;
	
	protected int length;
	
	protected int co2;
	
	protected int speed;
	
	protected Weather tiempo;
	
	
	//constructor

	public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time);
		
		this.id_ = id;
		this.src = srcJunc;
		this.dest = destJunc;
		this.length = length;
		this.co2 = co2Limit;
		this.speed = maxSpeed;
		this.tiempo = weather;
	}

	
	//metodos
	
	void execute(RoadMap map) {
		
		Junction srcJ = map.getJunction(this.src);
		Junction destJ = map.getJunction(this.dest);

		Road c = createRoadObject(srcJ, destJ);
		map.addRoad(c);
	
	}
	
	abstract protected Road createRoadObject(Junction srcJ, Junction destJ);
	abstract public String toString();
}
