package logica;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import logica.clases.TipoConsulta;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.ProblemaPersistencia;

public class LogicaTipoConsulta {

    private ControladoraLogica controladoraLogica;

    public LogicaTipoConsulta(ControladoraLogica controladoraLogica) {
        this.controladoraLogica = controladoraLogica;
    }

    public void crearNuevo(TipoConsulta tipoConsulta) {
        try {
            verificarCampos(tipoConsulta);
        } catch (CampoInvalido e) {
            e.printStackTrace();
            throw e;
        }

        controladoraLogica.crearTipoConsulta(tipoConsulta);
    }

    public List<TipoConsulta> traerTodos() {
        return controladoraLogica.traerTiposConsulta();
    }

    public List<TipoConsulta> ordenarAlfabeticamente(List<TipoConsulta> todasConsultas) {
        Collator colador = Collator.getInstance(new Locale("es", "ES"));
        colador.setStrength(Collator.PRIMARY);

        Collections.sort(todasConsultas, new Comparator<TipoConsulta>() {
            @Override
            public int compare(TipoConsulta tipoConsulta1, TipoConsulta tipoConsulta2) {
                return colador.compare(tipoConsulta1.getNombreConsulta(), tipoConsulta2.getNombreConsulta());
            }
        });

        return todasConsultas;
    }

    public TipoConsulta traerSeleccionado(long idTipoConsulta) {
        return controladoraLogica.traerTipoConsulta(idTipoConsulta);
    }

    public void editarDatos(TipoConsulta tipoConsulta) {
        try {
            verificarCampos(tipoConsulta);
        } catch (CampoInvalido e) {
            e.printStackTrace();
            throw e;
        }

        try {
            controladoraLogica.editarTipoConsulta(tipoConsulta);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar editar un tipo de consulta.");
        }
    }

    public void eliminar(long idTipoConsulta) {
        try {
            controladoraLogica.eliminarTipoConsulta(idTipoConsulta);
        } catch (NonexistentEntityException ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar eliminar un tipo de consulta.");
        }
    }

    private void verificarCampos(TipoConsulta tipoConsulta) {
        if (tipoConsulta.getNombreConsulta() == null || tipoConsulta.getNombreConsulta().isBlank()) {
            throw new CampoInvalido("El campo 'Nombre de Tipo de consulta' no puede ser vacío.");
        }
        if (tipoConsulta.getDuracionMinutos() < 5) {
            throw new CampoInvalido("La duración de las consultas no pueden ser menor a 5 minutos."); //Verififcable
        }
        if (tipoConsulta.getCosto() < 0) {
            throw new CampoInvalido("El costo de las consultas no puede ser menor a 0.");
        }
    }
}
