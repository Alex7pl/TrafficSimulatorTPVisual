package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	
	//atributos
	
	private int time;
	
	private String id;
	
	private LightSwitchingStrategy estrategiaCambio;
	
	private DequeuingStrategy estrategiaExtraer;
	
	private int x;
	
	private int y;
	
	private Factory<LightSwitchingStrategy> lss;
	
	private Factory<DequeuingStrategy> dqs;
	
	
	//constructor
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		
		lss = lssFactory;
		dqs = dqsFactory;
	}


	@Override
	protected Event createTheInstance(JSONObject data) {
		
		this.time = data.getInt("time");
		
		this.id = data.getString("id");
		
		JSONArray coorr = new JSONArray();
		
		coorr = data.getJSONArray("coor");
		
		this.x = coorr.getInt(0);
		this.y = coorr.getInt(1);
		
		JSONObject ls = new JSONObject();
		JSONObject dq = new JSONObject();
		
		ls = data.getJSONObject("ls_strategy");
		dq = data.getJSONObject("dq_strategy");
		
		this.estrategiaCambio = lss.createInstance(ls);
		this.estrategiaExtraer = dqs.createInstance(dq);
		
		return new NewJunctionEvent(this.time, this.id, this.estrategiaCambio, this.estrategiaExtraer, this.x, this.y);
	}
}
