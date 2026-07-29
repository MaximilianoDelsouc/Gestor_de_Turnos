package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.ActualizarTurnosHoy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import logica.LogicaTurno;
import logica.clases.Paciente;
import logica.clases.Turno;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.ProblemaPersistencia;

public class PanelInicio extends JPanel implements ActualizarTurnosHoy {

    private final LogicaTurno logicaTurno;

    private String ruta;

    private JTextField txtNombreApellido, txtTipoConsulta, txtHoraInicioTurno;
    private JTextArea txtObservacion;

    private JButton btnAtenderTurno;
    private ModeloTablaTurnosHoy modeloTabla;
    private JTable tablaTurnosHoy;

    private static final Color COLOR_FONDO_BOTONES = Color.LIGHT_GRAY;
    private static final Color COLOR_RESALTADO_BOTONES = Color.LIGHT_GRAY.brighter();

    public PanelInicio(LogicaTurno logicaTurno) {
        this.logicaTurno = logicaTurno;
        iniciarComponentes();
        iniciarEventosComponentes();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout());

        //Panel Próximo Paciente
        JLabel lblEncabezadoProximoPaciente = new JLabel("Próximo Paciente");
        JLabel lblNombreApellido = new JLabel("Nombre y Apellido:");
        JLabel lblTipoConsulta = new JLabel("Tipo de Consulta:");
        JLabel lblHoraInicioTurno = new JLabel("Hora de inicio del Turno:");
        JLabel lblObservacion = new JLabel("Observación:");

        Font fuenteEncabezados = new Font("Roboto SemiCondensed Medium", Font.BOLD, 24);
        lblEncabezadoProximoPaciente.setFont(fuenteEncabezados);
        lblEncabezadoProximoPaciente.setForeground(Color.BLACK);

        JLabel[] labelsCampos = {lblNombreApellido, lblTipoConsulta, lblHoraInicioTurno, lblObservacion};
        Font fuenteLabelsTextFields = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 20);
        for (JLabel label : labelsCampos) {
            label.setFont(fuenteLabelsTextFields);
            label.setForeground(Color.GRAY);
        }

        txtNombreApellido = new JTextField();
        txtTipoConsulta = new JTextField();
        txtHoraInicioTurno = new JTextField();

        JTextField[] textFields = {txtNombreApellido, txtTipoConsulta, txtHoraInicioTurno};
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

        JComponent[] componentesTexto = {txtNombreApellido, txtTipoConsulta, txtHoraInicioTurno, scrollTxtObservacion};

        JPanel panelProximoPaciente = FabricaElementos.crearPanelFormularioConEncabezado(labelsCampos, componentesTexto, lblEncabezadoProximoPaciente);

        ////Panel turnos de hoy////
        JLabel lblEncabezadoTurnosHoy = new JLabel("Turnos pendientes de Hoy");
        lblEncabezadoTurnosHoy.setFont(fuenteEncabezados);
        lblEncabezadoTurnosHoy.setForeground(Color.BLACK);

        JPanel panelTurnosHoy = FabricaElementos.crearPanelConEncabezado(lblEncabezadoTurnosHoy); //Devuelve el panel con BorderLayout completo pero solo con encabezado, el resto vacío.        

        JPanel panelBotonTabla = new JPanel(new BorderLayout());

        //Panel boton atender turno                
        btnAtenderTurno = new JButton("Atender turno");
        btnAtenderTurno.setFont(new Font("Roboto SemiCondensed Medium", Font.PLAIN, 18));
        btnAtenderTurno.setBackground(COLOR_FONDO_BOTONES);

        btnAtenderTurno.setEnabled(false);

        JPanel panelBotonAtender = new JPanel();
        panelBotonAtender.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelBotonAtender.add(btnAtenderTurno);

        panelBotonAtender.setBackground(Color.WHITE);

        panelBotonTabla.add(panelBotonAtender, BorderLayout.NORTH);

        //Tabla
        modeloTabla = new ModeloTablaTurnosHoy();
        tablaTurnosHoy = FabricaElementos.crearTabla(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaTurnosHoy);

        actualizar();

        panelBotonTabla.add(scrollTabla, BorderLayout.CENTER);

        panelTurnosHoy.add(panelBotonTabla, BorderLayout.CENTER);

        //Agregar todo al panel del contenido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelProximoPaciente, panelTurnosHoy);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setEnabled(false); //Desactiva ajuste por el usuario
        splitPane.setDividerSize(0);

        add(splitPane, BorderLayout.CENTER);

        setRuta("Inicio");
    }

    private void iniciarEventosComponentes() {
        btnAtenderTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonAtenderTurno();
            }
        });

        agregarEfectoResaltado(btnAtenderTurno);

        tablaTurnosHoy.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarBotonTabla();
                }
            }
        });
    }

    private void agregarEfectoResaltado(AbstractButton boton) {
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

    private void actualizarBotonTabla() {
        if (tablaTurnosHoy.getSelectedRow() == -1) {
            btnAtenderTurno.setEnabled(false);
        } else {
            btnAtenderTurno.setEnabled(true);
        }
    }

    private void botonAtenderTurno() {
        try {
            logicaTurno.atenderTurno(modeloTabla.traerTurno(tablaTurnosHoy.getSelectedRow()));
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Cambio de estado inválido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Estado cambiado a Atendido exitosamente.", "Turno Atendido", JOptionPane.INFORMATION_MESSAGE);

        actualizar();
    }

    @Override
    public void actualizar() {
        List<Turno> turnosHoy = logicaTurno.traerTurnosHoyPendientes();
        modeloTabla.actualizar(turnosHoy);
        actualizarProximoPaciente(turnosHoy);
    }

    private void actualizarProximoPaciente(List<Turno> turnosHoy) {
        logicaTurno.traerProximoPaciente(turnosHoy).ifPresentOrElse(
                paciente -> {
                    txtNombreApellido.setText(paciente.getNombre() + " " + paciente.getApellido());
                    txtTipoConsulta.setText(turnosHoy.get(0).getTipoConsulta().getNombreConsulta());
                    SimpleDateFormat formatoHoraInicio = new SimpleDateFormat("HH:mm");
                    String horaFormateada = formatoHoraInicio.format(turnosHoy.get(0).getFechaHoraInicial());
                    txtHoraInicioTurno.setText(horaFormateada + "hs");
                    txtObservacion.setText(paciente.getObservacion());
                },
                () -> {
                    txtNombreApellido.setText("");
                    txtTipoConsulta.setText("");
                    txtHoraInicioTurno.setText("");
                    txtObservacion.setText("");
                }
        );
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}

class ModeloTablaTurnosHoy extends AbstractTableModel {

    private List<Turno> turnosHoy = new ArrayList();
    private final String[] nombreColumnas = {"ID del turno", "Hora Inicial", "Hora Final", "Tipo de Consulta", "Paciente"};
    private static final SimpleDateFormat FORMATO_HORAS = new SimpleDateFormat("HH:mm");

    public void actualizar(List<Turno> turnosHoy) {
        this.turnosHoy = turnosHoy;
        fireTableDataChanged();
    }

    public Turno traerTurno(int fila) {
        return turnosHoy.get(fila);
    }

    @Override
    public int getRowCount() {
        return turnosHoy.size();
    }

    @Override
    public int getColumnCount() {
        return nombreColumnas.length;
    }

    //Ponerle el nombre a las columnas
    @Override
    public String getColumnName(int column) {
        return nombreColumnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Turno turno = turnosHoy.get(rowIndex);
        Paciente paciente = turno.getPaciente();

        switch (columnIndex) {

            case 0 -> {
                return turno.getIdTurno();
            }

            case 1 -> {
                return FORMATO_HORAS.format(turno.getFechaHoraInicial()) + " hs";
            }

            case 2 -> {
                return FORMATO_HORAS.format(turno.getFechaHoraFinal()) + " hs";
            }

            case 3 -> {
                return turno.getTipoConsulta().getNombreConsulta();
            }

            case 4 -> {
                return (paciente == null) ? "[ELIMINADO]" : paciente.getNombre() + " " + paciente.getApellido();
            }

            default -> {
                return null;
            }
        }
    }

    //Hacer que la tabla no sea editable
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
