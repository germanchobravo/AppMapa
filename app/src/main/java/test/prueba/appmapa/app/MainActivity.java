package test.prueba.appmapa.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import test.prueba.appmapa.app.Dominio.Filtros;

public class MainActivity extends Activity {

    //static final  LatLng santiago = new LatLng(-33.796923, -70.922433);

    private static final String LOG_TAG = "ExampleApp";


    FragmentList fragmentList;
    FragmentMap fragmentMap;

    Filtros filtros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowCustomEnabled(true);
        View view = View.inflate(getApplicationContext(), R.layout.actionbar_top,
                null);
        actionBar.setCustomView(view);

        fragmentMap = new FragmentMap();
        fragmentList = new FragmentList();

        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.mainContainer, fragmentMap)
                .commit();

        ImageButton btnFiltros = (ImageButton) findViewById(R.id.action_btnFiltros);

        filtros = new Filtros();
        btnFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityFiltros = new Intent(".Activities.FiltrosActivity");
                activityFiltros.putExtra("filtros", filtros);
                startActivityForResult(activityFiltros, 1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Filtros filtros = (Filtros)data.getSerializableExtra("filtros");

                android.app.FragmentManager fm = getFragmentManager();

                Bundle arguments = new Bundle();
                arguments.putString("localidadReferencia", filtros.getLocalidadReferencia());

                if(fragmentMap.isVisible())
                {

                    fm.beginTransaction()
                            .remove(fragmentMap).commit();

                    fragmentMap = FragmentMap.newInstance(arguments);

                    //fragmentMap = new FragmentMap();

                    fm.beginTransaction()
                            .replace(R.id.mainContainer, fragmentMap)
                            .commit();

                }else
                {

                    fm.beginTransaction()
                            .remove(fragmentList).commit();


                    fragmentList = new FragmentList();

                    fm.beginTransaction()
                            .replace(android.R.id.content, fragmentList)
                            .commit();
                }

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
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

                android.app.FragmentManager fm = getFragmentManager();

                if(fragmentList.isVisible())
                {
                    fm.beginTransaction()
                            .remove(fragmentList)
                            .replace(R.id.mainContainer, fragmentMap)
                            .commit();

                    item.setTitle("Lista");
                }else
                {
                    fm.beginTransaction()
                            .remove(fragmentMap)
                            .replace(R.id.mainContainer, fragmentList)
                            .commit();

                    item.setTitle("Mapa");

                }

                return true;
            case R.id.action_current_location:
               Toast.makeText(getBaseContext(),"Current location",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }






}
