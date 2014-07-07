package test.prueba.appmapa.app.task;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by gbravo on 7/5/14.
 */
public interface IAsyncTaskDelegateImage<T> {
    public void onTaskComplete(Bitmap imagen, int position, View view);
}
