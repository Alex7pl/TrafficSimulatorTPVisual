package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject{
	
	//ATRIBUTOS de clase Vehiculo:
	
	//lista de cruces por los que pasara el vehiculo.
	private List<Junction> itinerario;
	private int indiceUltJunction;
	
	//velocidad maxima
	private int velocidad_maxima;
	
	//velocidad a la que va el vehiculo:
	private int velocidad_actual;
	
	//estado actual del vehiculo(tipo enum)
	private VehicleStatus estado;
	
	//carretera sobre la que circula el vehiculo:
	private Road carretera;
	
	//localizacion: distancia desde el comienzo de la carretera,
	//comienzo es la localizacion 0.
	private int localizacion;
	
	//0-10, lo que emite en cada paso de la simulacion
	private int grado_contaminacion;
	
	//contaminacion total: total emitido durante su trayectoria:
	private int contaminacion_total;
	
	private int distancia_total;
	

	//constructor:
		Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
			super(id);
			this.indiceUltJunction = 0;
			
			//debemos comprobar que se tienen valores validos:
			if(maxSpeed > 0) this.velocidad_maxima = maxSpeed;
			else throw new IllegalArgumentException(String.format("[ERROR]: ","La velocidad maxima debe ser positiva"));
			
			if(0 <= contClass && contClass <=10) this.grado_contaminacion = contClass;
			else throw new IllegalArgumentException(String.format("[ERROR]: ","ClassCont debe situarse entre 0 y 10"));
			
			
			if(itinerary.size() >= 2) {
				
				//no se debe compartir el argumento itinerary, sino que debes hacer una copia
				this.itinerario = Collections.unmodifiableList(new ArrayList<>(itinerary));
			}
			else {
				//lanzo excepcion:
				throw new IllegalArgumentException(String.format("[ERROR]: ","El itinerario del vehiculo ", this.getId(), " es muy pequeño"));
			}
			
			//pongo el estado "inicial", luego, tambien pongo la velocidad a 0:
			this.estado = VehicleStatus.PENDING;
			
			this.velocidad_actual = 0;
			
			this.contaminacion_total = 0;
			
			this.distancia_total = 0;
			
		}
	
	//Getters:
		
	public int getLocation() {
		return this.localizacion;
	}
	
	public int getSpeed() {
		return this.velocidad_actual;
	}
	
	public int getMaxSpeed(){
		return this.velocidad_maxima;
	}
	
	public int getContClass() {
		return this.grado_contaminacion;
	}
	
	public VehicleStatus getStatus() {
		return this.estado;
	}
	
	public int getTotalCO2() {
		return this.contaminacion_total;
	}
	
	public List<Junction> getItinerary(){
		return this.itinerario;
	}
	
	public Road getRoad() {
		return this.carretera;
	}
	
	public int getDistance() {
		return this.distancia_total;
	}
	
	public int getIndiceUltJunc() {
		return this.indiceUltJunction;
	}
	
	public void setSpeed(int speed) {
		
		if(this.estado.name() == "TRAVELING") {
			if(speed < 0) {
				//lanza EXCEPCION
				throw new IllegalArgumentException(String.format("[ERROR]: ","La velocidad a actualizar debe ser mayor a 0"));
			}	
			else
				//saco el valor MINIMO!! entre...
				this.velocidad_actual = (speed <= this.velocidad_maxima) ? speed : this.velocidad_maxima;
		}	
	}
	
	public void setContClass(int cont) {
	
		if(cont < 0 || cont > 10) {
			//Lanza EXCEPCION
			throw new IllegalArgumentException(String.format("[ERROR]: ","La contaminacion a actualizar debe estar entre 0 y 10"));	
		}
		else this.grado_contaminacion = cont;
	}
	
	@Override
	public void advance(int time) {
		
		//solo avanza si el estado es TRAVELLING, si estoy en PENDNING, ARRIVED o WAITING no entro en el if (no hago nada)
		if(this.estado.name() == "TRAVELING") {
			
			//-----a) se actualiza LOCALIZACION:---------------------------
			
			int newLocation;
			int oldLocation = this.localizacion;
			
			newLocation = ((this.localizacion + this.velocidad_actual) < this.carretera.getLength()) ? 
					(this.localizacion + this.velocidad_actual) : this.carretera.getLength();
			
			this.distancia_total = distancia_total + (newLocation - this.localizacion);
			
			this.localizacion = newLocation;
			
			//-----b) CALCULAMOS LA CONTAMINACION:--------------------------
			//con la formula c = l * f			
			
			// f = grado de contaminacion (this.grado_contaminacion)
			// l = distancia recorrida 
			 
			int c, l;
			l = newLocation - oldLocation;
			c = this.grado_contaminacion * l;
			
			this.contaminacion_total += c;
			
			this.carretera.addContamination(c);
			
			//-----c) VEMOS SI ENTRA EN NUEVO CRUCE:-------------------------
			
			if(this.localizacion >= this.carretera.getLength()) {
				
				//el vehiculo entra en el cruce correspondiente: Salgo de esta carretera y entro en la siguiente:
				this.indiceUltJunction++;
				
				//busco la junction que tenga como carretera entrante a la ACTUAL y lo meto en su cola:
				
				this.velocidad_actual = 0;
				
				this.itinerario.get(indiceUltJunction).enter(this);
				
				//modificamos el estado del vehiculo:
				this.estado = VehicleStatus.WAITING;
				
			}
			
		}
	
	}
	
	public void moveToNextRoad() {
		//ME RECORRO LISTA JUNCTION BUSCANDO LA JUNCTION EN LA QUE ESTÃƒï¿½ LA CARRETERA
		//ACTUAL Y PASO A LA SIGUIENTE DEL ITINERARIO
		
		if(this.estado.name() == "WAITING" || this.estado.name() == "PENDING") {
				
			//primero salgo (siempre que no este en la primera invocacion):
			if(this.indiceUltJunction != 0) this.carretera.exit(this);
				
			//y ahora entro (si no estoy en el ultimo del itinerario)
			if(this.indiceUltJunction + 1 < this.itinerario.size()) {
				
				this.localizacion = 0;
				int nextJunction = this.indiceUltJunction + 1;
				this.carretera = this.itinerario.get(indiceUltJunction).roadTo(this.itinerario.get(nextJunction));
				this.carretera.enter(this);
				this.estado = VehicleStatus.TRAVELING;
			}
			else {
				//ya estoy en el ultimo junction. Simplemente salgo:
				this.estado = VehicleStatus.ARRIVED; //ya he llegado
			}
			
		}else {
			throw new IllegalArgumentException(String.format("[ERROR]: ","El vehiculo esta en movimiento"));
		}
	}
	
	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		JSONObject vehicleJSON = new JSONObject();
		
		vehicleJSON.put("id", this._id);
		vehicleJSON.put("speed", this.velocidad_actual);
		vehicleJSON.put("distance", this.distancia_total);
		vehicleJSON.put("co2", this.contaminacion_total);
		vehicleJSON.put("class", this.grado_contaminacion);
		vehicleJSON.put("status", this.estado.toString());
			
		if(this.estado.name() != "PENDING" && this.estado.name() != "ARRIVED") {
			vehicleJSON.put("road", this.carretera.getId());
			vehicleJSON.put("location", this.localizacion);
		}	
		
		return vehicleJSON;
	}
}

