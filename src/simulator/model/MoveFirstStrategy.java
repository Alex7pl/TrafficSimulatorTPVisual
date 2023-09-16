package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy{
	
	//constructor
	
	public MoveFirstStrategy() {}

	//metodos

	public List<Vehicle> dequeue(List<Vehicle> q) {
		// metodo implementado de la interfaz, lo implementa de manera que 
		// devuelve el primer elemento de la lista EN UNA NUEVA LISTA.
		
		int i = 0;
		
		List<Vehicle> primerVehiculo = new ArrayList<Vehicle>();
		
		if(!q.isEmpty()) {
		
			primerVehiculo.add(q.get(i));
		}
	
		return primerVehiculo;
	}
}

