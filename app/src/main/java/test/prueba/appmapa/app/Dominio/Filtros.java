package test.prueba.appmapa.app.Dominio;

import java.io.Serializable;

/**
 * Created by gbravo on 7/7/14.
 */
public class Filtros implements Serializable {

    private String localidadReferencia;

    public String getLocalidadReferencia() {
        return localidadReferencia;
    }

    public void setLocalidadReferencia(String localidadReferencia) {
        this.localidadReferencia = localidadReferencia;
    }
}
