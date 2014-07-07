package test.prueba.appmapa.app;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Propiedad;
import test.prueba.appmapa.app.Parsers.MeliPropiedadJSONParser;
import test.prueba.appmapa.app.Utiles.DownLoader;
import test.prueba.appmapa.app.Utiles.ListadoPropiedadesAdapter;
import test.prueba.appmapa.app.task.DownLoaderTask;
import test.prueba.appmapa.app.task.IAsyncTaskDelegate;

import java.util.ArrayList;

/**
 * Created by gbravo on 6/26/14.
 */

public class FragmentList extends ListFragment  implements AbsListView.OnScrollListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
        //setRetainInstance(true);

    }

    private void listarPropiedades(JSONObject jsonObj) {
        ArrayList<Propiedad> propiedades = MeliPropiedadJSONParser.parse(jsonObj);

        this.setListAdapter(new ListadoPropiedadesAdapter(
                getActivity(), R.layout.fragment_list, propiedades));


    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        /*
        IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {
            @Override
            public void onTaskComplete(JSONObject jsonObject) {
                listarPropiedades(jsonObject);
            }
        };

        StringBuilder url = new StringBuilder(DownLoader.API_MELI_BASE);
        DownLoaderTask taskPropiedades = new DownLoaderTask(delegado);
        taskPropiedades.execute(url.toString());
        */
}

  /*  @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        mCurrIdx = pos;
        getListView().setItemChecked(pos, true);
        mListener.onListSelection(pos);
    }*/

    private void setData()
    {
        //this.setListAdapter(New );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_inferior, menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {

            @Override
            public void onTaskComplete(JSONObject jsonObject) {
                listarPropiedades(jsonObject);
            }

        };

        StringBuilder url = new StringBuilder(DownLoader.API_MELI_BASE);
        DownLoaderTask taskPropiedades = new DownLoaderTask(delegado);
        taskPropiedades.execute(url.toString());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_lista:
                android.app.FragmentManager fm = getFragmentManager();

                FragmentMap fragmentMap = new FragmentMap();
                FragmentList fragmentList = new FragmentList();



                fm.beginTransaction().hide(fragmentList).commit();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
