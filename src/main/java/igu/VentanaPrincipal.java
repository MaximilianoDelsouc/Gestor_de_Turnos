package igu;

import igu.interfaces.ActualizarRuta;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import logica.LogicaPaciente;
import logica.LogicaTipoConsulta;
import logica.LogicaTurno;
import logica.clases.Turno;

public class VentanaPrincipal extends JFrame implements ActualizarRuta {

    private PanelInicio panelInicio;
    private PanelPacientes panelPacientes;
    private PanelTipoConsultas panelTipoConsultas;
    private PanelTurnos panelTurnos;

    private JLabel lblRuta, lblFechaHora;
    private JToggleButton btnInicio, btnPacientes, btnTipoConsultas, btnTurnos;
    private JPanel panContenido;
    private CardLayout cardLayoutContenido;

    private static final Color COLOR_FONDO_INTERFAZ = new Color(55, 125, 34);
    private static final Color COLOR_SELECCIONADO_BOTONES = new Color(45, 102, 27);
    private static final Color COLOR_RESALTADO_BOTONES = new Color(70, 158, 43);

    public VentanaPrincipal(LogicaPaciente logicaPaciente, LogicaTipoConsulta logicaTipoConsulta, LogicaTurno logicaTurno) {
        iniciarComponentes(logicaPaciente, logicaTipoConsulta, logicaTurno);
        iniciarEventos();
        revisarTurnosAyer(logicaTurno);
    }

    private void iniciarComponentes(LogicaPaciente logicaPaciente, LogicaTipoConsulta logicaTipoConsulta, LogicaTurno logicaTurno) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        getContentPane().setBackground(Color.WHITE);
        setTitle("Gestor de Turnos");
        setLayout(new BorderLayout());

        JPanel panelInterfaz = new JPanel();
        panelInterfaz.setLayout(new BorderLayout());

        //Panel que muestra ruta, fecha y hora actual
        JPanel panelRutaFecha = new JPanel();
        panelRutaFecha.setBackground(COLOR_FONDO_INTERFAZ);
        panelRutaFecha.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panelRutaFecha.setLayout(new BoxLayout(panelRutaFecha, BoxLayout.Y_AXIS));

        lblRuta = new JLabel("{Ruta/ruta/ruta}");
        lblFechaHora = new JLabel("{Fecha y hora}");
        lblRuta.setFont(new Font("Roboto SemiCondensed Medium", Font.BOLD, 34));
        lblFechaHora.setFont(new Font("Roboto SemiCondensed Medium", Font.BOLD, 28));
        lblRuta.setForeground(Color.WHITE);
        lblFechaHora.setForeground(Color.WHITE);

        iniciarReloj(lblFechaHora);

        panelRutaFecha.add(lblRuta);
        panelRutaFecha.add(Box.createVerticalStrut(20));
        panelRutaFecha.add(lblFechaHora);

        panelInterfaz.add(panelRutaFecha, BorderLayout.NORTH);

        //Panel de pestañas para moverse por la app
        JPanel panelPestanas = new JPanel();
        panelPestanas.setLayout(new GridLayout(1, 4));

        btnInicio = new JToggleButton("Inicio");
        btnPacientes = new JToggleButton("Pacientes");
        btnTipoConsultas = new JToggleButton("Tipos de Consultas");
        btnTurnos = new JToggleButton("Turnos");

        ButtonGroup botonesMenu = new ButtonGroup();

        JToggleButton[] botones = {btnInicio, btnPacientes, btnTipoConsultas, btnTurnos};
        Font fuenteBotones = new Font("Roboto SemiCondensed Medium", Font.BOLD, 28);
        for (JToggleButton boton : botones) {
            boton.setBackground(COLOR_FONDO_INTERFAZ);
            boton.setContentAreaFilled(false);
            boton.setOpaque(true);
            boton.setFocusPainted(false);
            boton.setFont(fuenteBotones);
            boton.setForeground(Color.WHITE);

            botonesMenu.add(boton);
            panelPestanas.add(boton);
        }

        panelInterfaz.add(panelPestanas, BorderLayout.SOUTH);

        add(panelInterfaz, BorderLayout.NORTH);

        //Configurar panel de contenido
        cardLayoutContenido = new CardLayout();
        panContenido = new JPanel(cardLayoutContenido);

        panelInicio = new PanelInicio(logicaTurno);
        panelPacientes = new PanelPacientes(logicaPaciente, this);
        panelTipoConsultas = new PanelTipoConsultas(logicaTipoConsulta, this);
        panelTurnos = new PanelTurnos(logicaTurno, logicaTipoConsulta, logicaPaciente, panelInicio, this);

        panContenido.add(panelInicio, "inicio");
        panContenido.add(panelPacientes, "pacientes");
        panContenido.add(panelTipoConsultas, "tipoConsultas");
        panContenido.add(panelTurnos, "turnos");

        cardLayoutContenido.show(panContenido, "inicio");
        actualizar("Inicio");

        add(panContenido, BorderLayout.CENTER);
    }

    private void iniciarEventos() {

        btnInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonInicio();
            }
        });

        agregarEfectoSeleccion(btnInicio);
        agregarEfectoResaltado(btnInicio);
        btnInicio.setSelected(true);

        btnPacientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonPacientes();
            }
        });

        agregarEfectoSeleccion(btnPacientes);
        agregarEfectoResaltado(btnPacientes);

        btnTipoConsultas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonTiposConsultas();
            }
        });

        agregarEfectoSeleccion(btnTipoConsultas);
        agregarEfectoResaltado(btnTipoConsultas);

        btnTurnos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonTurnos();
            }
        });

        agregarEfectoSeleccion(btnTurnos);
        agregarEfectoResaltado(btnTurnos);
    }

    private void botonInicio() {
        cardLayoutContenido.show(panContenido, "inicio");
        actualizar(panelInicio.getRuta());
    }

    private void botonPacientes() {
        cardLayoutContenido.show(panContenido, "pacientes");
        actualizar(panelPacientes.getRuta());
    }

    private void botonTiposConsultas() {
        cardLayoutContenido.show(panContenido, "tipoConsultas");
        actualizar(panelTipoConsultas.getRuta());
    }

    private void botonTurnos() {
        cardLayoutContenido.show(panContenido, "turnos");
        actualizar(panelTurnos.getRuta());
    }

    private void agregarEfectoResaltado(JToggleButton boton) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (!boton.isSelected()) {
                    boton.setBackground(COLOR_FONDO_INTERFAZ);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!boton.isSelected()) {
                    boton.setBackground(COLOR_RESALTADO_BOTONES);
                }
            }
        });
    }

    private void agregarEfectoSeleccion(JToggleButton boton) {
        boton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (boton.isSelected()) {
                    boton.setBackground(COLOR_SELECCIONADO_BOTONES);
                } else {
                    boton.setBackground(COLOR_FONDO_INTERFAZ);
                }
            }
        });
    }

    private void iniciarReloj(JLabel lblFechaHoy) {
        Timer temporizador = new Timer();
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Date fechaHoy = new Date();

                //Formato de hora
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                String horaActualFormato = simpleDateFormat.format(fechaHoy);

                horaActualFormato += (fechaHoy.getHours() < 12) ? " AM, " : " PM, ";

                //Formato de fecha
                simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                String fechaHoyFormato = simpleDateFormat.format(fechaHoy);

                String[] palabras = fechaHoyFormato.split(" ");

                String textoFechaMayusculas = "";
                for (String palabra : palabras) {
                    textoFechaMayusculas += palabra.toUpperCase().charAt(0) + palabra.substring(1, palabra.length()) + " ";
                }

                lblFechaHoy.setText(horaActualFormato + textoFechaMayusculas);
            }
        };

        temporizador.scheduleAtFixedRate(tarea, 0, 60000);
    }

    private void revisarTurnosAyer(LogicaTurno logicaTurno) {
        List<Turno> turnosAyerPendientes = logicaTurno.traerTurnosAyerPendientes();

        if (!turnosAyerPendientes.isEmpty()) {
            Calendar calendarioAyer = Calendar.getInstance();
            calendarioAyer.setTime(new Date());
            calendarioAyer.add(Calendar.DAY_OF_MONTH, -1);
            SimpleDateFormat formatoFecha = new SimpleDateFormat("EEEE, MMMM dd");

            int cantidadTurnos = turnosAyerPendientes.size();

            JOptionPane.showMessageDialog(null, "Ayer (" + formatoFecha.format(calendarioAyer.getTime()) + ") ha/n quedado " + cantidadTurnos + " turno/s en estado de pendiente. Revíselo/s para tener una correcta consistencia de datos.",
                    "Turno/s pendiente/s de ayer", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void actualizar(String ruta) {
        lblRuta.setText(ruta);
    }
}
