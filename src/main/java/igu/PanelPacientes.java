package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.ActualizarRuta;
import igu.interfaces.VolverVerHistorial;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import logica.LogicaPaciente;
import logica.clases.Paciente;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.ProblemaPersistencia;
import igu.interfaces.AccionesPaciente;

public class PanelPacientes extends JPanel implements AccionesPaciente, VolverVerHistorial {

    private final LogicaPaciente logicaPaciente;

    private final ActualizarRuta actualizarRuta;
    private String ruta;
    private static final String ESTA_RUTA = "Pacientes";

    private ModeloTablaPaciente modeloTabla;
    private JTable tablaPacientes;
    private JTextField txtBuscarPorNombre, txtBuscarPorApellido, txtBuscarPorDni;
    private JButton btnBuscar;
    private JButton btnAgregarPaciente, btnModificarDatos, btnEliminarPaciente, btnVerHistorial;

    private static final Color COLOR_FONDO_BOTONES = Color.LIGHT_GRAY;
    private static final Color COLOR_RESALTADO_BOTONES = Color.LIGHT_GRAY.brighter();

    private JPanel panelContenidoPacientes;
    private CardLayout cardLayoutContenidoPacientes;
    private PanelCrearEditarPaciente panelCrearEditarPaciente;
    private PanelVerHistorial panelVerHistorial;

    public PanelPacientes(LogicaPaciente logicaPaciente, ActualizarRuta actualizarRuta) {
        this.logicaPaciente = logicaPaciente;
        this.actualizarRuta = actualizarRuta;
        iniciarComponentes();
        iniciarEventosComponentes();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        JPanel panelInicioPacientes = new JPanel(new BorderLayout());
        panelInicioPacientes.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panelInicioPacientes.setBackground(Color.WHITE);

        //Tabla de pacientes
        modeloTabla = new ModeloTablaPaciente();
        tablaPacientes = FabricaElementos.crearTabla(modeloTabla);
        tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaPacientes);

        modeloTabla.actualizar(logicaPaciente.traerTodos());

        panelInicioPacientes.add(scrollTabla, BorderLayout.CENTER);

        //Panel de opciones de búsqueda        
        JLabel lblBuscarPorNombre = new JLabel("Buscar por Nombre:");
        JLabel lblBuscarPorApellido = new JLabel("Buscar por Apellido:");
        JLabel lblBuscarPorDni = new JLabel("Buscar por DNI:");

        JLabel[] labels = {lblBuscarPorNombre, lblBuscarPorApellido, lblBuscarPorDni};
        Font fuenteLabels = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 18);
        for (JLabel label : labels) {
            label.setFont(fuenteLabels);
            label.setForeground(Color.GRAY);
        }

        txtBuscarPorNombre = new JTextField();
        txtBuscarPorApellido = new JTextField();
        txtBuscarPorDni = new JTextField();

        JTextField[] textFields = {txtBuscarPorNombre, txtBuscarPorApellido, txtBuscarPorDni};
        Font fuenteTextFields = new Font("Roboto SemiCondensed Medium", Font.BOLD, 18);
        Color colorFondoTextFieldsYBotones = new Color(255, 243, 188);
        for (JTextField textField : textFields) {
            textField.setFont(fuenteTextFields);
            textField.setForeground(Color.BLACK);
            textField.setBackground(colorFondoTextFieldsYBotones);
            textField.setToolTipText("Deje el campo vacío si no quiere filtrar por este.");
            //textField.setColumns(42); //No hace falta porque el gridbaglayout configura el tamaño            
        }

        btnBuscar = new JButton();
        btnBuscar.setBackground(COLOR_FONDO_BOTONES);
        btnBuscar.setToolTipText("Buscar");

        JPanel panelOpcionesBusqueda = new JPanel();
        panelOpcionesBusqueda.setLayout(new GridBagLayout());
        panelOpcionesBusqueda.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        GridBagConstraints gridBagConstraintsPanelBusqueda = new GridBagConstraints();
        gridBagConstraintsPanelBusqueda.insets = new Insets(0, 10, 10, 10);

        gridBagConstraintsPanelBusqueda.gridx = 0;
        gridBagConstraintsPanelBusqueda.gridy = 0;
        panelOpcionesBusqueda.add(lblBuscarPorNombre, gridBagConstraintsPanelBusqueda);

        gridBagConstraintsPanelBusqueda.gridy = 1;
        panelOpcionesBusqueda.add(lblBuscarPorApellido, gridBagConstraintsPanelBusqueda);

        gridBagConstraintsPanelBusqueda.gridy = 2;
        panelOpcionesBusqueda.add(lblBuscarPorDni, gridBagConstraintsPanelBusqueda);

        gridBagConstraintsPanelBusqueda.weightx = 1;
        gridBagConstraintsPanelBusqueda.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraintsPanelBusqueda.gridx = 1;
        gridBagConstraintsPanelBusqueda.gridy = 0;
        panelOpcionesBusqueda.add(txtBuscarPorNombre, gridBagConstraintsPanelBusqueda);

        gridBagConstraintsPanelBusqueda.gridy = 1;
        panelOpcionesBusqueda.add(txtBuscarPorApellido, gridBagConstraintsPanelBusqueda);

        gridBagConstraintsPanelBusqueda.gridy = 2;
        panelOpcionesBusqueda.add(txtBuscarPorDni, gridBagConstraintsPanelBusqueda);

        gridBagConstraintsPanelBusqueda.weightx = 0;
        gridBagConstraintsPanelBusqueda.fill = GridBagConstraints.NONE;
        gridBagConstraintsPanelBusqueda.gridx = 2;
        gridBagConstraintsPanelBusqueda.gridy = 0;
        gridBagConstraintsPanelBusqueda.gridheight = 3;
        gridBagConstraintsPanelBusqueda.fill = GridBagConstraints.BOTH;
        btnBuscar.setPreferredSize(new Dimension(100, 1));
        panelOpcionesBusqueda.add(btnBuscar, gridBagConstraintsPanelBusqueda);

        gridBagConstraintsPanelBusqueda.gridx = 3;
        gridBagConstraintsPanelBusqueda.gridy = 0;
        panelOpcionesBusqueda.add(Box.createHorizontalStrut(145));

        panelOpcionesBusqueda.setBackground(Color.WHITE);

        panelInicioPacientes.add(panelOpcionesBusqueda, BorderLayout.NORTH);

        //Panel de botones  
        btnAgregarPaciente = new JButton("Agregar");
        btnVerHistorial = new JButton("Ver Historial");
        btnModificarDatos = new JButton("Modificar Datos");
        btnEliminarPaciente = new JButton("Eliminar Datos");

        btnAgregarPaciente.setEnabled(true);
        btnVerHistorial.setEnabled(false);
        btnModificarDatos.setEnabled(false);
        btnEliminarPaciente.setEnabled(false);

        JButton[] botones = {btnAgregarPaciente, btnVerHistorial, btnModificarDatos, btnEliminarPaciente};
        JPanel panelBotones = FabricaElementos.crearPanelBotonesParaTabla(botones);

        panelInicioPacientes.add(panelBotones, BorderLayout.EAST);

        //Configurar panel de contenido interno Paciente
        cardLayoutContenidoPacientes = new CardLayout();
        panelContenidoPacientes = new JPanel(cardLayoutContenidoPacientes);

        panelCrearEditarPaciente = new PanelCrearEditarPaciente(this); //Le paso esta instancia del panel, pero solo recibe la interfaz
        panelVerHistorial = new PanelVerHistorial(logicaPaciente, this);

        panelContenidoPacientes.add(panelInicioPacientes, "inicioPacientes");
        panelContenidoPacientes.add(panelCrearEditarPaciente, "crearEditar");
        panelContenidoPacientes.add(panelVerHistorial, "verHistorial");

        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "inicioPacientes");
        setRuta(ESTA_RUTA);

        add(panelContenidoPacientes, BorderLayout.CENTER);
    }

    private void iniciarEventosComponentes() {

        btnAgregarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonCrear();
            }
        });

        agregarEfectoResaltado(btnAgregarPaciente);

        btnModificarDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonEditar();
            }
        });

        agregarEfectoResaltado(btnModificarDatos);

        btnEliminarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonEliminar();
            }
        });

        agregarEfectoResaltado(btnEliminarPaciente);

        btnVerHistorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonVerHistorial();
            }
        });

        agregarEfectoResaltado(btnVerHistorial);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonBuscar();
            }
        });

        agregarEfectoResaltado(btnBuscar);

        tablaPacientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { //Al dar click en la tabla se tiran varios eventos, hace que solo pase uno
                    actualizarBotonesTabla();
                }
            }
        });

        panelContenidoPacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                deseleccionarTabla();
            }

        });
    }

    private void deseleccionarTabla() {
        tablaPacientes.clearSelection();
    }

    private void agregarEfectoResaltado(JButton boton) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(COLOR_FONDO_BOTONES);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(COLOR_RESALTADO_BOTONES);
                }
            }
        });
    }

    private void actualizarBotonesTabla() {

        boolean registroSeleccionado = tablaPacientes.getSelectedRow() != -1;

        btnAgregarPaciente.setEnabled(!registroSeleccionado);
        btnModificarDatos.setEnabled(registroSeleccionado);
        btnEliminarPaciente.setEnabled(registroSeleccionado);
        btnVerHistorial.setEnabled(registroSeleccionado);
    }

    private void botonCrear() {
        panelCrearEditarPaciente.modoCrear();
        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "crearEditar");
        registrarRuta(ESTA_RUTA + " / " + btnAgregarPaciente.getText());
    }

    private void botonEditar() {
        Paciente pacienteSeleccionado = modeloTabla.traerPaciente(tablaPacientes.getSelectedRow());
        panelCrearEditarPaciente.modoEditar(pacienteSeleccionado);
        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "crearEditar");
        registrarRuta(ESTA_RUTA + " / " + btnModificarDatos.getText());
    }

    private void botonEliminar() {
        Paciente paciente = logicaPaciente.traerPaciente(Integer.parseInt(String.valueOf(modeloTabla.getValueAt(tablaPacientes.getSelectedRow(), 0))));

        String mensaje = "¿Seguro que desea eliminar los datos del paciente con ID: " + paciente.getIdPaciente() + ", nombre completo: " + paciente.getNombre() + " " + paciente.getApellido() + ", DNI: " + paciente.getDni() + "?"
                + " Está acción será de forma permanente y no podrá deshacerse.";
        int opcionConfirmacion = JOptionPane.showConfirmDialog(null, mensaje, "Eliminar datos", JOptionPane.OK_CANCEL_OPTION);

        if (opcionConfirmacion == JOptionPane.OK_OPTION) {

            try {
                logicaPaciente.eliminar(paciente.getIdPaciente());
            } catch (ProblemaPersistencia e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            modeloTabla.actualizar(logicaPaciente.traerTodos());
            JOptionPane.showMessageDialog(null, "Datos del paciente eliminados exitosamente.", "Datos paciente eliminados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void botonVerHistorial() {
        Paciente pacienteSeleccionado = modeloTabla.traerPaciente(tablaPacientes.getSelectedRow());
        panelVerHistorial.cargarPaciente(pacienteSeleccionado);
        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "verHistorial");
        registrarRuta(ESTA_RUTA + " / " + btnVerHistorial.getText());
    }

    private void botonBuscar() {
        modeloTabla.actualizar(
                logicaPaciente.buscar(
                        txtBuscarPorNombre.getText().trim(),
                        txtBuscarPorApellido.getText().trim(),
                        txtBuscarPorDni.getText().trim()
                )
        );
    }

    @Override
    public void guardarNuevoPaciente(String nombre, String apellido, String dni, String telefono, String correoElectronico, String observacion) {
        try {
            logicaPaciente.crearNuevo(nombre, apellido, dni, telefono, correoElectronico, observacion);
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.actualizar(logicaPaciente.traerTodos());
        JOptionPane.showMessageDialog(null, "Nuevo paciente registrado con éxito.", "Paciente registrado", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "inicioPacientes");
        registrarRuta(ESTA_RUTA);
    }

    @Override
    public void guardarPacienteEditado(Paciente pacienteEditar, String nombre, String apellido, String dni, String telefono, String correoElectronico, String observacion) {
        try {
            logicaPaciente.editarDatos(pacienteEditar, nombre, apellido, dni, telefono, correoElectronico, observacion);
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con base de datos.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.actualizar(logicaPaciente.traerTodos());
        JOptionPane.showMessageDialog(null, "Paciente editado con éxito.", "Paciente editado", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "inicioPacientes");
        registrarRuta(ESTA_RUTA);
    }

    @Override
    public void eventoCancelar() {
        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "inicioPacientes");
        registrarRuta(ESTA_RUTA);
    }

    @Override
    public void volver() {
        cardLayoutContenidoPacientes.show(panelContenidoPacientes, "inicioPacientes");
        registrarRuta(ESTA_RUTA);
    }

    private void registrarRuta(String ruta) {
        actualizarRuta.actualizar(ruta);
        setRuta(ruta);
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}

class ModeloTablaPaciente extends AbstractTableModel {

    private List<Paciente> listaPacientes = new ArrayList();
    private final String[] nombresColumnas = {"ID", "Nombre", "Apellido", "DNI", "Correo Electrónico", "Teléfono", "Observación"};

    public void actualizar(List<Paciente> listaPacientes) {
        this.listaPacientes = listaPacientes;
        fireTableDataChanged();
    }

    public Paciente traerPaciente(int fila) {
        return listaPacientes.get(fila);
    }

    @Override
    public int getRowCount() {
        return listaPacientes.size();
    }

    @Override
    public int getColumnCount() {
        return nombresColumnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return nombresColumnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Paciente paciente = listaPacientes.get(rowIndex);

        return switch (columnIndex) {

            case 0 ->
                paciente.getIdPaciente();

            case 1 ->
                paciente.getNombre();

            case 2 ->
                paciente.getApellido();

            case 3 ->
                paciente.getDni();

            case 4 ->
                paciente.getCorreoElectronico();

            case 5 ->
                paciente.getTelefono();

            case 6 ->
                paciente.getObservacion();

            default ->
                null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
