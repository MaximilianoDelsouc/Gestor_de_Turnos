package igu;

import igu.clases_utilitarias.FabricaElementos;
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
import igu.interfaces.GuardarCancelarTipoConsulta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class PanelCrearEditarTipoConsulta extends JPanel {

    private GuardarCancelarTipoConsulta guardarCancelar;

    private JTextField txtNombreTipoConsulta, txtCosto;
    private JComboBox cmbDuracionMinutos;
    private JButton btnGuardar, btnLimpiarTodo, btnRestablecerTodo, btnCancelar;

    private static final Color COLOR_FONDO_BOTONES = new Color(255, 243, 188);
    private static final Color COLOR_RESALTADO_BOTONES = COLOR_FONDO_BOTONES.brighter();

    private TipoConsulta tipoConsultaEditar;

    public PanelCrearEditarTipoConsulta(GuardarCancelarTipoConsulta guardarCancelar) {
        this.guardarCancelar = guardarCancelar;

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

        cmbDuracionMinutos = new JComboBox();
        for (int i = 10; i <= 480; i += 5) {
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

        String nombreTipoConsulta = txtNombreTipoConsulta.getText().trim();
        String duracion = String.valueOf(cmbDuracionMinutos.getSelectedItem());
        String costoTexto = txtCosto.getText().trim();

        if (nombreTipoConsulta.isEmpty() || (costoTexto.isEmpty() || !costoTexto.matches("\\d+"))) {
            JOptionPane.showMessageDialog(null, "Verifique los campos Nombre del tipo de consulta y Costo. Estos no pueden estar vacíos y Costo solo permite números enteros positivos.",
                    "Campos vacíos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int costoValorEntero = 0;
        try {
            costoValorEntero = Integer.valueOf(costoTexto);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Verifique que el campo Costo sea solo números enteros.", "Campo Costo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (costoValorEntero < 0) {
            JOptionPane.showMessageDialog(null, "Verifique que el campo Costo sea igual o mayor a 0.", "Campo Costo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //CREAR NUEVO TIPO DE CONSULTA
        if (tipoConsultaEditar == null) {
            TipoConsulta tipoConsulta = new TipoConsulta(nombreTipoConsulta, Integer.valueOf(duracion), costoValorEntero);
            guardarCancelar.eventoGuardarTipoConsultaNueva(tipoConsulta);

            //EDITAR TIPO DE CONSULTA
        } else {
            tipoConsultaEditar.setNombreConsulta(nombreTipoConsulta);
            tipoConsultaEditar.setDuracionMinutos(Integer.valueOf(duracion));
            tipoConsultaEditar.setCosto(costoValorEntero);
            guardarCancelar.eventoGuardarTipoConsultaEditada(tipoConsultaEditar);
        }
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
        guardarCancelar.eventocancelar();
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
