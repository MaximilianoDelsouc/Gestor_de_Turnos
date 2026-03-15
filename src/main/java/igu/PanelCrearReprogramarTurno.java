package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.BuscarPacienteTurno;
import igu.interfaces.GuardarCancelarTurno;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import logica.clases.HorarioDisponible;
import logica.clases.Paciente;
import logica.clases.TipoConsulta;
import logica.clases.Turno;
import igu.interfaces.TraerHorariosDisponibles;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class PanelCrearReprogramarTurno extends JPanel {

    private TraerHorariosDisponibles traerHorariosDisponibles;
    private GuardarCancelarTurno guardarCancelar;
    private BuscarPacienteTurno buscarPaciente;

    private JTextField txtPaciente;
    private JButton btnBuscarPaciente;
    private JComboBox cmbTipoDeConsulta, cmbHorario;
    private JSpinner spnFecha;
    private JButton btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar;

    private static final Color COLOR_FONDO_BOTONES = new Color(255, 243, 188);
    private static final Color COLOR_RESALTADO_BOTONES = COLOR_FONDO_BOTONES.brighter();

    private Paciente pacienteTurno;

    private Turno turnoReprogramar;

    public PanelCrearReprogramarTurno(GuardarCancelarTurno guardarCancelar, BuscarPacienteTurno buscarPaciente, TraerHorariosDisponibles traerhHorariosDisponibles) {
        this.traerHorariosDisponibles = traerhHorariosDisponibles;
        this.guardarCancelar = guardarCancelar;
        this.buscarPaciente = buscarPaciente;
        iniciarComponentes();
        iniciarEventosComponentes();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        ////Panel datos del turno////
        JLabel lblPaciente = new JLabel("Paciente:");
        JLabel lblTipoDeConsulta = new JLabel("Tipo de consulta:");
        JLabel lblFecha = new JLabel("Fecha:");
        JLabel lblHorario = new JLabel("Horario:");

        JLabel[] labels = {lblPaciente, lblTipoDeConsulta, lblFecha, lblHorario};
        Font fuenteLabelsTextFields = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 18);
        for (JLabel label : labels) {
            label.setFont(fuenteLabelsTextFields);
            label.setForeground(Color.BLACK);
        }

        txtPaciente = new JTextField();
        btnBuscarPaciente = new JButton("Buscar Paciente");
        cmbTipoDeConsulta = new JComboBox();
        spnFecha = new JSpinner(new SpinnerDateModel(fechaHoyNormalizada(), fechaHoyNormalizada(), null, Calendar.DAY_OF_MONTH));
        cmbHorario = new JComboBox();

        Color colorFondoTextFields = new Color(255, 243, 188);

        txtPaciente.setColumns(20);
        txtPaciente.setFont(fuenteLabelsTextFields);
        txtPaciente.setBackground(colorFondoTextFields);
        txtPaciente.setForeground(Color.BLACK);
        txtPaciente.setEditable(false);

        btnBuscarPaciente.setFont(new Font("Roboto SemiCondensed Medium", Font.BOLD, 18));
        btnBuscarPaciente.setBackground(colorFondoTextFields);
        btnBuscarPaciente.setForeground(Color.DARK_GRAY);
        btnBuscarPaciente.setFocusPainted(false);

        cmbTipoDeConsulta.setPreferredSize(new Dimension(480, 30));
        cmbTipoDeConsulta.setFont(fuenteLabelsTextFields);
        cmbTipoDeConsulta.setBackground(colorFondoTextFields);
        cmbTipoDeConsulta.setForeground(Color.BLACK);
        cmbTipoDeConsulta.setEditable(false);

        JSpinner.DateEditor editorFecha = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy");
        JFormattedTextField texto = editorFecha.getTextField();
        texto.setColumns(10);
        texto.setBackground(new Color(255, 243, 188));
        texto.setEditable(false); // Bloquear edición manual        
        spnFecha.setEditor(editorFecha);
        spnFecha.setFont(fuenteLabelsTextFields);

        cmbHorario.setPreferredSize(new Dimension(480, 30));
        cmbHorario.setFont(fuenteLabelsTextFields);
        cmbHorario.setBackground(colorFondoTextFields);
        cmbHorario.setForeground(Color.BLACK);
        cmbHorario.setEditable(false);

        //GridBagConstrains de los paneles con los campos
        GridBagConstraints gridBagConstrains = new GridBagConstraints();
        gridBagConstrains.insets = new Insets(0, 20, 0, 20);
        gridBagConstrains.anchor = GridBagConstraints.WEST;
        gridBagConstrains.weighty = 1;
        gridBagConstrains.gridx = 0; //Se mantiene siempre igual

        //GridBagConstrains de los componentes dentro de los paneles de los campos
        GridBagConstraints gridBagConstrainsDentroPaneles = new GridBagConstraints();
        gridBagConstrainsDentroPaneles.insets = new Insets(0, 0, 20, 10);
        gridBagConstrainsDentroPaneles.anchor = GridBagConstraints.WEST;

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);

        //Panel primer campo
        JPanel panelCampoPaciente = new JPanel(new GridBagLayout());

        gridBagConstrainsDentroPaneles.gridx = 0;
        gridBagConstrainsDentroPaneles.gridy = 0;
        panelCampoPaciente.add(lblPaciente, gridBagConstrainsDentroPaneles);

        gridBagConstrainsDentroPaneles.gridy = 1;
        panelCampoPaciente.add(txtPaciente, gridBagConstrainsDentroPaneles);

        gridBagConstrainsDentroPaneles.gridx = 1;
        panelCampoPaciente.add(btnBuscarPaciente, gridBagConstrainsDentroPaneles);

        gridBagConstrains.gridy = 0;
        panelFormulario.add(panelCampoPaciente, gridBagConstrains);

        //Panel segundo campo
        JPanel panelCampoTipoConsulta = new JPanel(new GridBagLayout());

        gridBagConstrainsDentroPaneles.gridx = 0;
        gridBagConstrainsDentroPaneles.gridy = 0;
        panelCampoTipoConsulta.add(lblTipoDeConsulta, gridBagConstrainsDentroPaneles);

        gridBagConstrainsDentroPaneles.gridy = 1;
        panelCampoTipoConsulta.add(cmbTipoDeConsulta, gridBagConstrainsDentroPaneles);

        gridBagConstrains.gridy = 1;
        panelFormulario.add(panelCampoTipoConsulta, gridBagConstrains);

        //Panel tercer campo
        JPanel panelCampoFecha = new JPanel(new GridBagLayout());

        gridBagConstrainsDentroPaneles.gridx = 0;
        gridBagConstrainsDentroPaneles.gridy = 0;
        panelCampoFecha.add(lblFecha, gridBagConstrainsDentroPaneles);

        gridBagConstrainsDentroPaneles.gridy = 1;
        panelCampoFecha.add(spnFecha, gridBagConstrainsDentroPaneles);

        gridBagConstrains.gridy = 2;
        panelFormulario.add(panelCampoFecha, gridBagConstrains);

        //Panel cuarto campo
        JPanel panelCampoHorario = new JPanel(new GridBagLayout());

        gridBagConstrainsDentroPaneles.gridx = 0;
        gridBagConstrainsDentroPaneles.gridy = 0;
        panelCampoHorario.add(lblHorario, gridBagConstrainsDentroPaneles);

        gridBagConstrainsDentroPaneles.gridy = 1;
        panelCampoHorario.add(cmbHorario, gridBagConstrainsDentroPaneles);

        gridBagConstrains.gridy = 3;
        panelFormulario.add(panelCampoHorario, gridBagConstrains);

        ////Panel de los botones////
        btnGuardar = new JButton("Guardar");
        btnLimpiarTodo = new JButton("Limpiar todo");
        btnRestablecerTodo = new JButton("Restablecer todo");
        btnCancelar = new JButton("Cancelar");
        JButton[] botones = {btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar};

        JPanel panelBotones = FabricaElementos.crearPanelBotonesParaCrearEditar(botones);

        ////Agregar todo al panel del contenido////
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelFormulario, panelBotones);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setEnabled(false); //Desactiva ajuste por el usuario
        splitPane.setDividerSize(0);

        add(splitPane, BorderLayout.CENTER);
    }

    private void iniciarEventosComponentes() {

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonGuardar();
            }
        });

        agregarEfectoResaltado(btnGuardar);

        btnLimpiarTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonLimpiarTodo();
            }
        });

        agregarEfectoResaltado(btnLimpiarTodo);

        btnRestablecerTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonRestablecerTodo();
            }
        });

        agregarEfectoResaltado(btnRestablecerTodo);

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonCancelar();
            }
        });

        agregarEfectoResaltado(btnCancelar);

        btnBuscarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonBuscarPaciente();
            }
        });

        agregarEfectoResaltado(btnBuscarPaciente);

        cmbTipoDeConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarHorarios();
            }
        });

        spnFecha.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cargarHorarios();
            }
        });
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

    private void botonGuardar() {

        if (pacienteTurno == null) {
            JOptionPane.showMessageDialog(null, "Debe asignar un paciente para el turno. Haga presione el botón 'Buscar' al lado del campo Paciente para seleccionar uno.",
                    "No hay paciente asignado.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cmbTipoDeConsulta.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Debe asignar un tipo de consulta para el turno.",
                    "No hay tipo de consulta asignado.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cmbHorario.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Debe asginar un horario para el turno.",
                    "No hay horario asignado para el turno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HorarioDisponible horarioDisponibleNuevoTurno = (HorarioDisponible) cmbHorario.getSelectedItem();
        if (horarioDisponibleNuevoTurno.getHorarioInicial().before(new Date())) {
            JOptionPane.showMessageDialog(null, "Compruebe que el horario asigando no haya pasado aún.",
                    "Horario inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoConsulta tipoConsultaNuevoTurno = (TipoConsulta) cmbTipoDeConsulta.getSelectedItem();
        Turno nuevoTurno = new Turno(horarioDisponibleNuevoTurno.getHorarioInicial(), horarioDisponibleNuevoTurno.getHorarioFinal(), pacienteTurno, tipoConsultaNuevoTurno);

        if (turnoReprogramar == null) {
            guardarCancelar.eventoGuardarTurnoNuevo(nuevoTurno);
        } else {
            guardarCancelar.eventoReprogramarTurno(turnoReprogramar, nuevoTurno);
        }
    }

    private void botonLimpiarTodo() {
        pacienteTurno = null;
        txtPaciente.setText("");

        cmbTipoDeConsulta.setSelectedIndex(0);

        spnFecha.setValue(fechaHoyNormalizada());

        cmbHorario.setSelectedIndex(0);
    }

    private void botonRestablecerTodo() {
        cargarCampos(turnoReprogramar);
    }

    private void botonCancelar() {
        guardarCancelar.eventoCancelar();
        botonLimpiarTodo();
    }

    private void botonBuscarPaciente() {
        buscarPaciente.eventoBuscarPaciente(); //Carga el paciente mediante el setter

        if (pacienteTurno == null) {
            return;
        }

        txtPaciente.setText(pacienteTurno.getNombre() + " " + pacienteTurno.getApellido() + " - Nº" + pacienteTurno.getIdPaciente());
    }

    private void cargarTiposConsulta(List<TipoConsulta> listaTipoConsulta) {
        cmbTipoDeConsulta.removeAllItems();
        cmbTipoDeConsulta.addItem(null); //Opción vacía

        for (TipoConsulta tipoConsulta : listaTipoConsulta) {
            cmbTipoDeConsulta.addItem(tipoConsulta);
        }
    }

    private void cargarHorarios() {
        if (cmbTipoDeConsulta.getSelectedItem() == null) {
            cmbHorario.removeAllItems();
            cmbHorario.addItem(null);
            return;
        }

        TipoConsulta tipoConsultaSeleccionada = (TipoConsulta) cmbTipoDeConsulta.getSelectedItem();
        Date fechaSeleccionada = (Date) spnFecha.getValue();

        Map<Date, Date> horariosDisponibles = traerHorariosDisponibles.eventoTraerHorariosDisponibles(fechaSeleccionada, tipoConsultaSeleccionada, turnoReprogramar);

        cmbHorario.removeAllItems();
        cmbHorario.addItem(null);

        for (Map.Entry<Date, Date> horario : horariosDisponibles.entrySet()) {
            cmbHorario.addItem(new HorarioDisponible(horario.getKey(), horario.getValue()));
        }
    }

    private void cargarCampos(Turno turno) {
        pacienteTurno = turno.getPaciente();
        txtPaciente.setText(pacienteTurno.getNombre() + " " + pacienteTurno.getApellido());

        cmbTipoDeConsulta.setSelectedItem(turno.getTipoConsulta());

        Calendar calendarioTurno = Calendar.getInstance(); //Necesario normalizar porque solo se está manejando fechas, no horas, minutos, etc.
        calendarioTurno.setTime(turno.getHoraInicio());
        calendarioTurno.set(Calendar.HOUR_OF_DAY, 0);
        calendarioTurno.set(Calendar.MINUTE, 0);
        calendarioTurno.set(Calendar.SECOND, 0);
        calendarioTurno.set(Calendar.MILLISECOND, 0);
        Date fechaTurno = calendarioTurno.getTime();
        spnFecha.setValue(fechaTurno);

        HorarioDisponible horarioTurno = new HorarioDisponible(turno.getHoraInicio(), turno.getHoraFinal());
        cmbHorario.setSelectedItem(horarioTurno);
    }

    private Date fechaHoyNormalizada() {  //Necesario para que JSpinner funcione correctamente
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        return calendario.getTime();
    }

    public void modoCrear(List<TipoConsulta> listaTipoConsulta) {
        turnoReprogramar = null;

        cargarTiposConsulta(listaTipoConsulta);

        botonLimpiarTodo();

        btnLimpiarTodo.setEnabled(true);
        btnRestablecerTodo.setEnabled(false);
        btnBuscarPaciente.setEnabled(true);
    }

    public void modoReprogramar(List<TipoConsulta> listaTipoConsulta, Turno turno) {
        turnoReprogramar = turno;

        cargarTiposConsulta(listaTipoConsulta);

        cargarCampos(turno);

        btnLimpiarTodo.setEnabled(false);
        btnRestablecerTodo.setEnabled(true);
        btnBuscarPaciente.setEnabled(false);
    }

    public void setPacienteTurno(Paciente pacienteTurno) {
        this.pacienteTurno = pacienteTurno;
    }
}
