package simulator.view;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;


@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements TrafficSimObserver{

	private Controller _ctrl;
	private RoadMap mapa;
	
	private JToolBar barraTareas;
	private int ticks;
	private String error;
	private boolean _stopped;
	private JButton loadB;
	private JFileChooser chooserLoad;
	private JButton co2B;
	private JButton climaB;
	private JButton runB;
	private JButton stopB;
	private JSpinner _time;
	private JButton exitB;
	
	public ControlPanel(Controller c) {
		
		_ctrl = c;
		_stopped = false;
		initGUI();
	}
	
	private void initGUI() {
		
		//creo la barra de tareas
		barraTareas = new JToolBar();
		barraTareas.setFloatable(false);
		
		
		this.setLayout(new BorderLayout());
		this.add(barraTareas);
		
		//boton load_events:------------------------------
		loadB = new JButton(new ImageIcon("./resources/icons/open.png"));
		loadB.setToolTipText("Loads roads, vehicles, junctions and events into the simulator");
		loadB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// lo que hará si pulso el botón, llama a open();
				load();
				}
			});
				
		barraTareas.add(loadB);
		barraTareas.addSeparator();
				
				
		//boton del CO2----------------------------------
		co2B = new JButton(new ImageIcon("./resources/icons/co2class.png"));
		co2B.setToolTipText("Change CO2 Class of a Vehicle");
		co2B.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				co2Act();
						
			}
		} );
		barraTareas.add(co2B);
		
		//codigo para boton del clima
		climaB = new JButton();
		climaB.setIcon(new ImageIcon("./resources/icons/weather.png"));
		climaB.setToolTipText("Change Weather of a Road");
		climaB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				climaAct();
			}
		});
		barraTareas.add(climaB);
		
		barraTareas.addSeparator();
		
		//Boton run
		runB = new JButton(new ImageIcon("./resources/icons/run.png"));
		runB.setToolTipText("Run the simulator");
		runB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				enableToolBar(false);
				_stopped = false;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						run_sim((Integer) _time.getValue());
					}
				});
			}
		});
		barraTareas.add(runB);
		
		//Boton stop
		stopB = new JButton(new ImageIcon("./resources/icons/stop.png"));
		stopB.setToolTipText("Stop the simulator");
		stopB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				stop();
			}
		});
		barraTareas.add(stopB);
		
		//Boton para ticks
		barraTareas.add(new JLabel("Ticks: "));
		_time = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		_time.setToolTipText("Simulation tick to run: 1-10000");
		_time.setMaximumSize(new Dimension(80, 40));
		_time.setMinimumSize(new Dimension(80, 40));
		_time.setPreferredSize(new Dimension(80, 40));
		_time.setToolTipText("Simulation tick to run: 1-10000");
		barraTareas.add(_time);
		
		barraTareas.addSeparator();
		barraTareas.add(Box.createHorizontalGlue());
		
		//Boton exit
		exitB = new JButton();
		exitB.setAlignmentX(RIGHT_ALIGNMENT);
		exitB.setToolTipText("Quit the simulation");
		exitB.setIcon(new ImageIcon("./resources/icons/exit.png"));
		exitB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				exit();
			}
		});
		barraTareas.add(exitB);
		
		this._ctrl.addObserver(this);
	}
	
	protected void load() {
		
		chooserLoad = new JFileChooser();
		//abro directamente el directorio de los archivos de eventos:
		chooserLoad.setCurrentDirectory(new File("./resources/examples/"));
		
		int op = chooserLoad.showOpenDialog((Frame) SwingUtilities.getWindowAncestor(this));
		//xxx OJO cual seria el contenedor.
		
		//si el usuario pincha en aceptar
		if(op == JFileChooser.APPROVE_OPTION) {
			
			JOptionPane.showMessageDialog(chooserLoad, "You chose: " + chooserLoad.getSelectedFile());
			//se podria no mostrar este dialogo
			
			File archivoACargar = chooserLoad.getSelectedFile();
			
			InputStream archivoEntrada;
			
			try {
				archivoEntrada = new FileInputStream(archivoACargar);
				_ctrl.reset();
				_ctrl.loadEvents(archivoEntrada);
				
			}catch(Exception e) {
				//aqui capturo si el fichero no existe o dan fallo reset o loadEvents como dice en el enunciado, 
				//por eso meto la construccion del fileInput y los dos metodos en el try{};
				JOptionPane.showMessageDialog(null, this.error);
			}
		}
		else {
			//si al final ha decidido cancelar el usuario:
			if(op == JFileChooser.CANCEL_OPTION) JOptionPane.showMessageDialog(chooserLoad, "Selection canceled");
			else JOptionPane.showMessageDialog(chooserLoad, "An error has occured");

		}
		
	}
	
	protected void co2Act() {
		
		ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog((Frame) SwingUtilities.getWindowAncestor(this));
		
		int status = dialog.open(this.mapa);
		
		if(status != 0) {
			
			List<Pair<String, Integer>> co2Changes = new LinkedList<Pair<String, Integer>>();
			Pair<String, Integer> cambio = new Pair<String, Integer>(dialog.getSelectedVehicle().getId(), dialog.getSelectedCO2Class());
			co2Changes.add(cambio);
			
			try {
				this._ctrl.addEvent(new SetContClassEvent(ticks + dialog.getTicks(), co2Changes));
			}catch(Exception e){
				 JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), this.error);
				
			}
			
		}
		
	}

	
	protected void climaAct() {
		
		 ChangeWeatherDialog dialog = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));
		 
		 int status = dialog.open(this.mapa);
		 
		 if(status != 0) {
			 
			 List<Pair<String,Weather>> ws = new LinkedList<Pair<String,Weather>>();
			 Pair<String,Weather> cambio = new Pair<String,Weather>(dialog.getRoad().getId(), dialog.getWeather());
			 ws.add(cambio);
			 
			 try {
				 this._ctrl.addEvent(new SetWeatherEvent(ticks + dialog.getTicks(), ws));
			 } catch(Exception e) {
				 JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(this), this.error);
			 }
		 }
	}
	
	private void run_sim(int n) {
		
		if (n > 0 && !_stopped) {
			
			try {
				
				_ctrl.run(1, null);
				Thread.sleep(80);
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(null, this.error);
				_stopped = true;
				return;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			
			enableToolBar(true);
			_stopped = true;
		}
	}
	
	private void enableToolBar(boolean b) {
		
		this.loadB.setEnabled(b);
		this.co2B.setEnabled(b);
		this.climaB.setEnabled(b);
		this.runB.setEnabled(b);
		this.exitB.setEnabled(b);
	}

	private void stop() {
		
		_stopped = true;
	}
	
	private void exit() {
		
		int n = JOptionPane.showConfirmDialog((Frame) SwingUtilities.getWindowAncestor(this), "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
		
		if(n == 0) {
			
			System.exit(0);
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				mapa = map;
				ticks = time;
			}
		});
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				mapa = map;
				ticks = time;
			}
		});
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				mapa = map;
				ticks = time;
			}
		});
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				mapa = map;
				ticks = time;
			}
		});
	}

	@Override
	public void onError(String err) {
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				error = err;
			}
		});
	}
}
