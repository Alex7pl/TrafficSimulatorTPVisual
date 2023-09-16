package simulator.view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ChangeWeatherDialog extends  JDialog{
	
	private JComboBox<Road> carreteras;
	private JComboBox<Weather> weather;
	
	private DefaultComboBoxModel<Road> modeloC;
	private DefaultComboBoxModel<Weather> modeloW;
	
	private JSpinner time;
	
	private int _status;
	
	public ChangeWeatherDialog(Frame frame) {
		
		super(frame, true);
		initGUI();
	}

	private void initGUI() {
		
		setTitle("Change Road Weather");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JLabel msg = new JLabel("<html><p>Schedule an event to change the weather class of a road after a given number of simulation ticks form now.</p></html>");
		msg.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(msg);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel okPanel = new JPanel();
		okPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(okPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		this.modeloC = new DefaultComboBoxModel<>();
		this.carreteras = new JComboBox<>(modeloC);
		
		this.modeloW = new DefaultComboBoxModel<>();
		this.weather = new JComboBox<>(modeloW);
		
		JLabel rText = new JLabel("Road: ");
		rText.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(rText);
		buttonPanel.add(carreteras);
		
		JLabel wText = new JLabel("Weather: ");
		wText.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(wText);
		buttonPanel.add(weather);
		
		time = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		time.setMaximumSize(new Dimension(80, 40));
		time.setMinimumSize(new Dimension(80, 40));
		time.setPreferredSize(new Dimension(80, 40));
		
		JLabel ticksT = new JLabel("Ticks: ");
		ticksT.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(ticksT);
		buttonPanel.add(time);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		okPanel.add(cancelButton);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(carreteras.getSelectedItem() != null && weather.getSelectedItem() != null) {
					
					_status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		okPanel.add(okButton);
		
		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(RoadMap mapa) {
		
		for(Road r : mapa.getRoads()) {
			
			this.modeloC.addElement(r);
		}
		
		for(Weather w : Weather.values()) {
			
			this.modeloW.addElement(w);
		}
		
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		
		setVisible(true);
		return _status;
	}
	
	Road getRoad() {
		
		return (Road) carreteras.getSelectedItem();
	}
	
	Weather getWeather() {
		
		return (Weather) weather.getSelectedItem();
	}
	
	int getTicks() {
		
		return (Integer) time.getValue();
	}
}
