package data;

import java.sql.*;
import java.util.ArrayList;
import entidades.*;
import util.AppDataException;
import javax.swing.JOptionPane;
import java.security.KeyStore.ProtectionParameter;

public class DataElementos {
	
	
	public ArrayList<Elementos> getAll() throws Exception{
				
				Statement stmt=null;
				ResultSet rs=null;
				ArrayList<Elementos> elementos = new ArrayList<Elementos>();
				try {
					  stmt = FactoryConexion.getInstancia().getConn().createStatement();
					  rs = stmt.executeQuery("select * from elementos");
					  
				if (rs != null) {
						while (rs.next()) {
								Elementos el = new Elementos();
								el.setId_elemento(rs.getInt("id_elemento"));
								el.setNombre(rs.getString("nombre"));
								el.setStock(rs.getInt("stock"));
								el.setAutor(rs.getString("autor"));
								el.setGenero(rs.getString("genero"));
								el.setDescripcion(rs.getString("descripcion"));
								el.setId_tipoelemento(rs.getInt("id_tipoelemento"));
								
								elementos.add(el);
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
				
					return elementos;
					
					
				}
 
	
	public Elementos getByNombre(Elementos elementos) throws Exception{
	
			Elementos el = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				 /*al poner el signo de pregunta el driver se da cuenta que en ese lugar va a ir un parametro*/
				stmt = FactoryConexion.getInstancia().getConn().prepareStatement(
						"select e.id_elemento, e.nombre, e.stock, e.autor, e.genero, e.descripcion, e.id_tipoelementos from elementos e "
						+ "inner join tipo_elementos te on e.id_tipoelemento=te.id_tipoelemento where e.nombre=?");
						
				stmt.setString(1, elementos.getNombre());
				rs = stmt.executeQuery();
				
				if (rs!=null && rs.next()) {
					el = new Elementos();
					el.setId_elemento(rs.getInt("id_elemento"));   /* el dato que va como argumento tiene que ser igual al que esta en la base? */
					el.setNombre(rs.getString("nombre"));
					el.setStock(rs.getInt("stock"));
					el.setAutor(rs.getString("autor"));
					el.setGenero(rs.getString("genero"));
					el.setDescripcion(rs.getString("descripcion"));
					el.setId_tipoelemento(rs.getInt("id_tipoelemento"));
				
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
			
			return el;
		}
	
	
		
		public void add(Elementos el) throws Exception{
			PreparedStatement stmt=null;
			ResultSet keyResultSet=null;
			try {
				stmt=FactoryConexion.getInstancia().getConn()
						.prepareStatement(
						"insert into elementos(id_elemento, nombre, stock, autor, genero, descripcion, id_tipoelemento) values (?,?,?,?,?,?,?)",
						PreparedStatement.RETURN_GENERATED_KEYS
						);
				
				stmt.setString(1, el.getNombre());
				stmt.setInt(2, el.getStock());
				stmt.setString(3, el.getAutor());
				stmt.setString(4, el.getGenero());
				stmt.setString(5, el.getDescripcion());
				stmt.setInt(6, el.getId_tipoelemento());
				
				stmt.executeUpdate();
				
				keyResultSet=stmt.getGeneratedKeys();
				if(keyResultSet!=null && keyResultSet.next()){
					el.setId_elemento(keyResultSet.getInt(1));
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
		
		public void update(Elementos el) throws Exception{
			PreparedStatement stmt=null;
			
			try {
				stmt= FactoryConexion.getInstancia().getConn().prepareStatement(
						"update elementos set nombre=?, stock=?, autor=?, genero=?, descripcion=?, id_tipoelemento=? where id_elemento=?");
				
				stmt.setString(1, el.getNombre());
				stmt.setInt(2, el.getStock());
				stmt.setString(3, el.getAutor());
				stmt.setString(4, el.getGenero());
				stmt.setString(5, el.getDescripcion());
				stmt.setInt(6, el.getId_tipoelemento());
			
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
		
		public void delete(Elementos el) throws Exception{
			PreparedStatement stmt=null;
			
			try {
				stmt= FactoryConexion.getInstancia().getConn().prepareStatement(
						"delete from elementos where id_elemento=?");
				
				stmt.setInt(1, el.getId_elemento());
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
