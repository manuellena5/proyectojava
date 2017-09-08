package data;

import java.sql.*;
import java.util.ArrayList;
import entidades.*;
import util.AppDataException;
import javax.swing.JOptionPane;
import java.security.KeyStore.ProtectionParameter;

public class DataReservas {
	
	
	public ArrayList<Reserva> getAll() throws Exception{
				
				Statement stmt=null;
				ResultSet rs=null;
				ArrayList<Reserva> reservas = new ArrayList<Reserva>();
				try {
					  stmt = FactoryConexion.getInstancia().getConn().createStatement();
					  rs = stmt.executeQuery("select r.`id_persona`,r.`id_elemento`, r.`fecha_inicio`,r.`fecha_fin`,r.`detalle`,r.`estado`,"
					  		+ "r.`fecha_registro`,p.`nombre` nombrePersona,p.`apellido`,p.`dni`,p.`usuario`,e.`nombre` nombreElemento,"
							+ "e.`autor`,e.`genero`, te.nombre tipoelemento from reservas r inner join personas p on p.`id_persona` = r.`id_persona`" 
					  		+ "inner join elementos e on e.`id_elemento` = r.`id_elemento`"
							+ "inner join tipo_elementos te on te.`id_tipoelemento` = e.`id_tipoelemento`");
					  
					  
					  
					  
				if (rs != null) {
						while (rs.next()) {
								Reserva res = new Reserva();
								res.setElemento(new Elemento());
								res.setPersona(new Persona());
																
								res.setFecha_registro(rs.getDate("fecha_registro"));
								res.setFecha_inicio(rs.getDate("fecha_inicio"));
								res.setFecha_fin(rs.getDate("fecha_fin"));
								res.setDetalle(rs.getString("detalle"));
								res.setEstado(rs.getString("estado"));
								
								
								
								res.getElemento().setId_elemento(rs.getInt("id_elemento"));
								res.getElemento().setNombre(rs.getString("nombreElemento"));
								res.getElemento().setAutor(rs.getString("autor"));
								res.getElemento().setGenero(rs.getString("genero"));
								/*res.getElemento().getTipo_Elemento().setNombre(rs.getString("tipoelemento")); Como hacer para traer el tipo de elemento*/ 
								
								res.getPersona().setId_persona(rs.getInt("id_persona"));
								res.getPersona().setNombre(rs.getString("nombrePersona"));
								res.getPersona().setApellido(rs.getString("apellido"));
								res.getPersona().setDni(rs.getString("dni"));
								res.getPersona().setUsuario(rs.getString("usuario"));
								
								
								
								reservas.add(res);
										}
					}
				
				}catch (SQLException e) {
					
					throw e;
				}
				catch (AppDataException ade){
					throw ade;
				}
				
				try {
					if(rs!=null) rs.close();
					if(stmt!=null) stmt.close();
					FactoryConexion.getInstancia().releaseConn();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				
					return reservas;
					
					
				}
 
	
	public Reserva getByIdPersona(Reserva reservas) throws Exception{
	
			Reserva res = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				 /*al poner el signo de pregunta el driver se da cuenta que en ese lugar va a ir un parametro*/
				stmt = FactoryConexion.getInstancia().getConn().prepareStatement(
						"select p.id_persona, e.id_elemento, r.fecha_registro, r.fecha_inicio, r.fecha_fin, r.detalle, r.estado from reservas r "
						+ "inner join elementos e on e.id_elemento=r.id_elemento inner join personas p on p.id_persona=r.id_persona where r.id_persona=?");
						
				stmt.setInt(1, reservas.getPersona().getId_persona());
				rs = stmt.executeQuery();
				
				if (rs!=null && rs.next()) {
					res = new Reserva();
					res.setElemento(new Elemento());
					res.setPersona(new Persona());
					res.setFecha_registro(rs.getDate("fecha_registro"));
					res.setFecha_inicio(rs.getDate("fecha_inicio"));
					res.setFecha_fin(rs.getDate("fecha_fin"));
					res.setDetalle(rs.getString("detalle"));
					res.setEstado(rs.getString("estado"));
					
					res.getElemento().setId_elemento(rs.getInt("id_elemento"));
					res.getElemento().setNombre(rs.getString("nombreElemento"));
					res.getElemento().setAutor(rs.getString("autor"));
					res.getElemento().setGenero(rs.getString("genero"));
					
					res.getPersona().setId_persona(rs.getInt("id_persona"));
					res.getPersona().setNombre(rs.getString("nombrePersona"));
					res.getPersona().setApellido(rs.getString("apellido"));
					res.getPersona().setDni(rs.getString("dni"));
					res.getPersona().setUsuario(rs.getString("usuario"));
				
				}
				
				
			
			
			} catch (Exception e) {
				
				throw e;
			}
			
				try {
					if (rs != null) {rs.close();}
					if (stmt != null) {stmt.close();}
					FactoryConexion.getInstancia().releaseConn();	
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
			return res;
		}
	
	
		
		public void add(Reserva res) throws Exception{
			PreparedStatement stmt=null;
			ResultSet keyResultSet=null;
			try {
				stmt=FactoryConexion.getInstancia().getConn()
						.prepareStatement(
						"insert into reservas(fecha_registro, fecha_inicio, fecha_fin, detalle, estado) values (?,?,?,?,?)",
						PreparedStatement.RETURN_GENERATED_KEYS
						);
				
				stmt.setDate(1, res.getFecha_registro());
				stmt.setDate(2, res.getFecha_inicio());
				stmt.setDate(3, res.getFecha_fin());
				stmt.setString(4, res.getDetalle());
				stmt.setString(5, res.getEstado());
				
				stmt.executeUpdate();
				
				keyResultSet=stmt.getGeneratedKeys();
				if(keyResultSet!=null && keyResultSet.next()){
					res.getPersona().setId_persona(keyResultSet.getInt(1));
					res.getElemento().setId_elemento(keyResultSet.getInt(2));
				}
			} catch (SQLException | AppDataException e) {
				throw e;
			}
			try {
				if(keyResultSet!=null)keyResultSet.close();  /* preguntar que hace esta linea */ 
				if(stmt!=null)stmt.close();
				FactoryConexion.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public void update(Reserva res) throws Exception{
			PreparedStatement stmt=null;
			
			try {
				stmt= FactoryConexion.getInstancia().getConn().prepareStatement(
						"update reservas set fecha_registro=?, fecha_inicio=?, fecha_fin=?, detalle=?, estado=? where id_persona=? and id_elemento=?");
				
				stmt.setDate(1, res.getFecha_registro());
				stmt.setDate(2, res.getFecha_inicio());
				stmt.setDate(3, res.getFecha_fin());
				stmt.setString(4, res.getDetalle());
				stmt.setString(5, res.getEstado());
				stmt.setInt(6, res.getPersona().getId_persona());
				stmt.setInt(7, res.getElemento().getId_elemento());
			
				stmt.execute();
				
				
			} catch (SQLException | AppDataException e) {
				
				throw e;
			} 
			try {
				if(stmt!=null)stmt.close();
				FactoryConexion.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		
		public void delete(Reserva res) throws Exception{
			PreparedStatement stmt=null;
			
			try {
				stmt= FactoryConexion.getInstancia().getConn().prepareStatement(
						"delete from reservas where id_persona=? and id_elemento=?");
				
				stmt.setInt(1, res.getPersona().getId_persona());
				stmt.setInt(1, res.getElemento().getId_elemento());
				stmt.execute();
				
				
			} catch (SQLException | AppDataException e) {
				
				throw e;
			} 
			try {
				if(stmt!=null)stmt.close();
				FactoryConexion.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		 
}

