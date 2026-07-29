package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.clases_utilitarias.LimiteCaracteresDocumentFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import igu.interfaces.AccionesPaciente;
import javax.swing.text.AbstractDocument;

public class PanelCrearEditarPaciente extends JPanel {

    private final AccionesPaciente accionesPaciente;

    private JTextField txtNombre, txtApellido, txtDni, txtTelefono, txtCorreoElectronico;
    private JTextArea txtObservacion;
    private JButton btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar;

    private static final Color COLOR_FONDO_BOTONES = new Color(255, 243, 188);
    private static final Color COLOR_RESALTADO_BOTONES = COLOR_FONDO_BOTONES.brighter();

    private Paciente pacienteEditar = null;

    public PanelCrearEditarPaciente(AccionesPaciente guardarCancelar) {
        this.accionesPaciente = guardarCancelar;

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
        JLabel lblObservacion = new JLabel("Observación (Opcional):");

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

        ((AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new LimiteCaracteresDocumentFilter(Paciente.LONGITUD_MAXIMA_NOMBRE));
        ((AbstractDocument) txtApellido.getDocument()).setDocumentFilter(new LimiteCaracteresDocumentFilter(Paciente.LONGITUD_MAXIMA_APELLIDO));
        ((AbstractDocument) txtDni.getDocument()).setDocumentFilter(new LimiteCaracteresDocumentFilter(Paciente.LONGITUD_MAXIMA_DNI));
        ((AbstractDocument) txtTelefono.getDocument()).setDocumentFilter(new LimiteCaracteresDocumentFilter(Paciente.LONGITUD_MAXIMA_TELEFONO));
        ((AbstractDocument) txtCorreoElectronico.getDocument()).setDocumentFilter(new LimiteCaracteresDocumentFilter(Paciente.LONGITUD_MAXIMA_CORREO_ELECTRONICO));

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

        JPanel panelBotones = FabricaElementos.crearPanelBotonesParaCrearEditar(botones);

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
        //Los JTextFields no devuelven null
        nombre = txtNombre.getText().strip();
        apellido = txtApellido.getText().strip();
        dni = txtDni.getText().strip();
        telefono = txtTelefono.getText().strip();
        correoElectronico = txtCorreoElectronico.getText().strip();
        observacion = txtObservacion.getText().strip();

        if (correoElectronico.isEmpty()) {
            correoElectronico = null;
        }

        if (observacion.isEmpty()) {
            observacion = null;
        }

        boolean aprobado = verificarCamposIngresados(nombre, apellido, dni, telefono, correoElectronico);
        if (aprobado) {

            //CREAR NUEVO PACIENTE
            if (pacienteEditar == null) {
                accionesPaciente.guardarNuevoPaciente(nombre, apellido, dni, telefono, correoElectronico, observacion);

                //EDITAR PACIENTE
            } else {
                accionesPaciente.guardarPacienteEditado(pacienteEditar, nombre, apellido, dni, telefono, correoElectronico, observacion);
            }
        }
    }

    private static boolean verificarCamposIngresados(String nombre, String apellido, String dni, String telefono, String correoElectronico) {
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para el paciente.",
                    "Campo vacío", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (apellido.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un apellido para el paciente.",
                    "Campo vacío", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un número de DNI para el paciente.",
                    "Campo vacío", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (dni.length() != Paciente.LONGITUD_MAXIMA_DNI) {
            JOptionPane.showMessageDialog(null, "El número de DNI para el paciente debe contener 8 dígitos.",
                    "DNI inválido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un número de teléfono para el paciente.",
                    "Campo vacío", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (correoElectronico != null
                && (!correoElectronico.contains("@") || (!correoElectronico.endsWith(".com") && !correoElectronico.endsWith(".es")))) {
            JOptionPane.showMessageDialog(null, "La dirección de correo electrónico del paciente debe contener un @ y un dominio .com o .es.",
                    "Dirección de correo electrónico inválida", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
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
        accionesPaciente.eventoCancelar();
    }

    private void cargarCampos(Paciente pacienteSeleccionado) {
        txtNombre.setText(pacienteSeleccionado.getNombre());
        txtApellido.setText(pacienteSeleccionado.getApellido());
        txtDni.setText(pacienteSeleccionado.getDni());
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
