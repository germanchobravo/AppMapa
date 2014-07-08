package test.prueba.appmapa.app.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Filtros;
import test.prueba.appmapa.app.Dominio.Localidad;
import test.prueba.appmapa.app.Parsers.LocalidadJSONParser;
import test.prueba.appmapa.app.R;
import test.prueba.appmapa.app.Utiles.DownLoader;

import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by gbravo on 7/7/14.
 */
public class FiltrosActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String API_MELI_BASE = "https://mobile.mercadolibre.com.ar/sites/MLC/search?category=MLC1480" +
            "&state=TUxDUE1FVEExM2JlYg&city=TUxDQ1NBTjk4M2M";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBuWERTOvSMXXiUggUVh6ZRMJ3jQghBybc";
    Filtros filtros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filtros);

        ActionBar actionBar = getActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowCustomEnabled(true);

        View view = View.inflate(getApplicationContext(), R.layout.actionbar_filtros,
                null);
        actionBar.setCustomView(view);


        AutoCompleteTextView autoCompView = (AutoCompleteTextView)view.findViewById(R.id.action_filtros_autocomplete);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(getBaseContext(), android.R.layout.simple_list_item_1));
        autoCompView.setOnItemClickListener(this);

        Intent intent = getIntent();
        filtros = (Filtros) intent.getSerializableExtra("filtros");

    }

    private ArrayList<Localidad> autocomplete(String input) {
        ArrayList<Localidad> resultList = null;

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            sb.append("&components=country:cl");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            String data = DownLoader.DownloadUrl(sb.toString());

            JSONObject jsonObj = new JSONObject(data.toString());
            resultList = LocalidadJSONParser.parse(jsonObj);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Localidad localidad = (Localidad)adapterView.getItemAtPosition(position);

        filtros.setLocalidadReferencia(localidad.GetReferencia());

        /*
        IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {
            @Override
            public void onTaskComplete(JSONObject jsonObj) {
                try {
                    ejecutarLocalidadDetalle(jsonObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };

        DownLoaderTask detaisTask = new DownLoaderTask(delegado);

        String reference = localidad.GetReferencia();

        StringBuilder url = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
        url.append("?sensor=false&key=" + API_KEY);
        url.append("&reference=" + reference);

        detaisTask.execute(url.toString());

        */

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

            return resultList.get(index);
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
        inflater.inflate(R.menu.menu_filtros,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_aceptar:

                Intent returnIntent = new Intent();
                returnIntent.putExtra("filtros", filtros);

                setResult(RESULT_OK, returnIntent);
                finish();

                return true;
            case R.id.action_cancelar:

                Toast.makeText(getBaseContext(), "cancelo", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
