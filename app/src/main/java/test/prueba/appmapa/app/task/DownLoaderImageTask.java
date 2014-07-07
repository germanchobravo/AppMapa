package test.prueba.appmapa.app.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import test.prueba.appmapa.app.Utiles.DownLoader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gbravo on 7/2/14.
 */
public class DownLoaderImageTask extends AsyncTask<String, Void, Bitmap> {
    public IAsyncTaskDelegateImage delegado;
    private View content = null;
    private int position = 0;

    Bitmap imagen = null;

    public DownLoaderImageTask(IAsyncTaskDelegateImage del,int position, View content) {
        this.delegado = del;
        this.content = content;
        this.position = position;
    }

    @Override
    protected Bitmap doInBackground(String... url) {
        try{
            URL _url = new URL(url[0]);

            imagen = DownLoader.LoadImagenFromUrl(_url);

        }catch (MalformedURLException ex)
        {

        }
        catch(Exception e){
            Log.d("Background Task", e.toString());
        }
        return imagen;
    }

    @Override
    protected void onPostExecute(Bitmap imagen) {
        super.onPostExecute(imagen);

        try {
            if(imagen != null && !isCancelled())
            {

                delegado.onTaskComplete(imagen, this.position, this.content);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
