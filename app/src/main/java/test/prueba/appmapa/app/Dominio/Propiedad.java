package test.prueba.appmapa.app.Dominio;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by gbravo on 6/23/14.
 */
public class Propiedad {

    private String id;
    private String title;
    private Double lat;
    private Double lng;
    private Bitmap imagen;
    private String urlImagen;
    private Drawable drawableImagen;

    public String getTitle() {return title; }
    public String getId() { return id; }
    public Double getLat() {return this.lat; }
    public Double getLng() {return this.lng; }
    public Bitmap getImagen() {return this.imagen; }

    public String getUrlImagen() {
        return urlImagen;
    }

    public Drawable getDrawableImagen() {
        return drawableImagen;
    }

    public void setDrawableImagen(Drawable drawableImagen) {
        this.drawableImagen = drawableImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
    public void setTitle(String title) { this.title = title; }
    public void setId(String id) { this.id = id; }

    public void setImagen(Bitmap imagen) { this.imagen = imagen; }
}
