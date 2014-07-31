package test.prueba.appmapa.app.Dominio;

import test.prueba.appmapa.app.Model.DataSpinnerModel;
import test.prueba.appmapa.app.Model.OperacionFiltroModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbravo on 7/7/14.
 */
public class Filtros implements Serializable {

    public Filtros() {
        OperacionFiltroModel operacionVenta = new OperacionFiltroModel();
        operacionVenta.TipoOperacion = TipoOperacion.Venta;
        operacionVenta.TipoMoneda = TipoMoneda.UF;

        OperacionFiltroModel operacionArriendo = new OperacionFiltroModel();
        operacionArriendo.TipoOperacion = TipoOperacion.Arriendo;
        operacionArriendo.TipoMoneda = TipoMoneda.Pesos;

        OperacionFiltroModel operacionArriendoTemp = new OperacionFiltroModel();
        operacionArriendoTemp.TipoOperacion = TipoOperacion.Arriendo_Temporada;
        operacionArriendoTemp.TipoMoneda = TipoMoneda.Pesos;

        this.Operaciones = new ArrayList<OperacionFiltroModel>();
        this.Operaciones.add(operacionVenta);
        this.Operaciones.add(operacionArriendo);
        this.Operaciones.add(operacionArriendoTemp);
    }

    private String localidadReferencia;
    public String getLocalidadReferencia() {
        return localidadReferencia;
    }
    public void setLocalidadReferencia(String localidadReferencia) {
        this.localidadReferencia = localidadReferencia;
    }

    private TipoOperacion tipoOperacion;

    public TipoOperacion getTipoOperacion() { return tipoOperacion; }

    public void setTipoOperacion(TipoOperacion tipoOperacion) { this.tipoOperacion = tipoOperacion; }

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

    private DataSpinnerModel precioDesde;
    public DataSpinnerModel getPrecioDesde() { return precioDesde; }
    public void setPrecioDesde(DataSpinnerModel precioDesde) { this.precioDesde = precioDesde; }

    private DataSpinnerModel precioHasta;
    public DataSpinnerModel getPrecioHasta() { return precioHasta; }
    public void setPrecioHasta(DataSpinnerModel precioHasta) { this.precioHasta = precioHasta; }

    private List<OperacionFiltroModel> Operaciones;

    public List<OperacionFiltroModel> getOperaciones() { return Operaciones; }

    public OperacionFiltroModel getOperacionPorTipo(TipoOperacion tipoOperacion) {

        for (int i = 0; i < this.getOperaciones().size(); i++) {
            if(this.getOperaciones().get(i).TipoOperacion.equals(tipoOperacion))
            {
                return this.getOperaciones().get(i);
            }
        }
        return null;
    }


}
