package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.clases_utilitarias.LimiteCaracteresDocumentFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import logica.clases.TipoConsulta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import igu.interfaces.AccionesTipoConsulta;
import javax.swing.text.AbstractDocument;

public class PanelCrearEditarTipoConsulta extends JPanel {

    private final AccionesTipoConsulta accionesTipoConsulta;

    private JTextField txtNombreTipoConsulta, txtCosto;
    private JComboBox cmbDuracionMinutos;
    private JButton btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar;

    private static final Color COLOR_FONDO_BOTONES = new Color(255, 243, 188);
    private static final Color COLOR_RESALTADO_BOTONES = COLOR_FONDO_BOTONES.brighter();

    private TipoConsulta tipoConsultaEditar;

    public PanelCrearEditarTipoConsulta(AccionesTipoConsulta guardarCancelar) {
        this.accionesTipoConsulta = guardarCancelar;

        iniciarComponentes();
        iniciarEventosComponentes();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        //Formulario de los datos del tipo de consulta
        JLabel lblNombreTipoConsulta = new JLabel("Nombre del tipo de consulta:");
        JLabel lblDuracionMinutos = new JLabel("Duración en minutos:");
        JLabel lblCosto = new JLabel("Costo:");

        JLabel[] labelsCampos = {lblNombreTipoConsulta, lblDuracionMinutos, lblCosto};
        Font fuenteLabelsTextFields = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 20);
        for (JLabel label : labelsCampos) {
            label.setFont(fuenteLabelsTextFields);
            label.setForeground(Color.BLACK);
        }

        txtNombreTipoConsulta = new JTextField();
        txtCosto = new JTextField("0");

        JTextField[] textFields = {txtNombreTipoConsulta, txtCosto};
        Color colorFondoTexts = new Color(255, 243, 188);
        for (JTextField textField : textFields) {
            textField.setColumns(30);
            textField.setFont(fuenteLabelsTextFields);
            textField.setBackground(colorFondoTexts);
            textField.setForeground(Color.BLACK);
            textField.setEditable(true);
        }

        ((AbstractDocument) txtNombreTipoConsulta.getDocument()).setDocumentFilter(new LimiteCaracteresDocumentFilter(TipoConsulta.LONGITUD_MAXIMA_NOMBRE_CONSULTA));

        cmbDuracionMinutos = new JComboBox();
        for (int i = 10; i <= TipoConsulta.DURACION_MAXIMA_MINUTOS; i += 5) {
            cmbDuracionMinutos.addItem(i);
        }
        cmbDuracionMinutos.setPreferredSize(new Dimension(480, 30));
        cmbDuracionMinutos.setFont(fuenteLabelsTextFields);
        cmbDuracionMinutos.setBackground(colorFondoTexts);
        cmbDuracionMinutos.setForeground(Color.BLACK);
        cmbDuracionMinutos.setEditable(false);

        JComponent[] componentesTexto = {txtNombreTipoConsulta, cmbDuracionMinutos, txtCosto};

        JPanel panelCrearEditarTipoConsulta = FabricaElementos.crearPanelFormulario(labelsCampos, componentesTexto);

        //Panel con botones
        btnGuardar = new JButton("Guardar");
        btnLimpiarTodo = new JButton("Limpiar Todo");
        btnRestablecerTodo = new JButton("Restablecer Todo");
        btnCancelar = new JButton("Cancelar");

        JButton[] botones = {btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar};

        JPanel panelBotones = FabricaElementos.crearPanelBotonesParaCrearEditar(botones);

        //Agregar todo al panel del contenido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCrearEditarTipoConsulta, panelBotones);
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
        String nombreTipoConsulta = txtNombreTipoConsulta.getText().strip();
        int minutosDuracion = Integer.parseInt(String.valueOf(cmbDuracionMinutos.getSelectedItem()));
        int costo;
        try {
            costo = Integer.parseInt(txtCosto.getText().strip());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El costo del tipo de consulta debe ser solo número enteros.", "Campo Costo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean aprobado = verificarCamposIngresados(nombreTipoConsulta, costo);
        if (aprobado) {

            //CREAR NUEVO TIPO DE CONSULTA
            if (tipoConsultaEditar == null) {
                accionesTipoConsulta.guardarNuevoTipoConsulta(nombreTipoConsulta, minutosDuracion, costo);

                //EDITAR TIPO DE CONSULTA
            } else {
                accionesTipoConsulta.guardarTipoConsultaEditado(tipoConsultaEditar, nombreTipoConsulta, minutosDuracion, costo);
            }
        }
    }

    private static boolean verificarCamposIngresados(String nombreTipoConsulta, int costo) {
        if (nombreTipoConsulta.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para el tipo de consulta.",
                    "Campo vacío", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (costo < 0) {
            JOptionPane.showMessageDialog(null, "El costo del tipo de consulta no puede ser menor a 0 (cero).",
                    "Campo Costo inválido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void botonLimpiarTodo() {
        txtNombreTipoConsulta.setText("");
        cmbDuracionMinutos.setSelectedIndex(0);
        txtCosto.setText("0");
    }

    private void botonRestablecerTodo() {
        cargarCampos(tipoConsultaEditar);
    }

    private void botonCancelar() {
        accionesTipoConsulta.eventocancelar();
    }

    private void cargarCampos(TipoConsulta tipoConsulta) {
        txtNombreTipoConsulta.setText(tipoConsulta.getNombreConsulta());
        cmbDuracionMinutos.setSelectedItem(tipoConsulta.getDuracionMinutos());
        txtCosto.setText(String.valueOf(tipoConsulta.getCosto()));
    }

    public void modoCrear() {
        tipoConsultaEditar = null;
        botonLimpiarTodo();
        btnLimpiarTodo.setEnabled(true);
        btnRestablecerTodo.setEnabled(false);
    }

    public void modoEditar(TipoConsulta tipoConsulta) {
        tipoConsultaEditar = tipoConsulta;
        cargarCampos(tipoConsulta);
        btnLimpiarTodo.setEnabled(false);
        btnRestablecerTodo.setEnabled(true);
    }
}
