package test.prueba.appmapa.app;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        //GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

    }



}
