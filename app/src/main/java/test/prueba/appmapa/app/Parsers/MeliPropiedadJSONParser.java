package test.prueba.appmapa.app.Parsers;

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

            propiedad.setPrecio((Number)jPropiedad.get("price"));

            propiedad.setTitle(jPropiedad.getString("title"));

            //ATRIBUTOS TIPO PROPIEDAD Y OPERACION
            JSONArray atributos = jPropiedad.getJSONArray("attributes");

            for(int i=0; i<atributos.length();i++){
                try {
                    JSONObject atributo = (JSONObject) atributos.get(i);

                    if(atributo.length() > 0) {
                        if(atributo.getString("name").equals("Inmueble")) {
                            propiedad.setTipoPropiedad(atributo.getString("value_name"));
                        }else if(atributo.getString("name").equals("Operación"))
                        {
                            propiedad.setTipoOperacion(atributo.getString("value_name"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //GEOLOCALIZACION
            if(!jPropiedad.getJSONObject("location").get("latitude").equals("")
                    && !jPropiedad.getJSONObject("location").get("longitude").equals("")) {
                propiedad.setLat((Double) jPropiedad.getJSONObject("location").get("latitude"));
                propiedad.setLng((Double) jPropiedad.getJSONObject("location").get("longitude"));
            }

            propiedad.setUrlImagen(jPropiedad.getString("gallery_picture"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return propiedad;
    }

}
