package test.prueba.appmapa.app;

import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.PuntoGeografico;

/**
 * Created by gbravo on 6/22/14.
 */
public class LocalidadDetalleJSONParser {

public static PuntoGeografico parse(JSONObject jsonObject)
{
    return getPunto(jsonObject);
}

    /** Parsing the Place JSON object */
    private static PuntoGeografico getPunto(JSONObject jsonObj){

        PuntoGeografico pg = new PuntoGeografico();

        try {

            pg.SetLat((Double)jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat"));
            pg.SetLng((Double)jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pg;
    }
}
