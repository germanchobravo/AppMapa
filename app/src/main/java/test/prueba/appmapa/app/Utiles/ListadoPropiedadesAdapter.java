package test.prueba.appmapa.app.Utiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONObject;
import test.prueba.appmapa.app.Dominio.Propiedad;
import test.prueba.appmapa.app.R;
import test.prueba.appmapa.app.task.DownLoaderImageTask;
import test.prueba.appmapa.app.task.IAsyncTaskDelegate;

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

    public interface IPrueba
    {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        Propiedad currentPropiedad = (Propiedad) propiedades.get(position);

        ViewHolder holder;
        if(row == null)
        {
            row = inf.inflate(R.layout.fragment_list, null);

            holder = new ViewHolder();
            holder.txtID = (TextView)row.findViewById(R.id.txtID);
            holder.txtDireccion = (TextView)row.findViewById(R.id.txtDireccion);
            holder.relativeLayout = (RelativeLayout)row.findViewById(R.id.contenedor);
            row.setTag(holder);



        }else
        {
            holder = (ViewHolder) row.getTag();
        }


        String urlImagen = currentPropiedad.getUrlImagen();

        Bitmap imagen = null;

        if(urlImagen != "") {

            IAsyncTaskDelegate delagado = new IAsyncTaskDelegate() {
                @Override
                public void onTaskComplete(JSONObject jsonObject) {

                }

                @Override
                public void onTaskComplete(Bitmap imagen, RelativeLayout relativeLayout) {

                    if(imagen != null)
                    {
                        Drawable drawable = new BitmapDrawable(getContext().getResources(), imagen);

                        relativeLayout.setBackground(drawable);
                    }

                }
            };
            DownLoaderImageTask downLoadImagen = new DownLoaderImageTask(delagado, holder.relativeLayout);
            downLoadImagen.execute(urlImagen);

        }

        holder.txtID.setText(currentPropiedad.getId());
        holder.txtDireccion.setText(currentPropiedad.getTitle());

        return row;
    }

    static class ViewHolder {

        public RelativeLayout relativeLayout;
        public TextView txtID;
        public TextView txtDireccion;
    }

}
