package test.prueba.appmapa.app.Dominio;

import test.prueba.appmapa.app.Model.DataSpinnerModel;
import test.prueba.appmapa.app.Model.OperacionFiltroModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gbravo on 7/7/14.
 */
public class Filtros implements Serializable {

    private DataSpinnerModel precioDesde = new DataSpinnerModel(0,"Desde",0);
    private DataSpinnerModel precioHasta = new DataSpinnerModel(0,"Hasta",0);
    public Filtros() {

        OperacionFiltroModel operacionVenta = new OperacionFiltroModel();
        operacionVenta.TipoOperacion = TipoOperacion.Venta;
        operacionVenta.TipoMoneda = TipoMoneda.Pesos;
        operacionVenta.PrecioDesde = precioDesde;
        operacionVenta.PrecioHasta = precioHasta;

        OperacionFiltroModel operacionArriendo = new OperacionFiltroModel();
        operacionArriendo.TipoOperacion = TipoOperacion.Arriendo;
        operacionArriendo.TipoMoneda = TipoMoneda.Pesos;
        operacionArriendo.PrecioDesde = precioDesde;
        operacionArriendo.PrecioHasta = precioHasta;

        OperacionFiltroModel operacionArriendoTemp = new OperacionFiltroModel();
        operacionArriendoTemp.TipoOperacion = TipoOperacion.Arriendo_Temporada;
        operacionArriendoTemp.TipoMoneda = TipoMoneda.Pesos;
        operacionArriendoTemp.PrecioDesde = precioDesde;
        operacionArriendoTemp.PrecioHasta = precioHasta;

        this.Operaciones = new ArrayList<OperacionFiltroModel>();
        this.Operaciones.add(operacionVenta);
        this.Operaciones.add(operacionArriendo);
        this.Operaciones.add(operacionArriendoTemp);

        this.setOperacion(operacionVenta);
    }

    private String localidadReferencia;
    public String getLocalidadReferencia() {
        return localidadReferencia;
    }
    public void setLocalidadReferencia(String localidadReferencia) {
        this.localidadReferencia = localidadReferencia;
    }

    private OperacionFiltroModel Operacion;
    public OperacionFiltroModel getOperacion() { return Operacion; }
    public void setOperacion(OperacionFiltroModel operacion) { Operacion = operacion; }

    public String getIDCategory()
    {
        String idCategory = "";
        if(this.tiposPropiedad != null) {
            if (Arrays.asList(this.tiposPropiedad).contains("Departamento")) {
                switch (this.getOperacion().TipoOperacion) {
                    case Venta:
                        idCategory = "category=" + TipoOperacionPropiedad.VentaDepartamento.getID();
                        break;
                    case Arriendo:
                        idCategory = "category=" + TipoOperacionPropiedad.ArriendoDepartamento.getID();
                        break;
                    case Arriendo_Temporada:
                        idCategory = "category=" + TipoOperacionPropiedad.ArriendoTemporadaDepartamento.getID();
                }
            }

            if (Arrays.asList(this.tiposPropiedad).contains("Casa")) {
                if (idCategory.length() > 0) {
                    idCategory += "&";
                }
                switch (this.getOperacion().TipoOperacion) {
                    case Venta:
                        idCategory += "category=" + TipoOperacionPropiedad.VentaCasa.getID();
                        break;
                    case Arriendo:
                        idCategory += "category=" + TipoOperacionPropiedad.ArriendoCasa.getID();
                        break;
                    case Arriendo_Temporada:
                        idCategory += "category=" + TipoOperacionPropiedad.ArriendoTemporadaDepartamento.getID();
                        break;
                }
            }
        }else
        {
            //SOLO CONSIDERA DEPARTAMENTO
            switch (this.getOperacion().TipoOperacion) {
                case Venta:
                    idCategory = "&category=" + TipoOperacionPropiedad.VentaDepartamento.getID();
                    break;
                case Arriendo:
                    idCategory = "category=" + TipoOperacionPropiedad.ArriendoDepartamento.getID();
                    break;
                case Arriendo_Temporada:
                    idCategory = "category=" + TipoOperacionPropiedad.ArriendoTemporadaDepartamento.getID();
                    break;
            }
        }

        return idCategory;
    }

    private String[] tiposPropiedad;
    public String[] getTiposPropiedad() { return tiposPropiedad; }
    public void setTiposPropiedad(String[] tiposPropiedad) { this.tiposPropiedad = tiposPropiedad; }

    private int dormitorios;
    public int getDormitorios() { return dormitorios; }
    public void setDormitorios(int dormitorios) { this.dormitorios = dormitorios; }

    private int banos;
    public int getBanos() { return banos; }
    public void setBanos(int banos) { this.banos = banos; }

    private DataSpinnerModel superficieDesde;
    public DataSpinnerModel getSuperficieDesde() { return superficieDesde; }
    public void setSuperficieDesde(DataSpinnerModel superficieDesde) { this.superficieDesde = superficieDesde; }

    private DataSpinnerModel superficieHasta;
    public DataSpinnerModel getSuperficieHasta() { return superficieHasta; }
    public void setSuperficieHasta(DataSpinnerModel superficieHasta) { this.superficieHasta = superficieHasta; }

    private Boolean soloAmoblado;
    public Boolean getSoloAmoblado() { return soloAmoblado; }
    public void setSoloAmoblado(Boolean soloAmoblado) { this.soloAmoblado = soloAmoblado; }

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

    public void LimpiarFiltros()
    {
        for (int i = 0; i < this.getOperaciones().size(); i++) {
            this.getOperaciones().get(i).TipoMoneda = TipoMoneda.Pesos;
            this.getOperaciones().get(i).PrecioDesde = precioDesde;
            this.getOperaciones().get(i).PrecioHasta = precioHasta;
        }

        this.setOperacion(getOperacionPorTipo(TipoOperacion.Venta));

        this.banos = 0;
        this.dormitorios = 0;
        this.setOperacion(null);
        this.superficieDesde = null;
        this.superficieHasta = null;
        this.tiposPropiedad = null;
    }
}
