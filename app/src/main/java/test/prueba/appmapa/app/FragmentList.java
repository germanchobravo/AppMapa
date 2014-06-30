package test.prueba.appmapa.app;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.*;
import test.prueba.appmapa.app.Dominio.Propiedad;
import test.prueba.appmapa.app.Utiles.ListadoPropiedadesAdapter;

import java.util.ArrayList;

/**
 * Created by gbravo on 6/26/14.
 */

public class FragmentList extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        setHasOptionsMenu(true);
        setRetainInstance(true);
*/
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        ArrayList<Propiedad> propiedades = new ArrayList<Propiedad>();

        Propiedad propiedad = new Propiedad();
        propiedad.setId("ML01");
        propiedad.setTitle("Esta es la direccion");

        propiedades.add(propiedad);

        this.setListAdapter(new ListadoPropiedadesAdapter(
                getActivity(),R.layout.fragment_list,0, propiedades));

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inferior, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                countries
        );
        setListAdapter(adapter);
        */

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

}
