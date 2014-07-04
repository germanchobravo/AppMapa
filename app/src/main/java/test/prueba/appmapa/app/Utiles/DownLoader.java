package test.prueba.appmapa.app.Utiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by gbravo on 6/29/14.
 */
public class DownLoader {
    /** A method to download json data from url */

    public static final String API_MELI_BASE = "https://mobile.mercadolibre.com.ar/sites/MLC/search?category=MLC1480" +
            "&state=TUxDUE1FVEExM2JlYg&city=TUxDQ1NBTjk4M2M";

    public static String DownloadUrl(String url) throws IOException {
        String data = "";

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            URL _url = new URL(url.toString());
            conn = (HttpURLConnection) _url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

            data = jsonResults.toString();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }

        return data;
    }

    public static Bitmap LoadImagenFromUrl(URL link)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        //HttpURLConnection conn;
        try {
            //in = link.openConnection().getInputStream();

            URLConnection conn =  link.openConnection();
            in = conn.getInputStream();

            bitmap = BitmapFactory.decodeStream(in, null, null);
            in.close();

        }catch (IOException e) { }

        return bitmap;

    }
}
