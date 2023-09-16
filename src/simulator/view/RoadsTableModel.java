package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private List<Road> _roads;
	private String[] _colNames = { "Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"};

	public RoadsTableModel(Controller c) {
		
		_ctrl = c;
		_roads= new ArrayList<Road>();
		_ctrl.addObserver(this);
	}

	public void update() {

		fireTableDataChanged();;		
	}
	
	public void setRoadsList(List<Road> roads) {
		_roads = roads;
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
		return _roads == null ? 0 : _roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			//id:
			s = _roads.get(rowIndex).getId();
			break;
		case 1:
			//longitud:
			s = _roads.get(rowIndex).getLength();
			break;
		case 2:
			//weather de la road:
			s =_roads.get(rowIndex).getWeather().toString();
			break;
		case 3:
			//max speed:
			s = _roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:	
			//speed limit:
			s = _roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			//co2 acumulado:
			s = _roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			//cont limit:
			s = _roads.get(rowIndex).getContLimit();
			break;
			
		}
		return s;
	}	

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setRoadsList(map.getRoads());
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
				setRoadsList(map.getRoads());
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
				setRoadsList(map.getRoads());
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
				setRoadsList(map.getRoads());
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
				setRoadsList(map.getRoads());
			}
		});
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}

