package igu.interfaces;

import logica.clases.TipoConsulta;

public interface AccionesTipoConsulta {

    public void guardarNuevoTipoConsulta(String nombreConsulta, int duracionMinutos, int costo);

    public void guardarTipoConsultaEditado(TipoConsulta tipoConsultaEditar, String nombreConsulta, int duracionMinutos, int costo);

    public void eventocancelar();
}
