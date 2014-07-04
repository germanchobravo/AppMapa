package test.prueba.appmapa.app.Parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Localidad;

import java.util.ArrayList;

/**
 * Created by gbravo on 6/21/14.
 */
public class LocalidadJSONParser {

    /** Receives a JSONObject and returns a list */
    public static ArrayList<Localidad> parse(JSONObject jObject){

        JSONArray jLocalidad = null;
        try {

            /** Retrieves all the elements in the 'places' array */
            jLocalidad = jObject.getJSONArray("predictions");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jLocalidad);
    }


    private static ArrayList<Localidad> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        ArrayList<Localidad> placesList = new ArrayList<Localidad>();

        Localidad place = null;

               /** Taking each place, parses and adds to list object */
        for(int i=0; i<placesCount;i++){
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject)jPlaces.get(i));
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    /** Parsing the Place JSON object */
    private static Localidad getPlace(JSONObject jPlace){

        Localidad localidad = new Localidad();

        try {

            localidad.SetId(jPlace.getString("id"));
            localidad.SetDescripcion(jPlace.getString("description"));
            localidad.SetReferencia(jPlace.getString("reference"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localidad;
    }

}
