package negocio;

import java.util.ArrayList;

import java.sql.Date;
import java.text.SimpleDateFormat;

import data.DataReservas;
import entidades.Elemento;
import entidades.Reserva;
import entidades.Persona;
import entidades.Tipo_Elemento;

public class ReservasLogic {

	
	private Reserva reservas;
	private DataReservas reservasD;
	ArrayList<Reserva> lista = new ArrayList<Reserva>();
	


	public ReservasLogic(){
	
		reservas = new Reserva(); 
		reservasD = new DataReservas();
		
	}


	public void add(Reserva res) throws Exception{
	
		
		reservasD.add(res);
	
	}
	
	public void delete(Reserva res)throws Exception{
		
		this.reservasD.delete(res);
	}
	
	public void update(Reserva res)throws Exception{
		
		this.reservasD.update(res);
	}


	
	public Reserva GetByIdPersona(Reserva res) throws Exception{
		
		
		return reservasD.getByIdPersona(res);
	
	}
	
	
	public Reserva GetByNombre (int id_persona, int id_elemento) throws Exception{
		
		Reserva res=new Reserva();
		res.getPersona().setId_persona(id_persona);
		res.getElemento().setId_elemento(id_elemento);
		return GetByIdPersona(res);
		
		
		
	}
	
	public Reserva GetOne(int id_persona,int id_elemento,Date fecharegistro) throws Exception{
		
		return reservasD.GetOne(id_persona,id_elemento,fecharegistro); 
		
	}
	
	public ArrayList<Reserva> getByUsuario(Persona per) throws Exception{
		
		return reservasD.getByUsuario(per);
		
	}
	
	
	public ArrayList<Reserva> GetAll() throws Exception{
	
			return reservasD.getAll();
			
	}

	public boolean ValidarCantidadReservasPendientes(int idpersona,int idtipoelemento) throws Exception{
			
			Reserva res = new Reserva();
			
			res.getPersona().setId_persona(idpersona);
			res.getElemento().getTipo_Elemento().setId_tipoelemento(idtipoelemento);
			
			return ValidarCantidadReservasPendientes(res);
			
	}
		
		
		
	public boolean ValidarCantidadReservasPendientes(Reserva res) throws Exception{
			
			
			int cantReservasPendPersona = reservasD.getReservasPendientes(res);
			if(cantReservasPendPersona < res.getElemento().getTipo_Elemento().getCantMaxReservasPend())
			{return true;}
			else
			{return false;}
		
		
	}


	public ArrayList<Elemento> getElementosSinReserva(Date fechainicio,Date fechafin,Date fecharegistro,int idtipoelemento) throws Exception{
			
			return reservasD.getElementosSinReserva(fechainicio, fechafin,fecharegistro, idtipoelemento);
			
			
			
	}
		
		
	public void actualizarEstadoReservas() throws Exception{
			
		    ArrayList<Reserva> listado = new ArrayList<>();
		    listado = this.GetAll();
		
			java.util.Date FechaDelSistema = new java.util.Date(); /*Tomo la hora del sistema*/
			java.sql.Date fechaActual = new java.sql.Date(FechaDelSistema.getTime()); /* A la hora del sistema la convierto en el formato que trae la base */

			
			
			for (Reserva r : listado) {
				
				java.sql.Date fechafin = new java.sql.Date(r.getFecha_fin().getTime());
				 
				  if(fechafin.before(fechaActual) && r.getEstado().equals("Activa")){	
						
					  r.setEstado("Sin devolver");
					  this.update(r);
					}
					
				}
			
	}
	
	
	



}
