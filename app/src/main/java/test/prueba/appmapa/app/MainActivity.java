package test.prueba.appmapa.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        fragmentMap = new FragmentMap();

        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(android.R.id.content, fragmentMap)
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

                fragmentList = new FragmentList();
                fm.beginTransaction().add(android.R.id.content, fragmentList).commit();

                fm.beginTransaction().hide(fragmentMap).commit();


                return true;
            case R.id.action_current_location:
               Toast.makeText(getBaseContext(),"Current location",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }






}
