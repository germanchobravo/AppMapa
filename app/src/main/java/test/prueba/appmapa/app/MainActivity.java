package test.prueba.appmapa.app;

import android.app.Activity;
import android.content.Context;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnItemClickListener {

    static final  LatLng santiago = new LatLng(-33.796923, -70.922433);

    private GoogleMap map;
    private MarkerOptions markerOptions;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        map.addMarker(new MarkerOptions().position(santiago)
                .title("Santiago"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(santiago,10));

        //final EditText txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        Button btnBuscar = (Button) findViewById(R.id.btnBuscar);

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.txtAutoComplete);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1));
        autoCompView.setOnItemClickListener(this);

    }

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBuWERTOvSMXXiUggUVh6ZRMJ3jQghBybc";

    private ArrayList<Localidad> autocomplete(String input) {
        ArrayList<Localidad> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
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
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Localidad>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Localidad ug = new Localidad();

                ug.SetId(predsJsonArray.getJSONObject(i).getString("id"));
                ug.SetDescripcion(predsJsonArray.getJSONObject(i).getString("description"));
                ug.SetReferencia(predsJsonArray.getJSONObject(i).getString("reference"));
                resultList.add(ug);

            }

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
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
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

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Localidad localidad = (Localidad) adapterView.getItemAtPosition(position);


        DownloadTask detaisTask = new DownloadTask();

        // Getting url to the Google Places details api
        String url = localidad.GetReferencia();

        // Start downloading Google Place Details
        // This causes to execute doInBackground() of DownloadTask class
        detaisTask.execute(url);

    }


    /** A method to download json data from url */
    private String downloadUrl(String referencia) throws IOException {
        String data = "";

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
                sb.append("?sensor=false&key=" + API_KEY);
                sb.append("&reference=" + referencia);

                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
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

                Double lat = (Double)jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
                Double lng = (Double)jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");


                map.clear();

                latLng = new LatLng(lat, lng);



                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                //markerOptions.title(addresText);

                map.addMarker(markerOptions);

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
