package test.prueba.appmapa.app.Dominio;

/**
 * Created by gbravo on 7/28/14.
 */
public class LocalidadDetalle {
    private PuntoGeografico puntoGeografico;

    private String formattedAddress;


    public PuntoGeografico getPuntoGeografico() { return puntoGeografico; }
    public void setPuntoGeografico(PuntoGeografico puntoGeografico) { this.puntoGeografico = puntoGeografico; }

    public String getFormattedAddress() { return formattedAddress; }

    public void setFormattedAddress(String formattedAddress) { this.formattedAddress = formattedAddress; }
}
