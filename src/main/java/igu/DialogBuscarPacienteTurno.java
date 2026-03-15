package igu;

import igu.clases_utilitarias.FabricaElementos;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import logica.LogicaPaciente;
import logica.clases.Paciente;

public class DialogBuscarPacienteTurno extends JDialog {

    private LogicaPaciente logicaPaciente;

    private ModeloTablaPaciente modeloTabla;
    private JTable tablaPacientes;
    private JTextField txtBuscarPorNombre, txtBuscarPorApellido, txtBuscarPorDni;
    private JButton btnBuscar;
    private JButton btnSeleccionar, btnCancelar;

    private static final Color COLOR_FONDO_BOTONES = Color.LIGHT_GRAY;
    private static final Color COLOR_RESALTADO_BOTONES = Color.LIGHT_GRAY.brighter();

    private Paciente pacienteSeleccionado;

    public DialogBuscarPacienteTurno(JFrame ventanaPrincipal, LogicaPaciente logicaPaciente) {
        super(ventanaPrincipal, "Seleccionar paciente para turno", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 600);
        this.logicaPaciente = logicaPaciente;
        iniciarComponentes();
        //pack(); //Ajusta tamaño según componentes
        iniciarEventos();

    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        //Tabla
        modeloTabla = new ModeloTablaPaciente();
        tablaPacientes = FabricaElementos.crearTabla(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPacientes);

        modeloTabla.actualizar(logicaPaciente.traerTodos());

        add(scrollTabla, BorderLayout.CENTER);

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
        }

        btnBuscar = new JButton();
        btnBuscar.setBackground(Color.LIGHT_GRAY);
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

        add(panelOpcionesBusqueda, BorderLayout.NORTH);

        //Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 2));

        btnSeleccionar = new JButton("Seleccionar");
        btnCancelar = new JButton("Cancelar");

        btnSeleccionar.setEnabled(false);
        btnCancelar.setEnabled(true);

        JButton[] botones = {btnSeleccionar, btnCancelar};
        Font fuenteBotones = new Font("Roboto SemiCondensed Medium", Font.BOLD, 18);
        for (JButton boton : botones) {
            boton.setPreferredSize(new Dimension(150, 50));
            boton.setFont(fuenteBotones);
            boton.setForeground(Color.BLACK);
            boton.setBackground(Color.LIGHT_GRAY);
            boton.setFocusPainted(false);
            boton.setMargin(new Insets(8, 8, 8, 8));

            panelBotones.add(boton);
        }

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void iniciarEventos() {

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonBuscar();
            }
        });

        agregarEfectoResaltado(btnBuscar);

        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonSeleccionar();
            }
        });

        agregarEfectoResaltado(btnSeleccionar);

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonCancelar();
            }
        });

        agregarEfectoResaltado(btnCancelar);

        tablaPacientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarBotonSeleccionar();
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

    private void botonBuscar() {
        modeloTabla.actualizar(logicaPaciente.buscar(txtBuscarPorNombre.getText(), txtBuscarPorApellido.getText(), txtBuscarPorDni.getText()));
    }

    private void botonSeleccionar() {
        pacienteSeleccionado = modeloTabla.traerPaciente(tablaPacientes.getSelectedRow());
        dispose();
    }

    private void botonCancelar() {
        pacienteSeleccionado = null;
        dispose();
    }

    private void actualizarBotonSeleccionar() {

        boolean registroSeleccionado = tablaPacientes.getSelectedRow() >= 0;

        btnSeleccionar.setEnabled(registroSeleccionado);
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }
}
