package test.prueba.appmapa.app;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView;
import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Paginacion;
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
    protected View footerView;

    protected boolean loading = false;

    protected Paginacion paginacion;


    private void listarPropiedades(JSONObject jsonObj) {
        try{
            paginacion.Total  = jsonObj.getJSONObject("paging").getInt("total");
            paginacion.Offset = paginacion.getLimit();

        }catch (JSONException e)
        {

        }


        ArrayList<Propiedad> propiedades = MeliPropiedadJSONParser.parse(jsonObj);

        footerView = ((LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progressbar_footerlist, null, false);

        this.setListAdapter(new ListadoPropiedadesAdapter(
                getActivity(), R.layout.fragment_list, propiedades));

        this.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // nothing here
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (load(firstVisibleItem, visibleItemCount, totalItemCount)) {
                    loading = true;
                    getListView().addFooterView(footerView, null, false);

                    IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {

                        @Override
                        public void onTaskComplete(JSONObject jsonObject) {

                            paginacion.Offset += paginacion.getLimit();

                            ArrayList<Propiedad> propiedades = MeliPropiedadJSONParser.parse(jsonObject);


                            ListadoPropiedadesAdapter listadoPropiedadesAdapter = ((ListadoPropiedadesAdapter) getListAdapter());

                            listadoPropiedadesAdapter.addAll(propiedades);

                            listadoPropiedadesAdapter.notifyDataSetChanged();

                            getListView().removeFooterView(footerView);
                            loading = false;
                        }

                    };

                    StringBuilder url = new StringBuilder(DownLoader.API_MELI_BASE);

                    url.append("&limit=" + paginacion.getLimit());

                    url.append("&offset=" + paginacion.Offset);

                    DownLoaderTask taskPropiedades = new DownLoaderTask(delegado);
                    taskPropiedades.execute(url.toString());
                }
            }
        });

    }
    protected boolean load(int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount && getListView().getChildAt(visibleItemCount -1) != null && getListView().getChildAt(visibleItemCount-1).getBottom() <= getListView().getHeight();
        boolean moreRows = getListAdapter().getCount() < paginacion.Total;
        return moreRows && lastItem && !loading;

    }
    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

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

        paginacion = new Paginacion();
        IAsyncTaskDelegate delegado = new IAsyncTaskDelegate() {

            @Override
            public void onTaskComplete(JSONObject jsonObject) {
                listarPropiedades(jsonObject);
            }

        };

        StringBuilder url = new StringBuilder(DownLoader.API_MELI_BASE);
        url.append("&limit=" + paginacion.getLimit());
        url.append("&offset=" + paginacion.Offset);


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
