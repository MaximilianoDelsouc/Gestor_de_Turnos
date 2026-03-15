package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.GuardarCancelarPaciente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import logica.clases.Paciente;

public class PanelCrearEditarPaciente extends JPanel {

    private GuardarCancelarPaciente guardarCancelar;

    private JTextField txtNombre, txtApellido, txtDni, txtTelefono, txtCorreoElectronico;
    private JTextArea txtObservacion;
    private JButton btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar;

    private static final Color COLOR_FONDO_BOTONES = new Color(255, 243, 188);
    private static final Color COLOR_RESALTADO_BOTONES = COLOR_FONDO_BOTONES.brighter();

    private Paciente pacienteEditar = null;

    public PanelCrearEditarPaciente(GuardarCancelarPaciente guardarCancelar) {
        this.guardarCancelar = guardarCancelar;

        iniciarComponentes();
        iniciarEventos();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        //Formulario con información del paciente a agregar o modificar        
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblApellido = new JLabel("Apellido:");
        JLabel lblDni = new JLabel("DNI:");
        JLabel lblTelefono = new JLabel("Número de teléfono:");
        JLabel lblCorreoElectronico = new JLabel("Correo electrónico (Opcional):");
        JLabel lblObservacion = new JLabel("Observación: (Opcional):");

        JLabel[] labelsCampos = {lblNombre, lblApellido, lblDni, lblTelefono, lblCorreoElectronico, lblObservacion};
        Font fuenteLabelsTextFields = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 20);
        for (JLabel label : labelsCampos) {
            label.setFont(fuenteLabelsTextFields);
            label.setForeground(Color.BLACK);
        }

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtDni = new JTextField();
        txtTelefono = new JTextField();
        txtCorreoElectronico = new JTextField();

        JTextField[] textFields = {txtNombre, txtApellido, txtDni, txtTelefono, txtCorreoElectronico};
        Color colorFondoTexts = new Color(255, 243, 188);
        for (JTextField textField : textFields) {
            textField.setColumns(30);
            textField.setFont(fuenteLabelsTextFields);
            textField.setBackground(colorFondoTexts);
            textField.setForeground(Color.BLACK);
            textField.setEditable(true);
        }

        txtObservacion = new JTextArea(5, 30);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);
        txtObservacion.setFont(fuenteLabelsTextFields);
        txtObservacion.setBackground(colorFondoTexts);
        txtObservacion.setForeground(Color.BLACK);
        txtObservacion.setEditable(true);
        JScrollPane scrollTxtObservacion = new JScrollPane(txtObservacion);

        JComponent[] componentesTexto = {txtNombre, txtApellido, txtDni, txtTelefono, txtCorreoElectronico, scrollTxtObservacion};

        JPanel panelAgregarModificarPaciente = FabricaElementos.crearPanelFormulario(labelsCampos, componentesTexto);

        //Panel con botones
        btnGuardar = new JButton("Guardar");
        btnLimpiarTodo = new JButton("Limpiar Todo");
        btnRestablecerTodo = new JButton("Restablecer Todo");
        btnCancelar = new JButton("Cancelar");

        JButton[] botones = {btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar};
        Font fuenteBotones = new Font("Roboto SemiCondensed Medium", Font.BOLD, 18);
        for (JButton boton : botones) {
            boton.setFont(fuenteBotones);
            boton.setForeground(Color.BLACK);
            boton.setBackground(colorFondoTexts);
            boton.setFocusPainted(false);
            boton.setMargin(new Insets(40, 50, 40, 50));
        }

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstrains = new GridBagConstraints();
        gridBagConstrains.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstrains.gridx = 0;

        gridBagConstrains.gridy = 0;
        panelBotones.add(btnGuardar, gridBagConstrains);

        gridBagConstrains.gridy = 1;
        panelBotones.add(btnLimpiarTodo, gridBagConstrains);

        gridBagConstrains.gridy = 2;
        panelBotones.add(btnRestablecerTodo, gridBagConstrains);

        gridBagConstrains.gridy = 3;
        panelBotones.add(btnCancelar, gridBagConstrains);

        //Agregar todo al panel del contenido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelAgregarModificarPaciente, panelBotones);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setEnabled(false); //Desactiva ajuste por el usuario
        splitPane.setDividerSize(0);

        add(splitPane, BorderLayout.CENTER);
    }

    private void iniciarEventos() {

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
        String nombre, apellido, dni, telefono, correoElectronico, observacion;
        nombre = txtNombre.getText().trim();
        apellido = txtApellido.getText().trim();
        dni = txtDni.getText().trim();
        correoElectronico = txtCorreoElectronico.getText().trim();
        telefono = txtTelefono.getText().trim();
        observacion = txtObservacion.getText().trim();

        //Los JTextFields nunca devuelven null
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Verifique los campos de Nombre, Apellido, DNI y Número de Teléfono. Estos no puden estar vacíos",
                    "Campos Vacíos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //CREAR NUEVO PACIENTE
        if (pacienteEditar == null) {
            Paciente paciente = new Paciente(nombre, apellido, dni, telefono, correoElectronico, observacion);
            guardarCancelar.eventoGuardarPacienteNuevo(paciente);

            //EDITAR PACIENTE
        } else {
            pacienteEditar.setNombre(nombre);
            pacienteEditar.setApellido(apellido);
            pacienteEditar.setDni(dni);
            pacienteEditar.setTelefono(telefono);
            pacienteEditar.setCorreoElectronico(correoElectronico);
            pacienteEditar.setObservacion(observacion);
            guardarCancelar.eventoGuardarPacienteEditado(pacienteEditar);
        }
    }

    private void botonLimpiarTodo() {
        JTextComponent[] campos = {txtNombre, txtApellido, txtDni, txtTelefono, txtCorreoElectronico, txtObservacion};
        for (JTextComponent campo : campos) {
            campo.setText("");
        }
    }

    private void botonRestablecerTodo() {
        cargarCampos(pacienteEditar);
    }

    private void botonCancelar() {
        guardarCancelar.eventoCancelar();
    }

    private void cargarCampos(Paciente pacienteSeleccionado) {
        txtNombre.setText(pacienteSeleccionado.getNombre());
        txtApellido.setText(pacienteSeleccionado.getApellido());
        txtDni.setText(pacienteSeleccionado.getTelefono());
        txtTelefono.setText(pacienteSeleccionado.getTelefono());
        txtCorreoElectronico.setText(pacienteSeleccionado.getCorreoElectronico());
        txtObservacion.setText(pacienteSeleccionado.getObservacion());
    }

    public void modoEditar(Paciente paciente) {
        pacienteEditar = paciente;
        cargarCampos(paciente);
        btnLimpiarTodo.setEnabled(false);
        btnRestablecerTodo.setEnabled(true);
    }

    public void modoCrear() {
        pacienteEditar = null;
        botonLimpiarTodo();
        btnLimpiarTodo.setEnabled(true);
        btnRestablecerTodo.setEnabled(false);
    }
}
