package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.ActualizarTurnosHoy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import logica.LogicaTurno;
import logica.clases.Paciente;
import logica.clases.Turno;

public class PanelInicio extends JPanel implements ActualizarTurnosHoy {
    
    private LogicaTurno logicaTurno;    
    
    private String ruta;
    
    private JTextField txtNombreApellido, txtTipoConsulta, txtHoraInicioTurno;
    private JTextArea txtObservacion;
    
    private ModeloTablaTurnosHoy modeloTabla;
    
    public PanelInicio(LogicaTurno logicaTurno) {
        this.logicaTurno = logicaTurno;
        iniciarComponentes();
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

        //Panel Turnos Hoy       
        JLabel lblEncabezadoTurnosHoy = new JLabel("Turnos de Hoy");
        lblEncabezadoTurnosHoy.setFont(fuenteEncabezados);
        lblEncabezadoTurnosHoy.setForeground(Color.BLACK);
        
        modeloTabla = new ModeloTablaTurnosHoy();
        JTable tablaTurnosHoy = FabricaElementos.crearTabla(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaTurnosHoy);
        
        actualizarTurnosHoy();
        
        JPanel panelTurnosHoy = FabricaElementos.crearPanelConEncabezado(lblEncabezadoTurnosHoy); //Devuelve el panel con BorderLayout completo pero solo con encabezado, el resto vacío.

        panelTurnosHoy.add(scrollTabla, BorderLayout.CENTER);

        //Agregar todo al panel del contenido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelProximoPaciente, panelTurnosHoy);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setEnabled(false); //Desactiva ajuste por el usuario
        splitPane.setDividerSize(0);
        
        add(splitPane, BorderLayout.CENTER);
        
        setRuta("Inicio");
    }
    
    private void actualizarProximoPaciente(List<Turno> turnosHoy) {
        Paciente proximoPaciente = logicaTurno.traerProximoPaciente(turnosHoy);
        
        if (proximoPaciente == null) {
            txtNombreApellido.setText("----");
            txtTipoConsulta.setText("----");
            txtHoraInicioTurno.setText("----");
            txtObservacion.setText("----");
            return;
        }
        
        txtNombreApellido.setText(proximoPaciente.getNombre() + " " + proximoPaciente.getApellido());
        txtTipoConsulta.setText(turnosHoy.get(0).getTipoConsulta().getNombreConsulta());
        SimpleDateFormat formatoHoraInicio = new SimpleDateFormat("HH:mm");
        String horaFormateada = formatoHoraInicio.format(turnosHoy.get(0).getHoraInicio());
        txtHoraInicioTurno.setText(horaFormateada + "hs");
        txtObservacion.setText(proximoPaciente.getObservacion());
    }
    
    @Override
    public void actualizarTurnosHoy() {
        List<Turno> turnosHoy = logicaTurno.traerTurnosHoy();
        modeloTabla.actualizar(turnosHoy);
        actualizarProximoPaciente(turnosHoy);
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
    private final String[] nombreColumnas = {"Hora Inicial", "Hora Final", "Tipo de Consulta", "Paciente", "Estado"};
    
    public void actualizar(List<Turno> turnosHoy) {
        this.turnosHoy = turnosHoy;
        fireTableDataChanged();
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
        
        switch (columnIndex) {
            
            case 0:
                return turno.getHoraInicio();
            
            case 1:
                return turno.getHoraFinal();
            
            case 2:
                return turno.getTipoConsulta().getNombreConsulta();
            
            case 3:
                return turno.getPaciente().getNombre() + " " + turno.getPaciente().getApellido();
            
            case 4:
                return turno.getEstado();
            
            default:
                return null;
        }
    }

    //Hacer que la tabla no sea editable
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
