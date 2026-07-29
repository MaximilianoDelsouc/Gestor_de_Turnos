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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import logica.LogicaTipoConsulta;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.ProblemaPersistencia;
import igu.interfaces.AccionesTipoConsulta;
import javax.swing.Box;
import logica.exceptions.TipoConsultaDeshabilitado;

public class PanelTipoConsultas extends JPanel implements AccionesTipoConsulta {

    private final LogicaTipoConsulta logicaTipoConsulta;

    private final ActualizarRuta actualizarRuta;
    private String ruta;
    private static final String ESTA_RUTA = "Tipos de Consultas";

    private ModeloTablaTipoConsultas modeloTabla;
    private JTable tablaTipoConsultas;
    private JToggleButton btnOrdenarAlfabeticamente;
    private JToggleButton btnVerConsultasDeshabilitadas;
    private JButton btnCrearConsulta, btnModificarConsulta, btnEliminarDeshabilitarConsulta, btnHabilitarConsulta;

    private static final Color COLOR_FONDO_BOTONES = Color.LIGHT_GRAY;
    private static final Color COLOR_RESALTADO_BOTONES = Color.LIGHT_GRAY.brighter();

    private JPanel panelContenidoTiposConsulta;
    private CardLayout cardLayoutContenidoTipoConsultas;
    private PanelCrearEditarTipoConsulta panelCrearEditarTipoConsulta;

    public PanelTipoConsultas(LogicaTipoConsulta logicaTipoConsulta, ActualizarRuta actualizarRuta) {
        this.logicaTipoConsulta = logicaTipoConsulta;
        this.actualizarRuta = actualizarRuta;
        iniciarComponentes();
        iniciarEventosComponentes();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        JPanel panelInicioTipoConsultas = new JPanel(new BorderLayout());
        panelInicioTipoConsultas.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panelInicioTipoConsultas.setBackground(Color.WHITE);

        //Tabla de tipos de consulta
        modeloTabla = new ModeloTablaTipoConsultas();
        tablaTipoConsultas = FabricaElementos.crearTabla(modeloTabla);
        tablaTipoConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaTipoConsultas);

        actualizarContenidoTabla();

        panelInicioTipoConsultas.add(scrollTabla, BorderLayout.CENTER);

        //Panel de botones de filtro                       
        btnOrdenarAlfabeticamente = new JToggleButton("Ordenar alfabéticamente");
        btnOrdenarAlfabeticamente.setFont(new Font("Roboto SemiCondensed Medium", Font.BOLD, 18));
        btnOrdenarAlfabeticamente.setBackground(COLOR_FONDO_BOTONES);
        btnOrdenarAlfabeticamente.setFocusPainted(false);

        btnVerConsultasDeshabilitadas = new JToggleButton("Ver deshabilitados");
        btnVerConsultasDeshabilitadas.setFont(new Font("Roboto SemiCondensed Medium", Font.PLAIN, 18));
        btnVerConsultasDeshabilitadas.setBackground(COLOR_FONDO_BOTONES);
        btnVerConsultasDeshabilitadas.setFocusPainted(false);

        JPanel panelBotonesFiltros = new JPanel();
        panelBotonesFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelBotonesFiltros.add(btnOrdenarAlfabeticamente);
        panelBotonesFiltros.add(Box.createHorizontalStrut(30));
        panelBotonesFiltros.add(btnVerConsultasDeshabilitadas);

        panelBotonesFiltros.setBackground(Color.WHITE);

        panelInicioTipoConsultas.add(panelBotonesFiltros, BorderLayout.NORTH);

        //Panel botones
        btnCrearConsulta = new JButton("Crear");
        btnModificarConsulta = new JButton("Modificar Datos");
        btnEliminarDeshabilitarConsulta = new JButton("Eliminar / Deshabilitar");
        btnHabilitarConsulta = new JButton("Habilitar");

        btnCrearConsulta.setEnabled(true);
        btnModificarConsulta.setEnabled(false);
        btnEliminarDeshabilitarConsulta.setEnabled(false);
        btnHabilitarConsulta.setEnabled(false);

        JButton[] botones = {btnCrearConsulta, btnModificarConsulta, btnEliminarDeshabilitarConsulta, btnHabilitarConsulta};
        JPanel panelBotonesTabla = FabricaElementos.crearPanelBotonesParaTabla(botones);

        panelInicioTipoConsultas.add(panelBotonesTabla, BorderLayout.EAST);

        //Configurar panel de contenido interno TipoConsultas
        cardLayoutContenidoTipoConsultas = new CardLayout();
        panelContenidoTiposConsulta = new JPanel(cardLayoutContenidoTipoConsultas);

        panelCrearEditarTipoConsulta = new PanelCrearEditarTipoConsulta(this);

        panelContenidoTiposConsulta.add(panelInicioTipoConsultas, "inicioTipoConsultas");
        panelContenidoTiposConsulta.add(panelCrearEditarTipoConsulta, "crearEditar");

        cardLayoutContenidoTipoConsultas.show(panelContenidoTiposConsulta, "inicioTipoConsultas");
        setRuta(ESTA_RUTA);

        add(panelContenidoTiposConsulta, BorderLayout.CENTER);
    }

    private void iniciarEventosComponentes() {

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

        btnEliminarDeshabilitarConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonEliminarConsulta();
            }
        });

        agregarEfectoResaltado(btnEliminarDeshabilitarConsulta);

        btnHabilitarConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonHabilitarConsulta();
            }
        });

        agregarEfectoResaltado(btnHabilitarConsulta);

        btnOrdenarAlfabeticamente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonesFiltros();
            }
        });

        agregarEfectoResaltado(btnOrdenarAlfabeticamente);

        btnVerConsultasDeshabilitadas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonesFiltros();
            }
        });

        agregarEfectoResaltado(btnVerConsultasDeshabilitadas);

        tablaTipoConsultas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarBotonesTabla();
                }
            }
        });

        panelContenidoTiposConsulta.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                deseleccionarTabla();
            }

        });
    }

    private void deseleccionarTabla() {
        tablaTipoConsultas.clearSelection();
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

    private void actualizarBotonesTabla() {
        boolean hayRegistroSeleccionado = tablaTipoConsultas.getSelectedRow() != -1;

        if (hayRegistroSeleccionado) {
            btnCrearConsulta.setEnabled(false);
            TipoConsulta tipoConsultaSeleccionado = modeloTabla.traerTipoConsulta(tablaTipoConsultas.getSelectedRow());
            if (tipoConsultaSeleccionado.isHabilitado()) {
                btnModificarConsulta.setEnabled(true);
                btnEliminarDeshabilitarConsulta.setEnabled(true);
                btnHabilitarConsulta.setEnabled(false);
            } else {
                btnModificarConsulta.setEnabled(false);
                btnEliminarDeshabilitarConsulta.setEnabled(false);
                btnHabilitarConsulta.setEnabled(true);
            }

        } else {
            btnCrearConsulta.setEnabled(true);
            btnModificarConsulta.setEnabled(false);
            btnEliminarDeshabilitarConsulta.setEnabled(false);
            btnHabilitarConsulta.setEnabled(false);
        }
    }

    private void botonCrearConsulta() {
        panelCrearEditarTipoConsulta.modoCrear();
        cardLayoutContenidoTipoConsultas.show(panelContenidoTiposConsulta, "crearEditar");
        registrarRuta(ESTA_RUTA + " / " + btnCrearConsulta.getText());
    }

    private void botonModificarConsulta() {
        TipoConsulta tipoConsultaSeleccionada = modeloTabla.traerTipoConsulta(tablaTipoConsultas.getSelectedRow());
        panelCrearEditarTipoConsulta.modoEditar(tipoConsultaSeleccionada);
        cardLayoutContenidoTipoConsultas.show(panelContenidoTiposConsulta, "crearEditar");
        registrarRuta(ESTA_RUTA + " / " + btnModificarConsulta.getText());
    }

    private void botonEliminarConsulta() {
        TipoConsulta tipoConsulta = logicaTipoConsulta.traerTipoConsulta(Integer.parseInt(String.valueOf(modeloTabla.getValueAt(tablaTipoConsultas.getSelectedRow(), 0))));

        String mensaje = "¿Seguro que desea eliminar o deshabilitar el tipo de consulta con ID: " + tipoConsulta.getIdTipoConsulta() + ", nombre: " + tipoConsulta.getNombreConsulta() + "?"
                + " Si el tipo de consulta a sido registrado en turnos, entonces de deshabilitará y ya no podrá registrar nuevos turnos con este hasta que sea nuevamente habilitado."
                + " Caso contrario, se eliminará permanentemente.";
        int opcionConfirmacion = JOptionPane.showConfirmDialog(null, mensaje, "Eliminar", JOptionPane.OK_CANCEL_OPTION);

        if (opcionConfirmacion == JOptionPane.OK_OPTION) {

            try {
                logicaTipoConsulta.eliminarDeshabilitar(Integer.parseInt(String.valueOf(modeloTabla.getValueAt(tablaTipoConsultas.getSelectedRow(), 0))));
            } catch (ProblemaPersistencia e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            botonesFiltros();

            JOptionPane.showMessageDialog(null, "Tipo de consulta eliminado/habilitado exitosamente.", "Tipo de consulta eliminado/deshabilitado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void botonHabilitarConsulta() {
        try {
            logicaTipoConsulta.habilitar(modeloTabla.traerTipoConsulta(tablaTipoConsultas.getSelectedRow()));
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        botonesFiltros();

        JOptionPane.showMessageDialog(null, "Tipo de consulta habilitado exitosamente.", "Tipo de consulta habilitado", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void guardarNuevoTipoConsulta(String nombreConsulta, int duracionMinutos, int costo) {
        try {
            logicaTipoConsulta.crearNuevo(nombreConsulta, duracionMinutos, costo);
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        botonesFiltros();

        JOptionPane.showMessageDialog(null, "Nuevo tipo de consulta registrada con éxito.", "Tipo de consulta registrada.", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoTipoConsultas.show(panelContenidoTiposConsulta, "inicioTipoConsultas");
        registrarRuta(ESTA_RUTA);
    }

    @Override
    public void guardarTipoConsultaEditado(TipoConsulta tipoConsultaEditar, String nombreConsulta, int duracionMinutos, int costo) {
        try {
            logicaTipoConsulta.editarDatos(tipoConsultaEditar, nombreConsulta, duracionMinutos, costo);

        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campo inválido", JOptionPane.ERROR_MESSAGE);
            return;

        } catch (TipoConsultaDeshabilitado e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Tipo consulta deshabilitado", JOptionPane.ERROR_MESSAGE);
            return;

        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        botonesFiltros();

        JOptionPane.showMessageDialog(null, "Tipo de consulta editada con éxito.", "Tipo de consulta editada", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoTipoConsultas.show(panelContenidoTiposConsulta, "inicioTipoConsultas");
        registrarRuta(ESTA_RUTA);
    }

    private void botonesFiltros() {

        if (btnOrdenarAlfabeticamente.isSelected() && btnVerConsultasDeshabilitadas.isSelected()) {
            modeloTabla.actualizar(logicaTipoConsulta.ordenarAlfabeticamente(logicaTipoConsulta.traerTodosDeshabilitados()));
            return;
        }

        if (btnOrdenarAlfabeticamente.isSelected()) {
            modeloTabla.actualizar(logicaTipoConsulta.ordenarAlfabeticamente(logicaTipoConsulta.traerTodos()));
            return;
        }

        if (btnVerConsultasDeshabilitadas.isSelected()) {
            modeloTabla.actualizar(logicaTipoConsulta.traerTodosDeshabilitados());
            return;
        }

        modeloTabla.actualizar(logicaTipoConsulta.traerTodos());
    }

    private void actualizarContenidoTabla() {
        modeloTabla.actualizar(logicaTipoConsulta.traerTodos());
    }

    @Override
    public void eventocancelar() {
        cardLayoutContenidoTipoConsultas.show(panelContenidoTiposConsulta, "inicioTipoConsultas");
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

class ModeloTablaTipoConsultas extends AbstractTableModel {

    private List<TipoConsulta> listaTipoConsultas = new ArrayList();
    private final String[] nombreColumnas = {"ID", "Nombre de consulta", "Duración en Minutos", "Costo"};

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

        return switch (columnIndex) {

            case 0 ->
                tipoConsulta.getIdTipoConsulta();

            case 1 ->
                tipoConsulta.getNombreConsulta();

            case 2 ->
                tipoConsulta.getDuracionMinutos();

            case 3 ->
                tipoConsulta.getCosto();

            default ->
                null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
