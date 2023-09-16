package simulator.view;


import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimObserver {
	
	private Controller _ctrl;
	
	private JLabel ticks;
	
	private JLabel event;
	
	
	//Constructor 
	public StatusBar(Controller c) {
		
		_ctrl = c;
		ticks = new JLabel("");
		event = new JLabel("");
		initGUI();
	}
	
	private void initGUI() {
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		//Muestra ticks
		JLabel tiempo = new JLabel("Time: ", JLabel.LEFT);
		this.add(tiempo);
		this.add(ticks);

		this.add(Box.createHorizontalStrut(150));
		
		this.add(event);
		
		_ctrl.addObserver(this);
	}

	
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ticks.setText(String.valueOf(time));
				event.setText("");
			}
		});
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ticks.setText(String.valueOf(time));
				event.setText("Event added " + e.toString());
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
				ticks.setText(String.valueOf(time));
				event.setText("Reseted");
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
				ticks.setText(String.valueOf(time));
				event.setText("Welcome!");
			}
		});
	}

	@Override
	public void onError(String err) {
		
	}
}
