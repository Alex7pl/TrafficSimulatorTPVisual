package simulator.view;

import java.awt.Dimension;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


import simulator.model.RoadMap;
import simulator.model.Vehicle;


@SuppressWarnings("serial")
public class ChangeCO2ClassDialog extends JDialog{

	private JComboBox<Vehicle> vehicles;
	
	private JComboBox<Integer> co2classes;
	
	private DefaultComboBoxModel<Vehicle> modeloV;
	
	private DefaultComboBoxModel<Integer> modeloco2;
	
	private JSpinner time;
	
	private int _status;

	
	public ChangeCO2ClassDialog(Frame frame) {
			
			super(frame, true);
			initGUI();
	}
		
	private void initGUI() {
		
		setTitle("Change CO2 Class");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JLabel msg = new JLabel(
				"<html><p>Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.</p></html>");
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
		
		this.modeloV = new DefaultComboBoxModel<>();
		this.vehicles = new JComboBox<>(modeloV);
		
		this.modeloco2 = new DefaultComboBoxModel<>();
		this.co2classes = new JComboBox<>(modeloco2);
		
		JLabel vehicleText = new JLabel("Vehilce:");
		vehicleText.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(vehicleText);
		buttonPanel.add(vehicles);
		
		JLabel co2Text = new JLabel("CO2 Class: ");
		co2Text.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.add(co2Text);
		buttonPanel.add(co2classes);
		
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
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		okPanel.add(cancelButton);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(vehicles.getSelectedItem() != null && vehicles.getSelectedItem() != null) {
					
					_status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
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
		
		for(Vehicle v : mapa.getVehicles()) {
			this.modeloV.addElement(v);
		}
		
		for(int i = 0; i <= 10; i++) {
			this.modeloco2.addElement(i);
		}
		
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		
		setVisible(true);
		return _status;
		
	}
	
	Vehicle getSelectedVehicle() {
		
		return (Vehicle) vehicles.getSelectedItem();
	}
	
	int getSelectedCO2Class() {
		
		return (Integer) co2classes.getSelectedItem();
	}
	
	int getTicks() {
		
		return (Integer) time.getValue();
	}
		
}
