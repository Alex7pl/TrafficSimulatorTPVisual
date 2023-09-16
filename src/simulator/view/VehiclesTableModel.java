package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private List<Vehicle> _vehicles;
	private String[] _colNames = { "Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance" };

	public VehiclesTableModel(Controller c) {
		
		_ctrl = c;
		_vehicles= new ArrayList<Vehicle>();
		_ctrl.addObserver(this);
	}

	public void update() {

		fireTableDataChanged();;		
	}
	
	public void setVehiclesList(List<Vehicle> vehicles) {
		_vehicles = vehicles;
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
		return _vehicles == null ? 0 : _vehicles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			//id:
			s = _vehicles.get(rowIndex).getId();
			break;
		case 1:
			//localización:
			s = location(rowIndex);
			break;
		case 2:
			//itinerario:
			s =_vehicles.get(rowIndex).getItinerary().toString();
			break;
		case 3:
			//co2
			s = _vehicles.get(rowIndex).getContClass();
			break;
		case 4:	
			//max speed:
			s = _vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			//actual speed:
			s = _vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			//total co2:
			s = _vehicles.get(rowIndex).getTotalCO2();
			break;
		case 7:
			//distancia total:
			s = _vehicles.get(rowIndex).getDistance();
			break;
			
		}
		return s;
	}

	public String location(int rowIndex) {
		
		StringBuilder str = new StringBuilder();
		
		//if status del vehiculo es travelling
				//entonces hago string con el road y la location en la road
		//else si el status es waiting
				//entonces hago string que ponga Waiting:la junction en la que está waiting
		
		if(_vehicles.get(rowIndex).getStatus() == VehicleStatus.TRAVELING) {
			str.append(_vehicles.get(rowIndex).getRoad().getId()).append(":").append(_vehicles.get(rowIndex).getLocation());
		}
		else if(_vehicles.get(rowIndex).getStatus() == VehicleStatus.WAITING) {
			str.append("Waiting").append(":")
				.append(_vehicles.get(rowIndex).getItinerary().get(_vehicles.get(rowIndex).getIndiceUltJunc()).getId());
		}
		else if(_vehicles.get(rowIndex).getStatus() == VehicleStatus.ARRIVED) {
			str.append("Arrived");
		}
		else if(_vehicles.get(rowIndex).getStatus() == VehicleStatus.PENDING) {
			str.append("Pending");
		}
		
		String l = str.toString();
		
		return l;
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVehiclesList(map.getVehicles());
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
				setVehiclesList(map.getVehicles());
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
				setVehiclesList(map.getVehicles());
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
				setVehiclesList(map.getVehicles());
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
				setVehiclesList(map.getVehicles());
			}
		});
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}

