package test.prueba.appmapa.app.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import test.prueba.appmapa.app.Dominio.Filtros;
import test.prueba.appmapa.app.R;

import java.util.ArrayList;

/**
 * Created by gbravo on 7/22/14.
 */
public class DialogTipoPropiedades extends DialogFragment implements View.OnClickListener {

    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;

    public static DialogTipoPropiedades newInstance(Bundle arguments)
    {
        DialogTipoPropiedades f = new DialogTipoPropiedades();
        if(arguments != null)
        {
            f.setArguments(arguments);
        }

        return f;
    }



    public static interface OnPropiedadesSeleccionada{
        void onPropiedadesSeleccionada(String[] arrayString);
    }

    private OnPropiedadesSeleccionada mSeleccionListener;

    @Override
     public void dismiss() {
        super.dismiss();

        this.mSeleccionListener = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.mSeleccionListener = (OnPropiedadesSeleccionada)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " debe implementar OnPropiedadesSeleccionada");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setStyle(STYLE_NORMAL, R.style.DialogListadoPropiedades);


    }

    @Override
    public void onStart() {
        super.onStart();

        Filtros filtros = (Filtros)getArguments().getSerializable("filtros");

        if(filtros.getTiposPropiedad() != null && filtros.getTiposPropiedad().length > 0)
        {
            for (int i = 0; i < filtros.getTiposPropiedad().length ; i++) {

                for (int j = 0; j < adapter.getCount(); j++)
                {
                    if(adapter.getItem(j).equals(filtros.getTiposPropiedad()[i]))
                    {
                        listView.setItemChecked(j, true);
                    }
                }

            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Seleccionar tipo de propiedad");

        View view = inflater.inflate(R.layout.dialogo_tipo_propiedades, container, false);

        String[] tipoPropiedades = getResources().getStringArray(R.array.tipoPropiedades_array);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, tipoPropiedades);

        listView = (ListView)view.findViewById(R.id.listTipoPropiedad);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        Button btnAplicar = (Button) view.findViewById(R.id.btnAplicar);

        Button btnCancelar = (Button) view.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        btnAplicar.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getItem(position).equals("Todos"))
                {
                    SparseBooleanArray checkeds =  listView.getCheckedItemPositions();

                    int index = checkeds.keyAt(0);

                    if(adapter.getItem(index).equals("Todos")) {

                        for (int i = 1; i < checkeds.size(); i++) {
                            // Item position in adapter
                            index = checkeds.keyAt(i);
                            // Add sport if it is checked i.e.) == TRUE!
                            if (checkeds.valueAt(i))
                                listView.setItemChecked(i, false);
                        }
                    }
                }else {
                    listView.setItemChecked(0, false);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }

        String[] outputStrArr = new String[selectedItems.size()];
        if(selectedItems.size() > 0) {
            for (int i = 0; i < selectedItems.size(); i++) {
                outputStrArr[i] = selectedItems.get(i);
            }
        }
        mSeleccionListener.onPropiedadesSeleccionada(outputStrArr);

        getDialog().dismiss();
    }


}
