package simulator.model;

public class NewJunctionEvent extends Event{
	
	//atributos
	
	private String id;
	
	private LightSwitchingStrategy estrategiaCambio;
	
	private DequeuingStrategy estrategiaExtraer;
	
	private int x;
	
	private int y;
	
	
	//constructor

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		
		this.id = id;
		this.estrategiaCambio = lsStrategy;
		this.estrategiaExtraer = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
	}


	//metodo heredado
	
	void execute(RoadMap map) {
		
		map.addJunction(new Junction(id, estrategiaCambio, estrategiaExtraer, x, y));	
	}
	
	public String toString() {
		
		return "New Junction '"+ id +"'";
	}

}
