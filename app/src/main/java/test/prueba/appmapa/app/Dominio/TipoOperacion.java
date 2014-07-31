package test.prueba.appmapa.app.Dominio;

/**
 * Created by gbravo on 7/30/14.
 */
public enum TipoOperacion {
    Venta(1),
    Arriendo(2),
    Arriendo_Temporada(3);

    int index = 0;
    TipoOperacion(int index){
        this.index = index;
    }

}
