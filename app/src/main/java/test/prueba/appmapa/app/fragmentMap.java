package test.prueba.appmapa.app;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.*;
import test.prueba.appmapa.app.Parsers.LocalidadDetalleJSONParser;
import test.prueba.appmapa.app.Parsers.MeliPropiedadJSONParser;
import test.prueba.appmapa.app.task.DownLoaderTask;
import test.prueba.appmapa.app.task.IAsyncTaskDelegate;
import test.prueba.appmapa.app.task.ICallBackListPropiedades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbravo on 6/29/14.
 */
public class FragmentMap extends Fragment implements OnItemClickListener, ICallBackListPropiedades {



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
    MapFragment mapaFragment;

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public static FragmentMap newInstance(Bundle arguments){
        FragmentMap f = new FragmentMap();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    private Filtros filtros;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        String localidadReferencia;

        if(arguments != null)
        {

            filtros = new Filtros();
            localidadReferencia = getArguments()
                    .getString("localidadReferencia");

            filtros.setLocalidadReferencia(localidadReferencia);


        }

    }



  /*  @Override
    public void onResume() {
        super.onResume();

        map = mapaFragment.getMap();

        getCurrentLocationTask = new CurrentLocationTask();
        getCurrentLocationTask.execute();

        if(filtros != null)
        {
            RecibeLocalidadReferencia(filtros.getLocalidadReferencia());
        }
    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment)).getMap();

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                /*
                    VisibleRegion visibleRegion = map.getProjection()
                        .getVisibleRegion();

                    Point x = map.getProjection().toScreenLocation(
                                        visibleRegion.farRight);

                    Point y = map.getProjection().toScreenLocation(
                                        visibleRegion.nearLeft);

                    Point centerPoint = new Point(x.x / 2, y.y / 2);

                    LatLng centerFromPoint = map.getProjection().fromScreenLocation(
                                        centerPoint);

                 */

               // LatLng center = map.getCameraPosition().target;
               // Log.d("AppMapa", "Center From camera: Long: " + center.longitude + " Lat" + center.latitude);
            }
        });

        getCurrentLocationTask = new CurrentLocationTask();
        getCurrentLocationTask.execute();

        if(filtros != null)
        {
            RecibeLocalidadReferencia(filtros.getLocalidadReferencia());
        }

        map.setMyLocationEnabled(true);


        return view;

    }


    private void PintarPropiedades(List<Propiedad> propiedades) {


        for(int i=0; i<propiedades.size();i++){

            if(propiedades.get(i).getLat() != null && propiedades.get(i).getLng() != null) {
                latLng = new LatLng(propiedades.get(i).getLat(), propiedades.get(i).getLng());
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(propiedades.get(i).getTitle());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_propiedades));
                map.addMarker(markerOptions);
            }
        }
    }

    public void setPropiedadesEnMapa(ArrayList<Propiedad> propiedades) {


        map.clear();

        for(int i=0; i<propiedades.size();i++){

            if(propiedades.get(i).getLat() != null && propiedades.get(i).getLng() != null) {
                latLng = new LatLng(propiedades.get(i).getLat(), propiedades.get(i).getLng());
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(propiedades.get(i).getTitle());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_propiedades));
                map.addMarker(markerOptions);
            }
        }
    }

    private void pintarPropiedades(JSONObject jsonObj) {
        ArrayList<Propiedad> propiedades = MeliPropiedadJSONParser.parse(jsonObj);

        //map.clear();

        for(int i=0; i<propiedades.size();i++){

            if(propiedades.get(i).getLat() != null && propiedades.get(i).getLng() != null) {
                latLng = new LatLng(propiedades.get(i).getLat(), propiedades.get(i).getLng());
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(propiedades.get(i).getTitle());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_propiedades));
                map.addMarker(markerOptions);
            }
        }
    }

    private void ejecutarLocalidadDetalle(final JSONObject jsonObj) {

        LocalidadDetalle localidadDetalle = LocalidadDetalleJSONParser.parse(jsonObj);

        latLng = new LatLng(localidadDetalle.getPuntoGeografico().GetLat(), localidadDetalle.getPuntoGeografico().GetLng());

        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));


        IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {
            @Override
            public void onTaskComplete(JSONObject jsonObject) {

                pintarPropiedades(jsonObject);

            }

        };

        StringBuilder url = new StringBuilder(API_MELI_BASE);
        DownLoaderTask taskPropiedades = new DownLoaderTask(delegado);
        taskPropiedades.execute(url.toString());

    }

    public void RecibeLocalidadReferencia(String localidadReferencia)
    {
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

        String reference = localidadReferencia;

        StringBuilder url = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
        url.append("?sensor=false&key=" + API_KEY);
        url.append("&language=es");
        url.append("&reference=" + reference);
        detaisTask.execute(url.toString());

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Localidad localidad = (Localidad)adapterView.getItemAtPosition(position);

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

    }

    @Override
    public void onCallBackPropiedades(List<Propiedad> propiedades) {

        if(propiedades.size() > 0)
        {
            PintarPropiedades(propiedades);
        }

    }

    // Fetches data from url passed
    private class CurrentLocationTask extends AsyncTask<Void, Void, Location> {

        @Override
        protected Location doInBackground(Void... bestReading) {

            try{
                // Acquire reference to the LocationManager
                if (null == (mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE)))
                    getActivity().finish();

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
                    Toast.makeText(getActivity().getBaseContext(), "No Initial Reading Available", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



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
        if (bestAccuracy > minAccuracy || (System.currentTimeMillis() - bestAge) > maxAge) {
            return null;
        } else {
        return bestResult;
        }
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

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_donde_estoy));
        map.addMarker(markerOptions);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inferior,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
