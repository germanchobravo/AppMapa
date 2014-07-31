package test.prueba.appmapa.app.Model;

import test.prueba.appmapa.app.Dominio.TipoMoneda;
import test.prueba.appmapa.app.Dominio.TipoOperacion;

import java.io.Serializable;

/**
 * Created by gbravo on 7/30/14.
 */
public class OperacionFiltroModel implements Serializable{
    public TipoOperacion TipoOperacion;
    public TipoMoneda TipoMoneda;
    public DataSpinnerModel PrecioDesde;
    public DataSpinnerModel PrecioHasta;
}
