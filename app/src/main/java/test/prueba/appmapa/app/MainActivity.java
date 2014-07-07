package test.prueba.appmapa.app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    //static final  LatLng santiago = new LatLng(-33.796923, -70.922433);

    private static final String LOG_TAG = "ExampleApp";

    FragmentList fragmentList;
    FragmentMap fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view = View.inflate(getApplicationContext(), R.layout.actionbar_top,
                null);
        actionBar.setCustomView(view);

        fragmentMap = new FragmentMap();
        fragmentList = new FragmentList();

        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(android.R.id.content, fragmentMap)
                .add(android.R.id.content, fragmentList)
                .hide(fragmentList)
                .commit();

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
                            .hide(fragmentList)
                            //.remove(fragmentList)
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
