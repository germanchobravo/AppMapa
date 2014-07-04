package test.prueba.appmapa.app.task;


import android.graphics.Bitmap;
import android.widget.RelativeLayout;
import org.json.JSONObject;

/**
 * Created by gbravo on 6/23/14.
 */

public interface IAsyncTaskDelegate {
    public void onTaskComplete(JSONObject jsonObject);
    public void onTaskComplete(Bitmap imagen, RelativeLayout relativeLayout);
}

