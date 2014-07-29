package test.prueba.appmapa.app.Parsers;

import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.LocalidadDetalle;
import test.prueba.appmapa.app.Dominio.PuntoGeografico;

/**
 * Created by gbravo on 6/22/14.
 */
public class LocalidadDetalleJSONParser {

public static LocalidadDetalle parse(JSONObject jsonObject)
{
    return getPunto(jsonObject);
}

    /** Parsing the Place JSON object */
    private static LocalidadDetalle getPunto(JSONObject jsonObj){

        LocalidadDetalle localidadDetalle = new LocalidadDetalle();


        PuntoGeografico pg = new PuntoGeografico();

        try {

            localidadDetalle.setFormattedAddress((String)jsonObj.getJSONObject("result").get("formatted_address"));

            pg.SetLat((Double)jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat"));
            pg.SetLng((Double)jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng"));

            localidadDetalle.setPuntoGeografico(pg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localidadDetalle;
    }
}
