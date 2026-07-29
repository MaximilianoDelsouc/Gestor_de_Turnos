package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.VolverVerHistorial;
import java.awt.BorderLayout;
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
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import logica.LogicaPaciente;
import logica.clases.Paciente;
import logica.clases.Turno;

public class PanelVerHistorial extends JPanel {

    private final LogicaPaciente logicaPaciente;

    private final VolverVerHistorial volver;

    private Paciente paciente;
    private ModeloTablaHistorialTurnos modeloTabla;
    private JTextField txtNombreApellido, txtDni, txtTelefono, txtCorreoElectronico;
    private JTextArea txtObservacion;
    private JRadioButton rbAtendido, rbCanceladoAusentado;
    private ButtonGroup grupoBotonesRadio;
    private JButton btnTodos;
    private JButton btnVolver;

    private static final Color COLOR_FONDO_BOTONES = Color.LIGHT_GRAY;
    private static final Color COLOR_RESALTADO_BOTONES = Color.LIGHT_GRAY.brighter();

    public PanelVerHistorial(LogicaPaciente logicaPaciente, VolverVerHistorial volver) {
        this.logicaPaciente = logicaPaciente;
        this.volver = volver;
        iniciarComponentes();
        iniciarEventosComponentes();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        //Panel información del paciente        
        JLabel lblEncabezadoInformacionPaciente = new JLabel("Información del Paciente");
        JLabel lblNombreApellido = new JLabel("Nombre y Apellido:");
        JLabel lblDni = new JLabel("DNI:");
        JLabel lblTelefono = new JLabel("Número de teléfono:");
        JLabel lblCorreoElectronico = new JLabel("Correo Electrónico:");
        JLabel lblObservacion = new JLabel("Observación:");

        Font fuenteEncabezados = new Font("Roboto SemiCondensed Medium", Font.BOLD, 24);
        lblEncabezadoInformacionPaciente.setFont(fuenteEncabezados);
        lblEncabezadoInformacionPaciente.setForeground(Color.BLACK);

        JLabel[] labelsCampos = {lblNombreApellido, lblDni, lblTelefono, lblCorreoElectronico, lblObservacion};
        Font fuenteLabelsTextFields = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 20);
        for (JLabel label : labelsCampos) {
            label.setFont(fuenteLabelsTextFields);
            label.setForeground(Color.GRAY);
        }

        txtNombreApellido = new JTextField();
        txtDni = new JTextField();
        txtTelefono = new JTextField();
        txtCorreoElectronico = new JTextField();

        JTextField[] textFields = {txtNombreApellido, txtDni, txtTelefono, txtCorreoElectronico};
        Color colorFondoTexts = new Color(255, 243, 188);
        for (JTextField textField : textFields) {
            textField.setColumns(30);
            textField.setFont(fuenteLabelsTextFields);
            textField.setBackground(colorFondoTexts);
            textField.setForeground(Color.BLACK);
            textField.setEditable(false);
        }

        txtObservacion = new JTextArea(5, 30);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);
        txtObservacion.setFont(fuenteLabelsTextFields);
        txtObservacion.setBackground(colorFondoTexts);
        txtObservacion.setForeground(Color.BLACK);
        txtObservacion.setEditable(false);
        JScrollPane scrollTxtObservacion = new JScrollPane(txtObservacion);

        JComponent[] componentesTexto = {txtNombreApellido, txtDni, txtTelefono, txtCorreoElectronico, scrollTxtObservacion};

        JPanel panelInformacionPaciente = FabricaElementos.crearPanelFormularioConEncabezado(labelsCampos, componentesTexto, lblEncabezadoInformacionPaciente);

        ////Panel Historial de turnos////
        JLabel lblEncabezadoHistorialTurnos = new JLabel("Historial de Turnos");
        lblEncabezadoHistorialTurnos.setFont(fuenteEncabezados);
        lblEncabezadoHistorialTurnos.setForeground(Color.BLACK);

        JPanel panelHistorialTurnos = FabricaElementos.crearPanelConEncabezado(lblEncabezadoHistorialTurnos);

        JPanel panelTablaFiltro = new JPanel();
        panelTablaFiltro.setLayout(new BorderLayout());

        // Panel de filtros
        JPanel panelFiltro = new JPanel();
        panelFiltro.setLayout(new GridBagLayout());

        JLabel lblVerPorEstado = new JLabel("Ver por Estado:");
        Font fuenteFiltros = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 18);
        lblVerPorEstado.setFont(fuenteFiltros);
        rbAtendido = new JRadioButton("Atendido");
        rbCanceladoAusentado = new JRadioButton("Cancelado o Ausentado");
        btnTodos = new JButton("Todos");

        rbAtendido.setBackground(Color.WHITE);
        rbCanceladoAusentado.setBackground(Color.WHITE);

        rbAtendido.setFont(fuenteFiltros);
        rbAtendido.setFocusPainted(false);
        rbCanceladoAusentado.setFont(fuenteFiltros);
        rbCanceladoAusentado.setFocusPainted(false);

        btnTodos.setFocusPainted(false);
        btnTodos.setFont(fuenteFiltros);
        btnTodos.setForeground(Color.BLACK);
        btnTodos.setBackground(COLOR_FONDO_BOTONES);

        grupoBotonesRadio = new ButtonGroup();
        grupoBotonesRadio.add(rbAtendido);
        grupoBotonesRadio.add(rbCanceladoAusentado);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 0, 10, 20);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        panelFiltro.add(lblVerPorEstado, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        panelFiltro.add(rbAtendido, gridBagConstraints);

        gridBagConstraints.gridx = 2;
        panelFiltro.add(rbCanceladoAusentado, gridBagConstraints);

        gridBagConstraints.gridx = 3;
        panelFiltro.add(btnTodos, gridBagConstraints);

        gridBagConstraints.gridx = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panelFiltro.add(Box.createHorizontalGlue(), gridBagConstraints);

        panelTablaFiltro.add(panelFiltro, BorderLayout.NORTH);

        panelFiltro.setBackground(Color.WHITE);

        //Tabla de turnos        
        modeloTabla = new ModeloTablaHistorialTurnos();
        JTable tablaHistorialTurnos = FabricaElementos.crearTabla(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaHistorialTurnos);
        panelTablaFiltro.add(scrollTabla, BorderLayout.CENTER);

        panelHistorialTurnos.add(panelTablaFiltro, BorderLayout.CENTER);

        //Agregar todo al panel del contenido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInformacionPaciente, panelHistorialTurnos);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setEnabled(false); //Desactiva ajuste por el usuario
        splitPane.setDividerSize(0);

        add(splitPane, BorderLayout.CENTER);

        //Boton de volver
        btnVolver = new JButton("Volver");

        Font fuenteBotones = new Font("Roboto SemiCondensed Medium", Font.BOLD, 18);
        btnVolver.setPreferredSize(new Dimension(150, 50));
        btnVolver.setFont(fuenteBotones);
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setBackground(COLOR_FONDO_BOTONES);
        btnVolver.setFocusPainted(false);
        btnVolver.setMargin(new Insets(8, 8, 8, 8));

        add(btnVolver, BorderLayout.SOUTH);
    }

    private void iniciarEventosComponentes() {

        rbAtendido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonFiltrarPorEstadoAtendido();
            }
        });

        rbCanceladoAusentado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonFiltrarPorEstadoCanceladoAusentado();
            }
        });

        btnTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traerTodoHistorial();
                grupoBotonesRadio.clearSelection();
            }
        });

        agregarEfectoResaltado(btnTodos);

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonVolver();
            }
        });

        agregarEfectoResaltado(btnVolver);
    }

    private void botonFiltrarPorEstadoAtendido() {
        modeloTabla.actualizar(logicaPaciente.traerHistorialTurnosAtendidosPaciente(paciente.getIdPaciente()));
    }

    private void botonFiltrarPorEstadoCanceladoAusentado() {
        modeloTabla.actualizar(logicaPaciente.traerHistorialTurnosCanceladosAusentadosPaciente(paciente.getIdPaciente()));
    }

    private void traerTodoHistorial() {
        modeloTabla.actualizar(logicaPaciente.traerHistorialCompletoPaciente(paciente.getIdPaciente()));
    }

    private void botonVolver() {
        volver.volver();
    }

    private void agregarEfectoResaltado(JButton boton) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_FONDO_BOTONES);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_RESALTADO_BOTONES);
            }
        });
    }

    public void cargarPaciente(Paciente paciente) {
        txtNombreApellido.setText(paciente.getNombre() + " " + paciente.getApellido());
        txtDni.setText(paciente.getDni());
        txtTelefono.setText(paciente.getTelefono());
        txtCorreoElectronico.setText(paciente.getCorreoElectronico());
        txtObservacion.setText(paciente.getObservacion());

        modeloTabla.actualizar(logicaPaciente.traerHistorialCompletoPaciente(paciente.getIdPaciente()));

        this.paciente = paciente;
    }
}

class ModeloTablaHistorialTurnos extends AbstractTableModel {

    private List<Turno> historialTurnos = new ArrayList();
    private String[] nombreColumnas = {"Tipo de Consulta", "Hora y Fecha", "Estado", "Reprogramado"};

    public void actualizar(List<Turno> historialTurnos) {
        this.historialTurnos = historialTurnos;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return historialTurnos.size();
    }

    @Override
    public int getColumnCount() {
        return nombreColumnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return nombreColumnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Turno turno = historialTurnos.get(rowIndex);

        return switch (columnIndex) {

            case 0 ->
                turno.getTipoConsulta().getNombreConsulta();

            case 1 ->
                turno.getFechaHoraInicial();

            case 2 ->
                turno.getEstado();

            case 3 ->
                turno.isReprogramado();

            default ->
                null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
