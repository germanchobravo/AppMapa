package test.prueba.appmapa.app.Dominio;

/**
 * Created by gbravo on 7/10/14.
 */

public class Paginacion {

    private static final int limit = 10;

    public int Total;
    public int Offset;

    public int getLimit() {
        return limit;
    }
}
