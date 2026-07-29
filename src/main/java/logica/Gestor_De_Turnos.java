package logica;

import igu.VentanaPrincipal;

// Autor: Maximiliano Delsouc
// GitHub: https://github.com/MaximilianoDelsouc
// Proyecto bajo licencia MIT
public class Gestor_De_Turnos {

    public static void main(String[] args) {

        ControladoraLogica controladoraLogica = new ControladoraLogica();

        LogicaPaciente logicaPaciente = new LogicaPaciente(controladoraLogica);
        LogicaTipoConsulta logicaTipoConsulta = new LogicaTipoConsulta(controladoraLogica);
        LogicaTurno logicaTurno = new LogicaTurno(controladoraLogica);

        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(logicaPaciente, logicaTipoConsulta, logicaTurno);
        ventanaPrincipal.setVisible(true);
        ventanaPrincipal.setLocationRelativeTo(null);
    }
}
