package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.ActualizarRuta;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.AbstractTableModel;
import logica.clases.TipoConsulta;
import igu.interfaces.GuardarCancelarTipoConsulta;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import logica.LogicaTipoConsulta;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.ProblemaPersistencia;

public class PanelTipoConsultas extends JPanel implements GuardarCancelarTipoConsulta {

    private LogicaTipoConsulta logicaTipoConsulta;

    private ActualizarRuta actualizarRuta;
    private String ruta;
    private final static String estaRuta = "Tipo de Consultas";

    private ModeloTablaTipoConsultas modeloTabla;
    private JTable tablaTipoConsultas;
    private JToggleButton btnOrdenarAlfabeticamente;
    private JButton btnCrearConsulta, btnModificarConsulta, btnEliminarConsulta;

    private static final Color COLOR_FONDO_BOTONES = Color.LIGHT_GRAY;
    private static final Color COLOR_RESALTADO_BOTONES = Color.LIGHT_GRAY.brighter();

    private JPanel panelContenidoTipoConsultas;
    private CardLayout cardLayoutContenidoTipoConsultas;
    private PanelCrearEditarTipoConsulta panelCrearEditarTipoConsulta;

    public PanelTipoConsultas(LogicaTipoConsulta logicaTipoConsulta, ActualizarRuta actualizarRuta) {
        this.logicaTipoConsulta = logicaTipoConsulta;
        this.actualizarRuta = actualizarRuta;
        iniciarComponentes();
        iniciarEventos();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        JPanel panelInicioTipoConsultas = new JPanel(new BorderLayout());
        panelInicioTipoConsultas.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        //Tabla de todos los tipos de consultas
        modeloTabla = new ModeloTablaTipoConsultas();
        tablaTipoConsultas = FabricaElementos.crearTabla(modeloTabla);
        tablaTipoConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaTipoConsultas);

        actualizarContenidoTabla();

        panelInicioTipoConsultas.add(scrollTabla, BorderLayout.CENTER);

        //Panel del botón para ordenar alfabéticamente
        btnOrdenarAlfabeticamente = new JToggleButton("Ordenar alfabéticamente");
        btnOrdenarAlfabeticamente.setFont(new Font("Roboto SemiCondensed Medium", Font.PLAIN, 18));
        btnOrdenarAlfabeticamente.setBackground(new Color(255, 243, 188));
        btnOrdenarAlfabeticamente.setFocusPainted(false);

        JPanel panelBotonOrdenarAlfabeticamente = new JPanel();
        panelBotonOrdenarAlfabeticamente.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelBotonOrdenarAlfabeticamente.add(btnOrdenarAlfabeticamente);

        panelInicioTipoConsultas.add(panelBotonOrdenarAlfabeticamente, BorderLayout.NORTH);

        //Panel botones
        btnCrearConsulta = new JButton("Crear");
        btnModificarConsulta = new JButton("Modificar Datos");
        btnEliminarConsulta = new JButton("Eliminar");

        btnCrearConsulta.setEnabled(true);
        btnModificarConsulta.setEnabled(false);
        btnEliminarConsulta.setEnabled(false);

        JButton[] botones = {btnCrearConsulta, btnModificarConsulta, btnEliminarConsulta};
        JPanel panelBotonesTabla = FabricaElementos.crearPanelBotonesParaTabla(botones);

        panelInicioTipoConsultas.add(panelBotonesTabla, BorderLayout.EAST);

        //Configurar panel de contenido interno TipoConsultas
        cardLayoutContenidoTipoConsultas = new CardLayout();
        panelContenidoTipoConsultas = new JPanel(cardLayoutContenidoTipoConsultas);

        panelCrearEditarTipoConsulta = new PanelCrearEditarTipoConsulta(this);

        panelContenidoTipoConsultas.add(panelInicioTipoConsultas, "inicioTipoConsultas");
        panelContenidoTipoConsultas.add(panelCrearEditarTipoConsulta, "crearEditar");

        cardLayoutContenidoTipoConsultas.show(panelContenidoTipoConsultas, "inicioTipoConsultas");
        setRuta(estaRuta);

        add(panelContenidoTipoConsultas, BorderLayout.CENTER);
    }

    private void iniciarEventos() {

        btnCrearConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonCrearConsulta();
            }
        });

        agregarEfectoResaltado(btnCrearConsulta);

        btnModificarConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonModificarConsulta();
            }
        });

        agregarEfectoResaltado(btnModificarConsulta);

        btnEliminarConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonEliminarConsulta();
            }
        });

        agregarEfectoResaltado(btnEliminarConsulta);

        btnOrdenarAlfabeticamente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnOrdenarAlfabeticamente.isSelected()) {
                    botonOrdenarAlfabeticamente(true);
                } else {
                    botonOrdenarAlfabeticamente(false);
                }
            }
        });

        agregarEfectoResaltado(btnOrdenarAlfabeticamente);

        tablaTipoConsultas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarBotonesTabla();
                }
            }
        });
    }

    private void botonCrearConsulta() {
        panelCrearEditarTipoConsulta.modoCrear();
        cardLayoutContenidoTipoConsultas.show(panelContenidoTipoConsultas, "crearEditar");
        registrarRuta(estaRuta + " / " + btnCrearConsulta.getText());
    }

    private void botonModificarConsulta() {
        TipoConsulta tipoConsultaSeleccionada = modeloTabla.traerTipoConsulta(tablaTipoConsultas.getSelectedRow());
        panelCrearEditarTipoConsulta.modoEditar(tipoConsultaSeleccionada);
        cardLayoutContenidoTipoConsultas.show(panelContenidoTipoConsultas, "crearEditar");
        registrarRuta(estaRuta + " / " + btnModificarConsulta.getText());
    }

    private void botonEliminarConsulta() {
        try {
            logicaTipoConsulta.eliminar(Integer.valueOf(String.valueOf(modeloTabla.getValueAt(tablaTipoConsultas.getSelectedRow(), 0))));
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con base de datos.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (btnOrdenarAlfabeticamente.isSelected()) {
            botonOrdenarAlfabeticamente(true);
        } else {
            actualizarContenidoTabla();
        }

        JOptionPane.showMessageDialog(null, "Datos del Tipo de consulta eliminados exitosamente.", "Datos tipo de consulta eliminados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void botonOrdenarAlfabeticamente(boolean activado) {
        if (activado) {
            modeloTabla.actualizar(logicaTipoConsulta.ordenarAlfabeticamente(logicaTipoConsulta.traerTodos()));
        } else {
            modeloTabla.actualizar(logicaTipoConsulta.traerTodos());
        }
    }

    private void actualizarContenidoTabla() {
        modeloTabla.actualizar(logicaTipoConsulta.traerTodos());
    }

    private void actualizarBotonesTabla() {
        boolean registroSeleccionado = tablaTipoConsultas.getSelectedRow() != -1;

        btnCrearConsulta.setEnabled(!registroSeleccionado);
        btnModificarConsulta.setEnabled(registroSeleccionado);
        btnEliminarConsulta.setEnabled(registroSeleccionado);
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

    private void registrarRuta(String ruta) {
        actualizarRuta.actualizarRuta(ruta);
        setRuta(ruta);
    }

    @Override
    public void eventoGuardarTipoConsultaNueva(TipoConsulta tipoConsulta) {
        try {
            logicaTipoConsulta.crearNuevo(tipoConsulta);
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campos vacíos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (btnOrdenarAlfabeticamente.isSelected()) {
            botonOrdenarAlfabeticamente(true);
        } else {
            actualizarContenidoTabla();
        }

        JOptionPane.showMessageDialog(null, "Nuevo tipo de consulta registrada con éxito.", "Tipo de consulta registrada.", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoTipoConsultas.show(panelContenidoTipoConsultas, "inicioTipoConsultas");
        registrarRuta(estaRuta);
    }

    @Override
    public void eventoGuardarTipoConsultaEditada(TipoConsulta tipoConsulta) {
        try {
            logicaTipoConsulta.editarDatos(tipoConsulta);
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campos vacíos", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con base de datos.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (btnOrdenarAlfabeticamente.isSelected()) {
            botonOrdenarAlfabeticamente(true);
        } else {
            actualizarContenidoTabla();
        }

        JOptionPane.showMessageDialog(null, "Tipo de consulta editada con éxito.", "Tipo de consulta editada", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoTipoConsultas.show(panelContenidoTipoConsultas, "inicioTipoConsultas");
        registrarRuta(estaRuta);
    }

    @Override
    public void eventocancelar() {
        cardLayoutContenidoTipoConsultas.show(panelContenidoTipoConsultas, "inicioTipoConsultas");
        registrarRuta(estaRuta);
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}

class ModeloTablaTipoConsultas extends AbstractTableModel {

    private List<TipoConsulta> listaTipoConsultas = new ArrayList();
    private String[] nombreColumnas = {"ID", "Nombre de consulta", "Duración en Minutos", "Costo"};

    public void actualizar(List<TipoConsulta> listaTipoConsultas) {
        this.listaTipoConsultas = listaTipoConsultas;
        fireTableDataChanged();
    }

    public TipoConsulta traerTipoConsulta(int fila) {
        return listaTipoConsultas.get(fila);
    }

    @Override
    public int getRowCount() {
        return listaTipoConsultas.size();
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
        TipoConsulta tipoConsulta = listaTipoConsultas.get(rowIndex);

        switch (columnIndex) {

            case 0:
                return tipoConsulta.getIdTipoConsulta();

            case 1:
                return tipoConsulta.getNombreConsulta();

            case 2:
                return tipoConsulta.getDuracionMinutos();

            case 3:
                return tipoConsulta.getCosto();

            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
