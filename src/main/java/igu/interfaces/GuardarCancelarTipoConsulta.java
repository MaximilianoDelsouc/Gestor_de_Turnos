package igu.interfaces;

import logica.clases.TipoConsulta;

public interface GuardarCancelarTipoConsulta {

    public void eventoGuardarTipoConsultaNueva(TipoConsulta tipoConsulta);
    
    public void eventoGuardarTipoConsultaEditada(TipoConsulta tipoConsulta);

    public void eventocancelar();
}
