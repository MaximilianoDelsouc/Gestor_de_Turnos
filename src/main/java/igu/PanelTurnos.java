package igu;

import igu.clases_utilitarias.FabricaElementos;
import igu.interfaces.ActualizarRuta;
import igu.interfaces.ActualizarTurnosHoy;
import igu.interfaces.BuscarPacienteTurno;
import igu.interfaces.GuardarCancelarTurno;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import logica.LogicaTipoConsulta;
import logica.LogicaTurno;
import logica.clases.TipoConsulta;
import logica.clases.Turno;
import logica.exceptions.CampoInvalido;
import igu.interfaces.TraerHorariosDisponibles;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import logica.LogicaPaciente;
import logica.clases.Turno.Estado;
import logica.exceptions.EstadoInvalido;
import logica.exceptions.TurnoReprogramarPasado;
import persistencia.exceptions.ProblemaPersistencia;

public class PanelTurnos extends JPanel implements GuardarCancelarTurno, BuscarPacienteTurno, TraerHorariosDisponibles {

    private LogicaTurno logicaTurno;
    private LogicaTipoConsulta logicaTipoConsulta;
    private LogicaPaciente logicaPaciente;
    private ActualizarTurnosHoy actualizarTurnosHoy;

    private ActualizarRuta actualizarRuta;
    private String ruta;
    private final static String ESTA_RUTA = "Turnos";

    private JPanel panelContenidoTurnos;
    private CardLayout cardLayoutContenidoTurnos;
    private PanelCrearReprogramarTurno panelCrearReprogramarTurno;

    private ModeloTablaTurno modeloTabla = new ModeloTablaTurno();
    private JTable tablaTurnos;
    private JSpinner spnBuscarPorFecha;
    private JToggleButton btnBuscarPorFecha;
    private JRadioButton rbFiltrarEstadoPendiente, rbFiltrarEstadoConfirmado, rbFiltrarEstadoCancelado, rbFiltrarBuscarEstadoAtendido, rbFiltrarEstadoAusentado;
    private ButtonGroup grupoBotonesBuscarPorEstado;
    private JButton btnFiltrarEstadoTodos;
    private JButton btnNuevoTurno, btnConfirmarTurno, btnAtenderTurno, btnReprogramarTurno, btnCancelarTurno;
    private JToggleButton btnOrdenarPorFechaHora;
    private Date fechaBuscada;
    private Estado estadoBuscado;
    private boolean ordenarPorFecha = false;

    private static final Color COLOR_FONDO_BOTONES = Color.LIGHT_GRAY;
    private static final Color COLOR_RESALTADO_BOTONES = Color.LIGHT_GRAY.brighter();

    public PanelTurnos(LogicaTurno logicaTurno, LogicaTipoConsulta logicaTipoConsulta, LogicaPaciente logicaPaciente, ActualizarTurnosHoy actualizarTurnosHoy, ActualizarRuta actualizarRuta) {
        this.logicaTurno = logicaTurno;
        this.logicaTipoConsulta = logicaTipoConsulta;
        this.logicaPaciente = logicaPaciente;
        this.actualizarTurnosHoy = actualizarTurnosHoy;
        this.actualizarRuta = actualizarRuta;
        iniciarComponentes();
        iniciarEventos();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout());

        JPanel panelInicioTurnos = new JPanel(new BorderLayout());

        panelInicioTurnos.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        //Tabla de turnos
        modeloTabla = new ModeloTablaTurno();
        tablaTurnos = FabricaElementos.crearTabla(modeloTabla);
        tablaTurnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaTurnos);

        modeloTabla.actualizar(logicaTurno.traerTodos());

        panelInicioTurnos.add(scrollTabla, BorderLayout.CENTER);

        //Panel de filtros de busqueda
        JLabel lblBuscarPorFecha = new JLabel("Buscar por Fecha:");
        JLabel lblVerPorEstado = new JLabel("Ver por Estado:");

        Font fuenteFiltros = new Font("Roboto SemiCondensed Medium", Font.PLAIN, 18);

        lblBuscarPorFecha.setFont(fuenteFiltros);
        spnBuscarPorFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFecha = new JSpinner.DateEditor(spnBuscarPorFecha, "dd/MM/yyyy");
        JFormattedTextField texto = editorFecha.getTextField();
        texto.setColumns(10);
        texto.setBackground(new Color(255, 243, 188));
        texto.setEditable(false); // Bloquear edición manual        
        spnBuscarPorFecha.setEditor(editorFecha);
        spnBuscarPorFecha.setFont(fuenteFiltros);

        btnBuscarPorFecha = new JToggleButton();
        btnBuscarPorFecha.setPreferredSize(new Dimension(40, 40));
        btnBuscarPorFecha.setBackground(COLOR_FONDO_BOTONES);
        btnBuscarPorFecha.setToolTipText("Buscar por fecha");

        lblVerPorEstado.setFont(fuenteFiltros);
        rbFiltrarEstadoPendiente = new JRadioButton("Pendiente");
        rbFiltrarEstadoConfirmado = new JRadioButton("Confirmado");
        rbFiltrarEstadoCancelado = new JRadioButton("Cancelado");
        rbFiltrarBuscarEstadoAtendido = new JRadioButton("Atendido");
        rbFiltrarEstadoAusentado = new JRadioButton("Ausentado");
        btnFiltrarEstadoTodos = new JButton("Todos");

        JButton btnMasInformacionEstadoAusentado = new JButton();
        btnMasInformacionEstadoAusentado.setPreferredSize(new Dimension(20, 20));
        btnMasInformacionEstadoAusentado.setFont(fuenteFiltros);
        btnMasInformacionEstadoAusentado.setForeground(Color.BLACK);
        btnMasInformacionEstadoAusentado.setBackground(Color.WHITE);
        btnMasInformacionEstadoAusentado.setToolTipText("Más información sobre el estado 'Ausentado'");
        btnMasInformacionEstadoAusentado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Todos los turnos que se encuentren en el estado de Confirmado al pasar su hora de inicio se cambiarán a Ausentado.",
                        "Más información sobre el estado 'Ausentado'", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JRadioButton[] botonesRadio = {rbFiltrarEstadoPendiente, rbFiltrarEstadoConfirmado, rbFiltrarEstadoCancelado, rbFiltrarBuscarEstadoAtendido, rbFiltrarEstadoAusentado};
        grupoBotonesBuscarPorEstado = new ButtonGroup();
        for (JRadioButton boton : botonesRadio) {
            boton.setFont(fuenteFiltros);
            boton.setFocusPainted(false);
            grupoBotonesBuscarPorEstado.add(boton);
        }

        btnFiltrarEstadoTodos.setFocusPainted(false);
        btnFiltrarEstadoTodos.setFont(fuenteFiltros);
        btnFiltrarEstadoTodos.setForeground(Color.BLACK);
        btnFiltrarEstadoTodos.setBackground(COLOR_FONDO_BOTONES);

        btnOrdenarPorFechaHora = new JToggleButton("Ordenar por fecha y hora");
        btnOrdenarPorFechaHora.setToolTipText("Ordenar los turnos por fecha y hora de más próxima a más lejana.");
        btnOrdenarPorFechaHora.setFocusPainted(false);
        btnOrdenarPorFechaHora.setFont(fuenteFiltros);
        btnOrdenarPorFechaHora.setForeground(Color.BLACK);
        btnOrdenarPorFechaHora.setBackground(Color.LIGHT_GRAY);

        JPanel panelBusquedaPorFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBusquedaPorFecha.add(Box.createHorizontalStrut(10));
        panelBusquedaPorFecha.add(lblBuscarPorFecha);
        panelBusquedaPorFecha.add(Box.createHorizontalStrut(30));
        panelBusquedaPorFecha.add(spnBuscarPorFecha);
        panelBusquedaPorFecha.add(Box.createHorizontalStrut(30));
        panelBusquedaPorFecha.add(btnBuscarPorFecha);

        JPanel panelVerPorEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelVerPorEstado.add(Box.createHorizontalStrut(20));
        panelVerPorEstado.add(lblVerPorEstado);
        panelVerPorEstado.add(Box.createHorizontalStrut(30));
        panelVerPorEstado.add(rbFiltrarEstadoPendiente);
        panelVerPorEstado.add(Box.createHorizontalStrut(30));
        panelVerPorEstado.add(rbFiltrarEstadoConfirmado);
        panelVerPorEstado.add(Box.createHorizontalStrut(30));
        panelVerPorEstado.add(rbFiltrarEstadoCancelado);
        panelVerPorEstado.add(Box.createHorizontalStrut(30));
        panelVerPorEstado.add(rbFiltrarBuscarEstadoAtendido);
        panelVerPorEstado.add(Box.createHorizontalStrut(30));
        panelVerPorEstado.add(rbFiltrarEstadoAusentado);
        panelVerPorEstado.add(Box.createHorizontalStrut(5));
        panelVerPorEstado.add(btnMasInformacionEstadoAusentado);
        panelVerPorEstado.add(Box.createHorizontalStrut(30));
        panelVerPorEstado.add(btnFiltrarEstadoTodos);

        JPanel panelBotonOrdenarPorFecha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonOrdenarPorFecha.add(btnOrdenarPorFechaHora);

        JPanel panelFiltrosBusqueda = new JPanel();
        panelFiltrosBusqueda.setLayout(new BoxLayout(panelFiltrosBusqueda, BoxLayout.Y_AXIS));
        panelFiltrosBusqueda.add(panelBusquedaPorFecha);
        panelFiltrosBusqueda.add(Box.createVerticalStrut(10));
        panelFiltrosBusqueda.add(panelVerPorEstado);
        panelFiltrosBusqueda.add(Box.createVerticalStrut(10));
        panelFiltrosBusqueda.add(panelBotonOrdenarPorFecha);
        panelFiltrosBusqueda.add(Box.createVerticalStrut(10));

        panelInicioTurnos.add(panelFiltrosBusqueda, BorderLayout.NORTH);

        //Panel botones
        btnNuevoTurno = new JButton("Nuevo");
        btnConfirmarTurno = new JButton("Confirmar");
        btnAtenderTurno = new JButton("Atender");
        btnReprogramarTurno = new JButton("Reprogramar");
        btnCancelarTurno = new JButton("Cancelar");

        btnNuevoTurno.setEnabled(true);
        btnConfirmarTurno.setEnabled(false);
        btnAtenderTurno.setEnabled(false);
        btnReprogramarTurno.setEnabled(false);
        btnCancelarTurno.setEnabled(false);

        JButton[] botonesTabla = {btnNuevoTurno, btnConfirmarTurno, btnAtenderTurno, btnReprogramarTurno, btnCancelarTurno};
        JPanel panelBotonesTabla = FabricaElementos.crearPanelBotonesParaTabla(botonesTabla);

        panelInicioTurnos.add(panelBotonesTabla, BorderLayout.EAST);

        //Configurar panel de contenido interno Paciente
        cardLayoutContenidoTurnos = new CardLayout();
        panelContenidoTurnos = new JPanel(cardLayoutContenidoTurnos);

        panelCrearReprogramarTurno = new PanelCrearReprogramarTurno(this, this, this);

        panelContenidoTurnos.add(panelInicioTurnos, "inicioTurnos");
        panelContenidoTurnos.add(panelCrearReprogramarTurno, "crearReprogramar");

        cardLayoutContenidoTurnos.show(panelContenidoTurnos, "inicioTurnos");
        setRuta(ESTA_RUTA);

        add(panelContenidoTurnos, BorderLayout.CENTER);
    }

    private void iniciarEventos() {

        btnNuevoTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonNuevoTurno();
            }
        });

        agregarEfectoResaltado(btnNuevoTurno);

        btnConfirmarTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonConfirmarTurno();
            }
        });

        agregarEfectoResaltado(btnConfirmarTurno);

        btnAtenderTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonAtenderTurno();
            }
        });

        agregarEfectoResaltado(btnAtenderTurno);

        btnReprogramarTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonReprogramarTurno();
            }
        });

        agregarEfectoResaltado(btnReprogramarTurno);

        btnCancelarTurno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonCancelarTurno();
            }
        });

        agregarEfectoResaltado(btnCancelarTurno);

        spnBuscarPorFecha.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (btnBuscarPorFecha.isSelected()) {
                    botonBuscarPorFecha();
                }
            }
        });

        btnBuscarPorFecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnBuscarPorFecha.isSelected()) {
                    botonBuscarPorFecha();
                } else {
                    resetearBusquedaPorFecha();
                }
            }
        });

        agregarEfectoResaltado(btnBuscarPorFecha);

        rbFiltrarEstadoPendiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonesBuscarPorEstado(Turno.Estado.PENDIENTE);
            }
        });

        rbFiltrarEstadoConfirmado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonesBuscarPorEstado(Turno.Estado.CONFIRMADO);
            }
        });

        rbFiltrarEstadoCancelado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonesBuscarPorEstado(Turno.Estado.CANCELADO);
            }
        });

        rbFiltrarBuscarEstadoAtendido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonesBuscarPorEstado(Turno.Estado.ATENDIDO);
            }
        });

        rbFiltrarEstadoAusentado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonesBuscarPorEstado(Turno.Estado.AUSENTADO);
            }
        });

        btnFiltrarEstadoTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grupoBotonesBuscarPorEstado.clearSelection();
                resetearFiltroPorEstado();
            }
        });

        agregarEfectoResaltado(btnFiltrarEstadoTodos);

        btnOrdenarPorFechaHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnOrdenarPorFechaHora.isSelected()) {
                    botonOrdenarPorFecha();
                } else {
                    resetearOrdenarPorFecha();
                }
            }
        });

        agregarEfectoResaltado(btnOrdenarPorFechaHora);

        tablaTurnos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarBotonesTabla();
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

    private void botonNuevoTurno() {
        panelCrearReprogramarTurno.modoCrear(logicaTipoConsulta.traerTodos());
        cardLayoutContenidoTurnos.show(panelContenidoTurnos, "crearReprogramar");
        registrarRuta(ESTA_RUTA + " / " + btnNuevoTurno.getText());
    }

    private void botonConfirmarTurno() {
        try {
            logicaTurno.confirmarTurno(modeloTabla.traerTurno(tablaTurnos.getSelectedRow()).getIdTurno());
        } catch (EstadoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Cambio de estado inválido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con Base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Estado cambiado a Confirmado exitosamente.", "Turno Confirmado", JOptionPane.INFORMATION_MESSAGE);

        actualizarTurnosHoy.actualizarTurnosHoy();

        if (btnBuscarPorFecha.isSelected()) {
            botonBuscarPorFecha();
            return;
        }

        if (estadoBuscado != null) {
            botonesBuscarPorEstado(estadoBuscado);
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    private void botonAtenderTurno() {
        try {
            logicaTurno.atenderTurno(modeloTabla.traerTurno(tablaTurnos.getSelectedRow()).getIdTurno());
        } catch (EstadoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Cambio de estado inválido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con Base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Estado cambiado a Atendido exitosamente.", "Turno Atendido", JOptionPane.INFORMATION_MESSAGE);

        actualizarTurnosHoy.actualizarTurnosHoy();

        if (btnBuscarPorFecha.isSelected()) {
            botonBuscarPorFecha();
            return;
        }

        if (estadoBuscado != null) {
            botonesBuscarPorEstado(estadoBuscado);
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    private void botonReprogramarTurno() {
        Turno turnoSeleccionado = modeloTabla.traerTurno(tablaTurnos.getSelectedRow());
        panelCrearReprogramarTurno.modoReprogramar(logicaTipoConsulta.traerTodos(), turnoSeleccionado);
        cardLayoutContenidoTurnos.show(panelContenidoTurnos, "crearReprogramar");
        registrarRuta(ESTA_RUTA + " / " + btnReprogramarTurno.getText());
    }

    private void botonCancelarTurno() {
        try {
            logicaTurno.cancelarTurno(modeloTabla.traerTurno(tablaTurnos.getSelectedRow()).getIdTurno());
        } catch (EstadoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Cambio de estado inválido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con Base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Estado cambiado a Cancelado exitosamente.", "Turno Cancelado", JOptionPane.INFORMATION_MESSAGE);

        actualizarTurnosHoy.actualizarTurnosHoy();

        if (btnBuscarPorFecha.isSelected()) {
            botonBuscarPorFecha();
            return;
        }

        if (estadoBuscado != null) {
            botonesBuscarPorEstado(estadoBuscado);
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    private void botonBuscarPorFecha() {
        fechaBuscada = (Date) spnBuscarPorFecha.getValue();

        if (estadoBuscado != null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.filtrarPorFechaEstado(fechaBuscada, estadoBuscado)));
            return;
        }

        if (estadoBuscado != null && !ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.filtrarPorFechaEstado(fechaBuscada, estadoBuscado));
            return;
        }

        if (estadoBuscado == null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.buscarPorFecha(fechaBuscada)));
            return;
        }

        modeloTabla.actualizar(logicaTurno.buscarPorFecha(fechaBuscada));
    }

    private void botonesBuscarPorEstado(Turno.Estado estadoBuscado) {
        this.estadoBuscado = estadoBuscado;

        if (fechaBuscada != null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.filtrarPorFechaEstado(fechaBuscada, estadoBuscado)));
            return;
        }

        if (fechaBuscada != null && !ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.filtrarPorFechaEstado(fechaBuscada, estadoBuscado));
            return;
        }

        if (fechaBuscada == null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.filtrarPorEstado(estadoBuscado)));
            return;
        }

        modeloTabla.actualizar(logicaTurno.filtrarPorEstado(estadoBuscado));
    }

    private void resetearBusquedaPorFecha() {
        fechaBuscada = null;

        if (estadoBuscado != null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.filtrarPorEstado(estadoBuscado)));
            return;
        }

        if (estadoBuscado != null && !ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.filtrarPorEstado(estadoBuscado));
            return;
        }

        if (estadoBuscado == null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.traerTodos()));
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    private void resetearFiltroPorEstado() {
        estadoBuscado = null;

        if (fechaBuscada != null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.buscarPorFecha(fechaBuscada)));
            return;
        }

        if (fechaBuscada != null && !ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.buscarPorFecha(fechaBuscada));
            return;
        }

        if (fechaBuscada == null && ordenarPorFecha) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.traerTodos()));
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    private void botonOrdenarPorFecha() {
        ordenarPorFecha = true;

        if (fechaBuscada != null && estadoBuscado != null) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.filtrarPorFechaEstado(fechaBuscada, estadoBuscado)));
            return;
        }

        if (fechaBuscada != null && estadoBuscado == null) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.buscarPorFecha(fechaBuscada)));
            return;
        }

        if (fechaBuscada == null && estadoBuscado != null) {
            modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.filtrarPorEstado(estadoBuscado)));
            return;
        }

        modeloTabla.actualizar(logicaTurno.ordenarPorFechaCercanaLejana(logicaTurno.traerTodos()));
    }

    private void resetearOrdenarPorFecha() {
        ordenarPorFecha = false;

        if (fechaBuscada != null && estadoBuscado != null) {
            modeloTabla.actualizar(logicaTurno.filtrarPorFechaEstado(fechaBuscada, estadoBuscado));
            return;
        }

        if (fechaBuscada != null && estadoBuscado == null) {
            modeloTabla.actualizar(logicaTurno.buscarPorFecha(fechaBuscada));
            return;
        }

        if (fechaBuscada == null && estadoBuscado != null) {
            modeloTabla.actualizar(logicaTurno.filtrarPorEstado(estadoBuscado));
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    private void actualizarBotonesTabla() {
        if (tablaTurnos.getSelectedRow() == -1) {
            btnNuevoTurno.setEnabled(true);
            btnConfirmarTurno.setEnabled(false);
            btnAtenderTurno.setEnabled(false);
            btnReprogramarTurno.setEnabled(false);
            btnCancelarTurno.setEnabled(false);

        } else {
            btnNuevoTurno.setEnabled(false);

            Turno turnoSeleccionado = modeloTabla.traerTurno(tablaTurnos.getSelectedRow());

            btnConfirmarTurno.setEnabled(turnoSeleccionado.puedeConfirmar());
            btnAtenderTurno.setEnabled(turnoSeleccionado.puedeAtender());
            btnReprogramarTurno.setEnabled((turnoSeleccionado.puedeReprogramar() && turnoSeleccionado.getHoraInicio().after(new Date())) ? true : false);
            btnCancelarTurno.setEnabled(turnoSeleccionado.puedeCancelar());
        }
    }

    private void registrarRuta(String ruta) {
        actualizarRuta.actualizarRuta(ruta);
        setRuta(ruta);
    }

    @Override
    public Map<Date, Date> eventoTraerHorariosDisponibles(Date fecha, TipoConsulta tipoConsulta, Turno turnoIgnorar) {
        return logicaTurno.traerHorariosDisponibles(fecha, tipoConsulta, turnoIgnorar);
    }

    @Override
    public void eventoGuardarTurnoNuevo(Turno turno) {
        try {
            logicaTurno.crearNuevo(turno);
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campo Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con Base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Nuevo turno registrado con éxito.", "Turno registrado", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoTurnos.show(panelContenidoTurnos, "inicioTurnos");
        registrarRuta(ESTA_RUTA);

        actualizarTurnosHoy.actualizarTurnosHoy();

        if (btnBuscarPorFecha.isSelected()) {
            botonBuscarPorFecha();
            return;
        }

        if (estadoBuscado != null) {
            botonesBuscarPorEstado(estadoBuscado);
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    @Override
    public void eventoReprogramarTurno(Turno turnoReprogramado, Turno nuevoTurno) {
        try {
            logicaTurno.reprogramarTurno(turnoReprogramado, nuevoTurno);
        } catch (TurnoReprogramarPasado e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Turno a reprogramar no válido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (CampoInvalido e) {
            JOptionPane.showMessageDialog(null, e.getMensaje(),
                    "Campo Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ProblemaPersistencia e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un problema con la base de datos. Comuníquese con su desarrolador y/o intente más tarde.",
                    "Problema con Base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Turno reprogramado con éxito.", "Turno reprogramado", JOptionPane.INFORMATION_MESSAGE);
        cardLayoutContenidoTurnos.show(panelContenidoTurnos, "inicioTurnos");
        registrarRuta(ESTA_RUTA);

        actualizarTurnosHoy.actualizarTurnosHoy();

        if (btnBuscarPorFecha.isSelected()) {
            botonBuscarPorFecha();
            return;
        }

        if (estadoBuscado != null) {
            botonesBuscarPorEstado(estadoBuscado);
            return;
        }

        modeloTabla.actualizar(logicaTurno.traerTodos());
    }

    @Override
    public void eventoCancelar() {
        cardLayoutContenidoTurnos.show(panelContenidoTurnos, "inicioTurnos");
        registrarRuta(ESTA_RUTA);
    }

    @Override
    public void eventoBuscarPaciente() {

        //List<Paciente> listaPacientes = logicaPaciente.traerTodos();
        //JDialog necesita el JFrame principal. 
        //A diferencia de getParent(), que sube solo un nivel, este método recorre toda la estructura jerárquica de la GUI hasta dar con el contenedor de nivel superior*/
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        DialogBuscarPacienteTurno dialogBuscarPaciente = new DialogBuscarPacienteTurno(frame, logicaPaciente);
        dialogBuscarPaciente.setLocationRelativeTo(null);
        dialogBuscarPaciente.setVisible(true);

        panelCrearReprogramarTurno.setPacienteTurno(dialogBuscarPaciente.getPacienteSeleccionado());
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}

class ModeloTablaTurno extends AbstractTableModel {

    private List<Turno> listaTurnos = new ArrayList();
    private String[] nombreColumnas = {"ID", "Fecha", "Hora Inicial", "Hora Final", "Tipo de Consulta", "ID Paciente", "Nombre y Apellido", "Estado", "Reprogramado"};

    public void actualizar(List<Turno> listaTurnos) {
        this.listaTurnos = listaTurnos;
        fireTableDataChanged();
    }

    public Turno traerTurno(int fila) {
        return listaTurnos.get(fila);
    }

    @Override
    public int getRowCount() {
        return listaTurnos.size();
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
        Turno turno = listaTurnos.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return turno.getIdTurno();

            case 1:
                SimpleDateFormat formatoFecha = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                String fechaHoyFormato = formatoFecha.format(turno.getHoraInicio());

                String[] palabras = fechaHoyFormato.split(" ");

                String textoFechaMayusculas = "";
                for (int i = 0; i < palabras.length; i++) {
                    textoFechaMayusculas += palabras[i].toUpperCase().charAt(0) + palabras[i].substring(1, palabras[i].length()) + " ";
                }

                return textoFechaMayusculas;

            case 2:
                SimpleDateFormat formatoHoraInicial = new SimpleDateFormat("HH:mm");
                return formatoHoraInicial.format(turno.getHoraInicio()) + " hs";

            case 3:
                SimpleDateFormat formatoHoraFinal = new SimpleDateFormat("HH:mm");
                return formatoHoraFinal.format(turno.getHoraFinal()) + " hs";

            case 4:
                return turno.getTipoConsulta().getNombreConsulta();

            case 5:
                return turno.getPaciente().getIdPaciente();

            case 6:
                return turno.getPaciente().getNombre() + " " + turno.getPaciente().getApellido();

            case 7:
                return turno.getEstado();

            case 8:
                String reprogramado = (turno.isReprogramado()) ? "Sí" : "No";
                return reprogramado;

            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
