package igu.clases_utilitarias;

import igu.exceptions.DiferenciaCantidadComponentes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

//Clase utilitaria
public final class FabricaElementos {

    private FabricaElementos() {
    }

    public static JPanel crearPanelFormulario(JLabel[] labels, JComponent[] componentesTexto) {

        //GridBagConstrains de los paneles con los campos
        GridBagConstraints gridBagConstrains = new GridBagConstraints();
        gridBagConstrains.insets = new Insets(0, 20, 0, 20);
        gridBagConstrains.anchor = GridBagConstraints.WEST;
        gridBagConstrains.weighty = 1;
        gridBagConstrains.gridx = 0; //Se mantiene siempre igual

        //GridBagConstrains de los componentes dentro de los paneles de los campos
        GridBagConstraints gridBagConstrainsDentroPaneles = new GridBagConstraints();
        gridBagConstrainsDentroPaneles.insets = new Insets(0, 0, 20, 0);
        gridBagConstrainsDentroPaneles.anchor = GridBagConstraints.WEST;
        gridBagConstrainsDentroPaneles.gridx = 0; //Se mantiene siempre igual

        //Armar formulario
        if (labels.length != componentesTexto.length) {
            throw new DiferenciaCantidadComponentes("La cantidad de labels y componentes de texto ingresados no es la misma.");
        }

        JPanel panelFormulario = new JPanel();
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setLayout(new GridBagLayout());

        for (int i = 0; i < labels.length; i++) {

            JPanel panelCampo = new JPanel();
            panelCampo.setLayout(new GridBagLayout());
            gridBagConstrainsDentroPaneles.gridy = 0;
            panelCampo.add(labels[i], gridBagConstrainsDentroPaneles);
            gridBagConstrainsDentroPaneles.gridy = 1;
            panelCampo.add(componentesTexto[i], gridBagConstrainsDentroPaneles);

            gridBagConstrains.gridy = i + 1;
            panelFormulario.add(panelCampo, gridBagConstrains);
        }

        return panelFormulario;
    }

    public static JPanel crearPanelFormularioConEncabezado(JLabel[] labels, JComponent[] componentesTexto, JLabel lblEncabezado) {

        //Panel que contendrá el encabezado y formulario
        JPanel panelFormularioConEncabezado = new JPanel();
        panelFormularioConEncabezado.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panelFormularioConEncabezado.setLayout(new BorderLayout());
        panelFormularioConEncabezado.setBackground(Color.WHITE);

        //Agregar encabezado
        panelFormularioConEncabezado.add(crearPanelConEncabezado(lblEncabezado), BorderLayout.NORTH);

        //Panel de solo el formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setLayout(new GridBagLayout());

        //GridBagConstrains de los paneles con los campos
        GridBagConstraints gridBagConstrains = new GridBagConstraints();
        gridBagConstrains.insets = new Insets(0, 20, 0, 20);
        gridBagConstrains.weighty = 1;
        gridBagConstrains.gridx = 0; //Se mantiene siempre igual        

        //GridBagConstrains de los componentes dentro de los paneles de los campos
        GridBagConstraints gridBagConstrainsDentroPaneles = new GridBagConstraints();
        gridBagConstrainsDentroPaneles.insets = new Insets(0, 0, 20, 0);
        gridBagConstrainsDentroPaneles.anchor = GridBagConstraints.WEST;
        gridBagConstrainsDentroPaneles.gridx = 0; //Se mantiene siempre igual

        //Armar encabezado
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setLayout(new GridBagLayout());
        panelEncabezado.setBackground(Color.WHITE);
        gridBagConstrainsDentroPaneles.gridy = 0;
        panelEncabezado.add(lblEncabezado, gridBagConstrainsDentroPaneles);
        JSeparator separador = new JSeparator(JSeparator.HORIZONTAL);
        separador.setPreferredSize(new Dimension(480, 1));
        gridBagConstrainsDentroPaneles.gridy = 1;
        panelEncabezado.add(separador, gridBagConstrainsDentroPaneles);
        panelFormularioConEncabezado.add(panelEncabezado, BorderLayout.NORTH);

        //Armar formulario
        if (labels.length != componentesTexto.length) {
            throw new DiferenciaCantidadComponentes("La cantidad de labels y componentes de texto ingresados no es la misma.");
        }

        for (int i = 0; i < labels.length; i++) {

            JPanel panelCampo = new JPanel();
            panelCampo.setLayout(new GridBagLayout());
            panelCampo.setBackground(Color.WHITE);

            gridBagConstrainsDentroPaneles.gridy = 0;
            panelCampo.add(labels[i], gridBagConstrainsDentroPaneles);
            gridBagConstrainsDentroPaneles.gridy = 1;
            panelCampo.add(componentesTexto[i], gridBagConstrainsDentroPaneles);

            gridBagConstrains.gridy = i + 1;
            panelFormulario.add(panelCampo, gridBagConstrains);
        }

        //Agregar formulario
        panelFormularioConEncabezado.add(panelFormulario, BorderLayout.CENTER);

        return panelFormularioConEncabezado;
    }

    //Devuelve un panel entero con BorderLayout pero solo con el encabezado, el resto vacío
    public static JPanel crearPanelConEncabezado(JLabel lblEncabezado) {
        //Panel entero
        JPanel panelEntero = new JPanel();
        panelEntero.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panelEntero.setLayout(new BorderLayout());
        panelEntero.setBackground(Color.WHITE);

        //Parte del encabezado
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setLayout(new GridBagLayout());
        panelEncabezado.setBackground(Color.WHITE);
        GridBagConstraints gridBagConstrains = new GridBagConstraints();
        gridBagConstrains.insets = new Insets(0, 0, 20, 0);
        gridBagConstrains.anchor = GridBagConstraints.WEST;
        gridBagConstrains.weightx = 1;
        gridBagConstrains.gridx = 0;
        gridBagConstrains.gridy = 0;
        panelEncabezado.add(lblEncabezado, gridBagConstrains);

        JSeparator separador = new JSeparator(JSeparator.HORIZONTAL);
        separador.setPreferredSize(new Dimension(480, 1));
        gridBagConstrains.gridy = 1;
        panelEncabezado.add(separador, gridBagConstrains);

        panelEntero.add(panelEncabezado, BorderLayout.NORTH);

        return panelEntero;
    }

    public static JTable crearTabla(AbstractTableModel modeloTabla) {

        JTable tabla = new JTable(modeloTabla);
        tabla.getTableHeader().setFont(new Font("Roboto SemiCondensed Medium", Font.BOLD, 20));
        tabla.setFont(new Font("Roboto SemiCondensed Medium", Font.PLAIN, 16));
        tabla.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tabla.setBackground(new Color(255, 243, 188));
        tabla.setRowHeight(30);

        return tabla;
    }

    public static JPanel crearPanelBotonesParaTabla(JButton[] botones) {

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(botones.length, 1));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));

        Font fuenteBotones = new Font("Roboto SemiCondensed Medium", Font.BOLD, 18);
        for (JButton boton : botones) {
            boton.setFont(fuenteBotones);
            boton.setForeground(Color.BLACK);
            boton.setBackground(Color.LIGHT_GRAY);
            boton.setFocusPainted(false);
            boton.setMargin(new Insets(8, 8, 8, 8));

            panelBotones.add(boton);
        }

        return panelBotones;
    }

    public static JPanel crearPanelBotonesParaCrearEditar(JButton[] botones) {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstrains = new GridBagConstraints();
        gridBagConstrains.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstrains.gridx = 0;

        Font fuenteBotones = new Font("Roboto SemiCondensed Medium", Font.BOLD, 18);
        Color colorFondo = new Color(255, 243, 188);
        int iteradorBotonYPosicion = 0;
        for (JButton boton : botones) {
            boton.setFont(fuenteBotones);
            boton.setForeground(Color.BLACK);
            boton.setBackground(colorFondo);
            boton.setFocusPainted(false);
            boton.setMargin(new Insets(40, 50, 40, 50));

            gridBagConstrains.gridy = iteradorBotonYPosicion;
            panelBotones.add(botones[iteradorBotonYPosicion], gridBagConstrains);
            iteradorBotonYPosicion++;
        }

        return panelBotones;
    }
}
