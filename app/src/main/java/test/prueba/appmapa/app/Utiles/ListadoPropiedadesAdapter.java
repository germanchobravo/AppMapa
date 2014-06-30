package test.prueba.appmapa.app.Utiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import test.prueba.appmapa.app.Dominio.Propiedad;
import test.prueba.appmapa.app.R;

import java.util.ArrayList;

/**
 * Created by gbravo on 6/30/14.
 */
public class ListadoPropiedadesAdapter extends ArrayAdapter<Propiedad> {

    LayoutInflater inf;
    ArrayList<Propiedad> propiedades;


    public ListadoPropiedadesAdapter(Context context, int resource, int textViewResourceId, ArrayList<Propiedad> objects) {
        super(context, resource, textViewResourceId, objects);

        this.inf = LayoutInflater.from(context);
        this.propiedades = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        Propiedad currentPropiedad = (Propiedad) propiedades.get(position);

        if(row == null)
        {
            row = inf.inflate(R.layout.fragment_list, null);
        }

        TextView txtID = (TextView)row.findViewById(R.id.txtID);
        txtID.setText(currentPropiedad.getId());

        TextView txtDireccion = (TextView)row.findViewById(R.id.txtDireccion);
        txtDireccion.setText(currentPropiedad.getTitle());

        return row;
    }
}
