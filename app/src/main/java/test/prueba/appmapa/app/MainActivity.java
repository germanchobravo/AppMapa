package test.prueba.appmapa.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Filtros;
import test.prueba.appmapa.app.Dominio.Paginacion;
import test.prueba.appmapa.app.Dominio.Propiedad;
import test.prueba.appmapa.app.Parsers.MeliPropiedadJSONParser;
import test.prueba.appmapa.app.Utiles.DownLoader;
import test.prueba.appmapa.app.task.DownLoaderTask;
import test.prueba.appmapa.app.task.IAsyncTaskDelegate;

import java.util.ArrayList;

public class MainActivity extends Activity {

    //static final  LatLng santiago = new LatLng(-33.796923, -70.922433);

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;

    private static final String LOG_TAG = "ExampleApp";


    FragmentList fragmentList;
    FragmentMap fragmentMap;

    Filtros filtros;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuTitles));

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.icono_menu,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ActionBar actionBar = getActionBar();

        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME );


       // actionBar.setTitle("Probando la wea de action bar");
       // actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        View view = View.inflate(getApplicationContext(), R.layout.actionbar_top,
                null);
        actionBar.setCustomView(view);

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


        fragmentMap = new FragmentMap();
        fragmentList = new FragmentList();

        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.mainContainer, fragmentMap)
                .add(R.id.mainContainer, fragmentList)
                .hide(fragmentList)
                .commit();


    }

    protected Paginacion paginacion;

    @Override
    protected void onStart() {
        super.onStart();

        paginacion = new Paginacion();
        IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {

            @Override
            public void onTaskComplete(JSONObject jsonObject) {

                try
                {
                    paginacion.Total  = jsonObject.getJSONObject("paging").getInt("total");
                    paginacion.Offset = paginacion.getLimit();

                } catch (JSONException ex)
                {

                }


                ArrayList<Propiedad> propiedades = MeliPropiedadJSONParser.parse(jsonObject);
                fragmentMap.onCallBackPropiedades(propiedades);
            }

        };

        StringBuilder url = new StringBuilder(DownLoader.API_MELI_BASE);

        DownLoaderTask taskPropiedades = new DownLoaderTask(delegado);
        taskPropiedades.execute(url.toString());

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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId())
        {
            case R.id.action_lista:

                android.app.FragmentManager fm = getFragmentManager();

                if(fragmentList.isVisible())
                {
                    fm.beginTransaction()
                            .hide(fragmentList)
                            .show(fragmentMap)
                            .commit();

                    item.setTitle("Lista");
                }else
                {
                    fm.beginTransaction()
                            .hide(fragmentMap)
                            .show(fragmentList)
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
