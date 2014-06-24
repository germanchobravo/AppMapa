package test.prueba.appmapa.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Propiedad;

import java.util.ArrayList;

/**
 * Created by gbravo on 6/23/14.
 */
public class MeliPropiedadJSONParser {

    /** Receives a JSONObject and returns a list */
    public static ArrayList<Propiedad> parse(JSONObject jObject){

        JSONArray jPropiedades = null;
        try {

            /** Retrieves all the elements in the 'places' array */
            jPropiedades = jObject.getJSONArray("results");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPropiedades(jPropiedades);
    }


    private static ArrayList<Propiedad> getPropiedades(JSONArray jPropiedades){
        int propiedadesCount = jPropiedades.length();
        ArrayList<Propiedad> ListPropiedad = new ArrayList<Propiedad>();

        Propiedad propiedad = null;

        /** Taking each place, parses and adds to list object */
        for(int i=0; i<propiedadesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                propiedad = getPropiedad((JSONObject) jPropiedades.get(i));
                ListPropiedad.add(propiedad);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ListPropiedad;
    }

    /** Parsing the Place JSON object */
    private static Propiedad getPropiedad(JSONObject jPropiedad){

        Propiedad propiedad = new Propiedad();

        try {

            propiedad.setId(jPropiedad.getString("id"));
            propiedad.setTitle(jPropiedad.getString("title"));
            propiedad.setLat((Double)jPropiedad.getJSONObject("location").get("latitude"));
            propiedad.setLng((Double)jPropiedad.getJSONObject("location").get("longitude"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return propiedad;
    }

}
