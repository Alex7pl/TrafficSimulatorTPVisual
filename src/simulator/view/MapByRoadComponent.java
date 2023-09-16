package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{
	
	
	private static final long serialVersionUID = 1L;
	
	private static final int _JRADIUS = 10;
	
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;
	private Controller _ctrl;
	
	private Image _car;
	private Image _roadCont;
	private Image _weather;
	
	public MapByRoadComponent(Controller c) {
		
		_ctrl = c;
		initGui();
		_ctrl.addObserver(this);
	}
	
	private void initGui() {
		
		_car = loadImage("car.png");
	}
	
	public String weatherIcon(Weather weather) {
		String w = null;
		
		switch(weather) {
			case SUNNY:
				w = "sun.png";
				break;
				
			case CLOUDY:
				w = "cloud.png";
				break;
				
			case RAINY:
				w = "rain.png";
				break;
				
			case WINDY:
				w = "wind.png";
				break;
				
			case STORM:
				w = "storm.png";
				break;
		}
		
		return w;
	}
	
	public String contIcon(int totalCont, int contLimit) {
		
		int c = (int) Math.floor(Math.min((double) totalCont/(1.0 + (double) contLimit),1.0) / 0.19);
		
		StringBuilder str = new StringBuilder();
		
		str.append("cont_").append(c).append(".png");
		
		String icon = str.toString();
		
		return icon;
	}

	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
		drawJunctions(g);
	}
	
	private void drawRoads(Graphics g) {
		
		int i = 0;
		
		for (Road r : _map.getRoads()) {
			
			// the road goes from (x1,y1) to (x2,y2)
			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i + 1) * 50;

			//Ponemos color a negro segun RGB
			Color roadColor = new Color(0, 0, 0);

			// draw line from (x1,y1) to (x2,y2) with arrow of color arrowColor and line of
			// color roadColor. The size of the arrow is 15px length and 5 px width
			drawLine(g, x1, y, x2, roadColor);
			
			//Pinto id de la carretera
			g.drawString(r.getId(), x1 - 30, (i + 1) * 51);
			
			//pinto vehiculos en esa road
			drawVehicles(g, r, y);
			
			//ahora pintaremos los dos iconos (weather y cont. carretera):
			_weather = loadImage(this.weatherIcon(r.getWeather()));
			g.drawImage(_weather, x2 + 10, y-16, 32, 32, this);
			
			_roadCont = loadImage(this.contIcon(r.getTotalCO2(), r.getContLimit()));
			g.drawImage(_roadCont, x2 + 50, y-16, 32, 32, this);

			
			i++;
		}

	}
	
	private void drawVehicles(Graphics g, Road r, int y) {
		
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				if (r == v.getRoad()) {
	
					// The calculation below compute the coordinate (vX,vY) of the vehicle on the
					// corresponding road. It is calculated relatively to the length of the road, and
					// the location on the vehicle.
					int x1v = 50;
					int x2v = getWidth() - 100;

					int vX = x1v + (int) ((x2v - x1v) * ((double)v.getLocation() / (double)r.getLength()));
					int vY = y;

					// draw an image of a car and it identifier
					g.drawImage(_car, vX, vY - 10 , 16, 16, this);
					
					int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
					g.setColor(new Color(0, vLabelColor, 0));
					g.drawString(v.getId(), vX, vY - 10 );
				}
			}
		}
	}

	
	private void drawJunctions(Graphics g) {
		
		int i = 0;
		
		for (Road r : _map.getRoads()) {
			
			Junction org, dest;
			
			org = r.getSrc();
			dest = r.getDest();

			// (x,y) are the coordinates of the junction
			int x1 = 50;
			int y1 = (i + 1) * 50;

			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y1 - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(org.getId(), x1, y1);
			
			int x2 = getWidth() - 100;
			int y2 = (i + 1) * 50;

			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(selectColor(r));
			g.fillOval(x2 - _JRADIUS / 2, y2 - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// draw the junction's identifier at (x,y)
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(dest.getId(), x2, y2);
			
			i++;
		}
	}
	
	private Color selectColor(Road r) {
		
		Color semaforo = _RED_LIGHT_COLOR;
		int idx = r.getDest().getGreenLightIndex();
		if (idx != -1 && r.equals(r.getDest().getInRoads().get(idx))) {
			semaforo = _GREEN_LIGHT_COLOR;
		}
		
		return semaforo;
	}
	
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}
	}
	
	private void drawLine(//
			Graphics g, //
			int x1, int y, //
			int x2, Color lineColor) {

		g.setColor(lineColor);
		g.drawLine(x1, y, x2, y);
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			_map = map;
			repaint();
		});
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}
