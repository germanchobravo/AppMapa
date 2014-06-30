package test.prueba.appmapa.app.task;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import test.prueba.appmapa.app.Utiles.DownLoader;

/**
 * Created by gbravo on 6/29/14.
 */
// Fetches data from url passed
public class DownLoaderTask extends AsyncTask<String, Void, String> {

    public IAsyncTaskDelegate delegado;

    public DownLoaderTask(IAsyncTaskDelegate del) {
        this.delegado = del;
    }

    @Override
    protected String doInBackground(String... url) {

        String data = "";

        try{
            data = DownLoader.DownloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObj = new JSONObject(result);

            delegado.onTaskComplete(jsonObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
