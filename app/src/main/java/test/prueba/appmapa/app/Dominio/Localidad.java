package test.prueba.appmapa.app.Dominio;

/**
 * Created by gbravo on 6/21/14.
 */
public class Localidad {

    private String id;
    private String descripcion;
    private String referencia;

    public String GetId() { return this.id; }
    public String GetReferencia() {return this.referencia; }
    public String GetDescripcion() {return  this.descripcion; }

    public void SetId(String id) { this.id = id; }

    public void SetReferencia(String referencia) { this.referencia = referencia; }

    public void SetDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return this.GetDescripcion();
    }
}
