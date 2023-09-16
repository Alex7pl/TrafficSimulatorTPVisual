package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy{
	
	
	//constructor
	
	public MoveAllStrategy() {}

	
	//metodos

	public List<Vehicle> dequeue(List<Vehicle> q) {
		// metodo implementado de la interfaz.
		//devuelve TODOS los elementos de la LISTA, pero no devuelve la lista en si.
		
		List<Vehicle> copy = new ArrayList<Vehicle>(q);
		
		return copy;
	}
}

