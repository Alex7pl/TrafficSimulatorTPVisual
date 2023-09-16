package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private List<Junction> _cruces;
	private String[] _colNames = { "Id", "Green", "Queues" };

	public JunctionsTableModel(Controller c) {
		
		_ctrl = c;
		_cruces= new ArrayList<Junction>();
		_ctrl.addObserver(this);
	}

	public void update() {

		fireTableDataChanged();;		
	}
	
	public void setJunctionsList(List<Junction> cruces) {
		_cruces = cruces;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	public int getRowCount() {
		return _cruces == null ? 0 : _cruces.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {

		case 0:
			s = _cruces.get(rowIndex).getId();
			break;
		case 1:
			s = greenRoad(_cruces.get(rowIndex));
			break;
		case 2:
			s = queuesL(_cruces.get(rowIndex));
			break;
		}
		return s;
	}
	
	private String greenRoad(Junction j) {
		
		String green;
		
		if(j.getGreenLightIndex() == - 1) { green = "NONE"; }
		else {
			
			List<Road> carreterasE = j.getInRoads();
			
			green = carreterasE.get(j.getGreenLightIndex()).getId();
		}
		
		return green;
	}
	
	private String queuesL(Junction j) {
		
		String queues = "";
		
		for(Road r : j.getInRoads()) {
			
			queues += r.getId() + ": [";
			
			for(Vehicle v : j.getColas().get(r)) {
				
				queues += v.getId() + ",";
			}
			
			queues += "] ";
		}
		
		return queues;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setJunctionsList(map.getJunctions());
			}
		});
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setJunctionsList(map.getJunctions());
			}
		});
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setJunctionsList(map.getJunctions());
			}
		});
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setJunctionsList(map.getJunctions());
			}
		});
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setJunctionsList(map.getJunctions());
			}
		});
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}

