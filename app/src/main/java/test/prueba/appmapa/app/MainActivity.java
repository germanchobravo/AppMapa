package test.prueba.appmapa.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Propiedad;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnItemClickListener {

    //static final  LatLng santiago = new LatLng(-33.796923, -70.922433);

    private GoogleMap map;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private static final long ONE_MIN = 1000 * 60;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long MEASURE_TIME = 1000 * 30;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final float MIN_DISTANCE = 10.0f;

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String API_MELI_BASE = "https://mobile.mercadolibre.com.ar/sites/MLC/search?category=MLC1480" +
            "&state=TUxDUE1FVEExM2JlYg&city=TUxDQ1NBTjk4M2M";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBuWERTOvSMXXiUggUVh6ZRMJ3jQghBybc";
    private Location mBestReading;

    // Reference to the LocationManager and LocationListener
    private LocationManager mLocationManager;
    private CurrentLocationTask getCurrentLocationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        map.setMyLocationEnabled(true);

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.txtAutoComplete);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1));
        autoCompView.setOnItemClickListener(this);
        getCurrentLocationTask = new CurrentLocationTask();
        getCurrentLocationTask.execute();

    }

    // Fetches data from url passed
    private class CurrentLocationTask extends AsyncTask<Void, Void, Location> {

        @Override
        protected Location doInBackground(Void... bestReading) {

            try{
                // Acquire reference to the LocationManager
                if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
                    finish();

                // Get best last location measurement
                mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);



            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return mBestReading;
        }

        @Override
        protected void onPostExecute(Location bestReading) {
            super.onPostExecute(bestReading);

            try {
                // Display last reading information
                if (null != bestReading) {

                    updateDisplay(bestReading);

                } else {
                    Toast.makeText(getBaseContext(), "No Initial Reading Available", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private ArrayList<Localidad> autocomplete(String input) {
        ArrayList<Localidad> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        /*try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            sb.append("&components=country:cl");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (java.net.MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);

        } catch (java.io.IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }*/

        try {

            /*JSONObject jsonObj = new JSONObject(jsonResults.toString());
            resultList = LocalidadJSONParser.parse(jsonObj);*/

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            sb.append("&components=country:cl");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            String data = downloadUrl(sb.toString());

            JSONObject jsonObj = new JSONObject(data.toString());
            resultList = LocalidadJSONParser.parse(jsonObj);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<Localidad> implements Filterable {
        private ArrayList<Localidad> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Localidad getItem(int index) {

            return (Localidad)resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        resultList = autocomplete(constraint.toString());

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filtros,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_filtros:
                Toast.makeText(this,"aca va el filtro", Toast.LENGTH_LONG).show();

                return true;
            case R.id.action_current_location:
                getCurrentLocationTask = new CurrentLocationTask();
                getCurrentLocationTask.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Localidad localidad = (Localidad) adapterView.getItemAtPosition(position);


        DownloadTask detaisTask = new DownloadTask();

        String reference = localidad.GetReferencia();

        StringBuilder url = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
        url.append("?sensor=false&key=" + API_KEY);
        url.append("&reference=" + reference);

        detaisTask.execute(url.toString());

    }


    /** A method to download json data from url */
    private String downloadUrl(String url) throws IOException {
        String data = "";

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {

                URL _url = new URL(url.toString());
                conn = (HttpURLConnection) _url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }

                data = jsonResults.toString();

            } catch (Exception e) {
                Log.d("Exception while downloading url", e.toString());
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
            }

        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                JSONObject jsonObj = new JSONObject(result);

                PuntoGeografico punto = LocalidadDetalleJSONParser.parse(jsonObj);

                latLng = new LatLng(punto.GetLat(),punto.GetLng());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));


                ApiMeliDownloadTask meliDownloadTask = new ApiMeliDownloadTask();

                StringBuilder url = new StringBuilder(API_MELI_BASE);
                //url.append("?sensor=false&key=" + API_KEY);
                //((url.append("&reference=" + reference);

                meliDownloadTask.execute(url.toString());



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // Fetches data from url passed
    private class ApiMeliDownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObj = new JSONObject(result);

                ArrayList<Propiedad> propiedades = MeliPropiedadJSONParser.parse(jsonObj);

                map.clear();
                /** Taking each place, parses and adds to list object */
                for(int i=0; i<propiedades.size();i++){

                        latLng = new LatLng(propiedades.get(i).getLat(),propiedades.get(i).getLng());
                        markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(propiedades.get(i).getTitle());
                        map.addMarker(markerOptions);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    // Get the last known location from all providers
    // return best reading that is as accurate as minAccuracy and
    // was taken no longer then minAge milliseconds ago. If none,
    // return null.

    private Location bestLastKnownLocation(float minAccuracy, long maxAge) {

        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;

        List<String> matchingProviders = mLocationManager.getAllProviders();

        for (String provider : matchingProviders) {

            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {

                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if (accuracy < bestAccuracy) {

                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestAge = time;

                }
            }
        }

        // Return best reading or null
        /*if (bestAccuracy > minAccuracy || (System.currentTimeMillis() - bestAge) > maxAge) {
            return null;
        } else {*/
            return bestResult;
        //}
    }

    // Update display
    private void updateDisplay(Location location) {

        PuntoGeografico punto = new PuntoGeografico();

        punto.SetLng(location.getLongitude());
        punto.SetLat(location.getLatitude());

        posicionActualEnMapa(punto);
    }

    private void posicionActualEnMapa(PuntoGeografico punto)
    {
        latLng = new LatLng(punto.GetLat(),punto.GetLng());

        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps_indicator_current_position));
        map.addMarker(markerOptions);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }
}
