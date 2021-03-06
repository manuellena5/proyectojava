package ui;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.TableRowSorter;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import entidades.Elemento;
import entidades.Reserva;
import negocio.ReservasLogic;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;

public class Reservas extends JInternalFrame {
	private JTable table;
	private JTextField txtBuscar;
	private JComboBox comboFiltro;
	private TableRowSorter trsFiltro;
	private ArrayList<Reserva> lista; 
	ReservasLogic reservaLogic = new ReservasLogic();
	private String modo;
	
	public Reservas() {
		
		setTitle("Reservas");
		setClosable(true);
		setBounds(100, 100, 988, 483);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblBuscarPor = new JLabel("Buscar por:");
		
		comboFiltro = new JComboBox();
		comboFiltro.setModel(new DefaultComboBoxModel(new String[] {"ID_Elemento","ID_Persona","Fecha_Registro","Fecha_Inicio","Fecha_Fin","Estado","Apellido","Nombre_Persona","Dni","Elemento","Autor","Genero","Detalle"}));
		
		txtBuscar = new JTextField();
		
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				String cadena = (txtBuscar.getText());
                txtBuscar.setText(cadena);
                repaint();
                filtro();
			}
		});
		
		txtBuscar.setColumns(10);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnSalirClick();
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(lblBuscarPor)
							.addGap(18)
							.addComponent(comboFiltro, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
							.addGap(27)
							.addComponent(txtBuscar, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 923, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addContainerGap(29, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(849, Short.MAX_VALUE)
					.addComponent(btnSalir)
					.addGap(70))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtBuscar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboFiltro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblBuscarPor))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSalir)
					.addContainerGap(25, Short.MAX_VALUE))
		);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnAlta = new JButton("Alta");
		btnAlta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				modo = "Alta";
			
					try {
						ShowAbmReservas(modo);
					} catch (Exception e) {
						
						JOptionPane.showMessageDialog(rootPane, e.getMessage());
					}
				
				
			}

			
		});
		menuBar.add(btnAlta);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				modo = "Editar";
					if (table.getSelectedRow() == -1) {
						JOptionPane.showMessageDialog(btnEditar, "Debe seleccionar una reserva");}
					else{
					try {
						ShowAbmReservas(modo);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(rootPane, e1.getMessage());
					}
					}
			}
		});
		menuBar.add(btnEditar);
		
		try{
			this.lista = reservaLogic.GetAll();
		} catch (Exception e){
			JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	
		}
		initDataBindings();
		trsFiltro = new TableRowSorter(table.getModel());
        table.setRowSorter(trsFiltro);

	}
	
	private void ShowAbmReservas(String modo) throws Exception {
		
		ReservasAbm abm = new ReservasAbm();
		if (modo != "Alta") {
			
			int index = table.convertRowIndexToModel(table.getSelectedRow());
			abm.showAbmReservas(this.lista.get(index),modo);
						
			
			
		}
		this.getDesktopPane().add(abm);
		abm.setVisible(true);

		this.dispose();
		
	}
	
	
	public void filtro() {
        int columnaABuscar = 0;
        if (comboFiltro.getSelectedItem().toString() == "ID_Elemento") {
            columnaABuscar = 0;
        }
        if (comboFiltro.getSelectedItem().toString() == "ID_Persona") {
            columnaABuscar = 1;
        }
        if (comboFiltro.getSelectedItem().toString() == "Fecha_Registro") {
            columnaABuscar = 2;
        }
        if (comboFiltro.getSelectedItem() == "Fecha_Inicio") {
            columnaABuscar = 3;
        }
        if (comboFiltro.getSelectedItem() == "Fecha_Fin") {
            columnaABuscar = 4;
        }
        if (comboFiltro.getSelectedItem() == "Estado") {
            columnaABuscar = 5;
        }
        if (comboFiltro.getSelectedItem() == "Apellido") {
            columnaABuscar = 6;
        }
        if (comboFiltro.getSelectedItem() == "Nombre_Persona") {
            columnaABuscar = 7;
        }
        if (comboFiltro.getSelectedItem() == "Dni") {
            columnaABuscar = 8;
        }
        if (comboFiltro.getSelectedItem() == "Elemento") {
            columnaABuscar = 9;
        }
        if (comboFiltro.getSelectedItem() == "Autor") {
            columnaABuscar = 10;
        }
        if (comboFiltro.getSelectedItem() == "Genero") {
            columnaABuscar = 11;
        }
        if (comboFiltro.getSelectedItem() == "Detalle") {
            columnaABuscar = 12;
        }
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtBuscar.getText(), columnaABuscar));
    }
	
	
	protected void initDataBindings() {
		JTableBinding<Reserva, List<Reserva>, JTable> jTableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ, lista, table);
		//
		BeanProperty<Reserva, Integer> reservaBeanProperty = BeanProperty.create("elemento.id_elemento");
		jTableBinding.addColumnBinding(reservaBeanProperty).setColumnName("ID Elemento").setEditable(false);
		//
		BeanProperty<Reserva, Integer> reservaBeanProperty_1 = BeanProperty.create("persona.id_persona");
		jTableBinding.addColumnBinding(reservaBeanProperty_1).setColumnName("ID Persona").setEditable(false);
		//
		BeanProperty<Reserva, Date> reservaBeanProperty_4 = BeanProperty.create("fecha_registro");
		jTableBinding.addColumnBinding(reservaBeanProperty_4).setColumnName("Fecha Registro").setEditable(false);
		//
		BeanProperty<Reserva, Date> reservaBeanProperty_2 = BeanProperty.create("fecha_inicio");
		jTableBinding.addColumnBinding(reservaBeanProperty_2).setColumnName("Fecha Inicio").setEditable(false);
		//
		BeanProperty<Reserva, Date> reservaBeanProperty_3 = BeanProperty.create("fecha_fin");
		jTableBinding.addColumnBinding(reservaBeanProperty_3).setColumnName("Fecha Fin").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_5 = BeanProperty.create("estado");
		jTableBinding.addColumnBinding(reservaBeanProperty_5).setColumnName("Estado").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_7 = BeanProperty.create("persona.apellido");
		jTableBinding.addColumnBinding(reservaBeanProperty_7).setColumnName("Apellido").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_8 = BeanProperty.create("persona.nombre");
		jTableBinding.addColumnBinding(reservaBeanProperty_8).setColumnName("Nombre P.").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_9 = BeanProperty.create("persona.dni");
		jTableBinding.addColumnBinding(reservaBeanProperty_9).setColumnName("Dni").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_10 = BeanProperty.create("elemento.nombre");
		jTableBinding.addColumnBinding(reservaBeanProperty_10).setColumnName("Elemento").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_11 = BeanProperty.create("elemento.autor");
		jTableBinding.addColumnBinding(reservaBeanProperty_11).setColumnName("Autor").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_12 = BeanProperty.create("elemento.genero");
		jTableBinding.addColumnBinding(reservaBeanProperty_12).setColumnName("Genero").setEditable(false);
		//
		BeanProperty<Reserva, String> reservaBeanProperty_6 = BeanProperty.create("detalle");
		jTableBinding.addColumnBinding(reservaBeanProperty_6).setColumnName("Detalle").setEditable(false);
		//
		jTableBinding.bind();
	}
	
	public void btnSalirClick(){
		this.dispose();
		
		
	}
}
