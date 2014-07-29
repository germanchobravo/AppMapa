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

    private Integer tipoOperacion;

    public Integer getTipoOperacion() { return tipoOperacion; }

    public void setTipoOperacion(Integer tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    private String[] tiposPropiedad;
    public String[] getTiposPropiedad() { return tiposPropiedad; }
    public void setTiposPropiedad(String[] tiposPropiedad) { this.tiposPropiedad = tiposPropiedad; }

    private int dormitorios;
    public int getDormitorios() { return dormitorios; }
    public void setDormitorios(int dormitorios) { this.dormitorios = dormitorios; }

    private int banos;
    public int getBanos() { return banos; }
    public void setBanos(int banos) { this.banos = banos; }

    private TipoMoneda tipoMoneda;

    public TipoMoneda getTipoMoneda() { return tipoMoneda; }

    public void setTipoMoneda(TipoMoneda tipoMoneda) { this.tipoMoneda = tipoMoneda; }
}
