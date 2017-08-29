package entidades;

public class Persona {

	private int id;
	private String nombre;
	private String apellido;
	private String dni;
	private boolean habilitado;
	private String categoria;
	private String usuario;
	private String password;
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public boolean isHabilitado() {
		return habilitado;
	}
	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	
	public Persona(){
		
	}
	
	
	public Persona(String nombre, String apellido, String dni, boolean habilitado, String categoria, String usuario, String password) {
		super();
		this.setNombre(nombre);
		this.setApellido(apellido);
		this.setDni(dni);
		this.setHabilitado(habilitado);
		this.setCategoria(categoria);
		this.setPassword(password);
		this.setUsuario(usuario);
		}
	
	@Override
	public boolean equals(Object o){
		return (o instanceof Persona) &&
				(((Persona)o).getDni().equals(this.getDni()));
		
		 

		
	}
	
}
