package test.prueba.appmapa.app.task;


import org.json.JSONObject;

/**
 * Created by gbravo on 6/23/14.
 */

public interface IAsyncTaskDelegate {
    public void onTaskComplete(JSONObject jsonObject);
}

