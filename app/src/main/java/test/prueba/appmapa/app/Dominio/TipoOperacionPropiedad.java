package test.prueba.appmapa.app.Dominio;

/**
 * Created by gbravo on 8/1/14.
 */
public enum TipoOperacionPropiedad {
    VentaCasa("MLC5628"),
    ArriendoCasa("MLC6406"),
    ArriendoTemporadaCasa("MLC116364"),
    VentaDepartamento("MLC1480"),
    ArriendoDepartamento("MLC6407"),
    ArriendoTemporadaDepartamento("MLC116367");

    private String id = "";
    TipoOperacionPropiedad(String id){
        this.id = id;
    }

    public String getID()
    {
        return id;
    }
}
