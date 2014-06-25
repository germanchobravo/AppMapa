package test.prueba.appmapa.app;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by gbravo on 6/24/14.
 */
public class SplashScreen extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        Thread timer = new Thread() {

            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent actividaPrincipal = new Intent("test.prueba.appmapa.app.MainActivity");
                    startActivity(actividaPrincipal);
                }
            }
        };

        timer.start();
    }
}
