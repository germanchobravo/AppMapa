package test.prueba.appmapa.app.Utiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import test.prueba.appmapa.app.Dominio.Propiedad;
import test.prueba.appmapa.app.R;
import test.prueba.appmapa.app.task.DownLoaderImageTask;
import test.prueba.appmapa.app.task.IAsyncTaskDelegateImage;

import java.util.ArrayList;

/**
 * Created by gbravo on 6/30/14.
 */
public class ListadoPropiedadesAdapter extends ArrayAdapter<Propiedad> {

    LayoutInflater inf;
    ArrayList<Propiedad> propiedades;


    public ListadoPropiedadesAdapter(Context context, int textViewResourceId, ArrayList<Propiedad> objects) {
        super(context, textViewResourceId, objects);

        this.inf = LayoutInflater.from(context);
        this.propiedades = objects;

    }

    @Override
    public int getCount() {
        return this.propiedades.size();
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
            row.setTag(holder);

        }else
        {
            holder = (ViewHolder) row.getTag();
            row.setBackground(null);
        }


        String urlImagen = currentPropiedad.getUrlImagen();


        if(urlImagen != "" && currentPropiedad.getDrawableImagen() == null) {

            IAsyncTaskDelegateImage delagado = new IAsyncTaskDelegateImage() {

                @Override
                public void onTaskComplete(Bitmap imagen,int position, View view) {

                    if(imagen != null)
                    {
                        Drawable drawable = new BitmapDrawable(getContext().getResources(), imagen);
                        Propiedad prop = propiedades.get(position);
                        prop.setDrawableImagen(drawable);
                        view.setBackground(drawable);
                    }

                }


            };

            DownLoaderImageTask downLoadImagen = new DownLoaderImageTask(delagado, position, row);
            downLoadImagen.execute(urlImagen);

        }else
        {
            if(Build.VERSION.SDK_INT >= 16) {
                row.setBackground(currentPropiedad.getDrawableImagen());
            }else
            {
                row.setBackgroundDrawable(currentPropiedad.getDrawableImagen());
            }

        }


        holder.txtID.setText(currentPropiedad.getId());
        holder.txtDireccion.setText(currentPropiedad.getTitle());

        return row;
    }

    static class ViewHolder {
        public TextView txtID;
        public TextView txtDireccion;
    }

}
