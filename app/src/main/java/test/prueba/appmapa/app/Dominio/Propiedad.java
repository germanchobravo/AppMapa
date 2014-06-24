package test.prueba.appmapa.app.Dominio;

/**
 * Created by gbravo on 6/23/14.
 */
public class Propiedad {

    private String id;
    private String title;
    private Double lat;
    private Double lng;

    public String getTitle() {return title; }
    public String getId() { return id; }
    public Double getLat() {return this.lat; }
    public Double getLng() {return this.lng; }

    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
    public void setTitle(String title) { this.title = title; }
    public void setId(String id) { this.id = id; }
}
