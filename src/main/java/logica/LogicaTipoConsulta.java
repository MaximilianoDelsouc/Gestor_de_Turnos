package logica;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import logica.clases.TipoConsulta;
import logica.clases.Turno;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.ProblemaPersistencia;

public class LogicaTipoConsulta {

    private final ControladoraLogica controladoraLogica;

    public LogicaTipoConsulta(ControladoraLogica controladoraLogica) {
        this.controladoraLogica = controladoraLogica;
    }

    /*
    CREATE
     */
    public void crearNuevo(String nombreConsulta, int duracionMinutos, int costo) {
        if (!comprobarUnicidadNombre(nombreConsulta)) {
            throw new CampoInvalido("El nombre para el nuevo tipo de consulta ya existe en el sistema. Este debe ser único.");
        }

        TipoConsulta tipoConsulta = new TipoConsulta(nombreConsulta, duracionMinutos, costo);
        controladoraLogica.crearTipoConsulta(tipoConsulta);
    }

    /*
    READ
     */
    public List<TipoConsulta> traerTodos() {
        return controladoraLogica.traerTiposConsulta().stream()
                .filter(tipoConsulta -> tipoConsulta.isHabilitado())
                .toList();
    }

    public TipoConsulta traerTipoConsulta(long idTipoConsulta) {
        return controladoraLogica.traerTipoConsulta(idTipoConsulta);
    }

    public List<TipoConsulta> ordenarAlfabeticamente(List<TipoConsulta> todasConsultas) {
        Collator collator = Collator.getInstance(new Locale("es", "ES"));
        collator.setStrength(Collator.PRIMARY); // Ignora mayúsculas, minúsculas y acentos.

        return todasConsultas.stream()
                .sorted(Comparator.comparing(TipoConsulta::getNombreConsulta, collator))
                .toList();
    }

    public List<TipoConsulta> traerTodosDeshabilitados() {
        return controladoraLogica.traerTiposConsulta().stream()
                .filter(tipoConsulta -> !tipoConsulta.isHabilitado())
                .toList();
    }

    /*
    UPDATE
     */
    public void editarDatos(TipoConsulta tipoConsulta, String nombreConsulta, int duracionMinutos, int costo) {
        if (!comprobarUnicidadNombre(nombreConsulta)) {
            throw new CampoInvalido("El nombre para el nuevo tipo de consulta ya existe en el sistema. Este debe ser único.");
        }

        tipoConsulta.setNombreConsulta(nombreConsulta);
        tipoConsulta.setDuracionMinutos(duracionMinutos);
        tipoConsulta.setCosto(costo);

        try {
            controladoraLogica.editarTipoConsulta(tipoConsulta);
        } catch (Exception e) {
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar editar un tipo de consulta.");
        }
    }

    private boolean comprobarUnicidadNombre(String nombre) {
        String nombreMinusculas = nombre.toLowerCase();

        return !controladoraLogica.traerTiposConsulta().stream()
                .anyMatch(tipoConsulta -> tipoConsulta.getNombreConsulta().toLowerCase().equals(nombreMinusculas));
    }

    public void habilitar(TipoConsulta tipoConsulta) {
        tipoConsulta.setHabilitado(true);

        try {
            controladoraLogica.editarTipoConsulta(tipoConsulta);
        } catch (Exception e) {
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar habilitar un tipo de consulta.");
        }
    }

    /*
    DELETE
     */
    public void eliminarDeshabilitar(long idTipoConsulta) {
        TipoConsulta tipoConsulta = controladoraLogica.traerTipoConsulta(idTipoConsulta);
        boolean esRegistroPadre = false;
        for (Turno turno : controladoraLogica.traerTurnos()) {
            if (turno.getTipoConsulta().equals(tipoConsulta)) {
                esRegistroPadre = true;
                break;
            }
        }

        if (esRegistroPadre) {
            tipoConsulta.setHabilitado(false);
            try {
                controladoraLogica.editarTipoConsulta(tipoConsulta);
            } catch (Exception e) {
                throw new ProblemaPersistencia("Ha ocurrido un problema al intentar deshabilitar un tipo de consulta.");
            }

        } else {
            try {
                controladoraLogica.eliminarTipoConsulta(idTipoConsulta);
            } catch (NonexistentEntityException e) {
                throw new ProblemaPersistencia("El tipo de consulta que se intenta eliminar no existe.");
            }
        }

    }
}
