package test.prueba.appmapa.app;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by gbravo on 6/21/14.
 */
public class GeoCoderTaskActivity extends Activity {

    static final LatLng santiago = new LatLng(-33.796923, -70.922433);

    private GoogleMap map;
    private MarkerOptions markerOptions;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geocoder);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        map.addMarker(new MarkerOptions().position(santiago)
                .title("Santiago"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(santiago, 10));

        final EditText txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        Button btnBuscar = (Button) findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = txtDireccion.getText().toString();
                if(location == null || location.equals(""))
                {
                    Toast.makeText(getBaseContext(), "ingrese direccion", Toast.LENGTH_SHORT).show();

                }else
                {
                    new GeoCoderTask().execute(location);
                }

            }
        });
    }

    private class GeoCoderTask extends AsyncTask<String, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(String... locationName) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try{
                addresses = geocoder.getFromLocationName(locationName[0], 3);

            }catch (IOException e)
            {
                e.printStackTrace();
            }

            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if(addresses == null || addresses.size() == 0)
            {
                Toast.makeText(getBaseContext(), "Direccion no encontrada",Toast.LENGTH_LONG).show();
            }

            map.clear();

            for (int i = 0; i < addresses.size(); i++)
            {
                Address address = addresses.get(i);

                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addresText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addresText);

                map.addMarker(markerOptions);

                if(i == 0)
                {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }

            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inferior,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_lista:
                Toast.makeText(this,"aca va el filtro", Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
