package solutions.alterego.dronix.droidcar;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;


public class GetImage {

    public GetImage() {
        isStopped = new AtomicBoolean(false);
    }

    /*final Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);*/
    private AtomicBoolean isStopped;

    public byte[] getImage() {
        byte[] imageBytes = null;

        URL url;
        try {
            url = new URL("http://dronix.dyndns.org:8081");
        } catch (MalformedURLException e) {
            Log.i("Error streamTask", e.getMessage());
            return null;

        }

        HttpHost host = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        HttpGet httpget = new HttpGet("/");
        HttpClient httpClient = new DefaultHttpClient();


        try {
            HttpResponse response = httpClient.execute(host, httpget);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                InputStream is = response.getEntity().getContent();
                if (is != null) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    String tmp = "";

                    do {
                        tmp = in.readLine();
                    }
                    while (!tmp.toLowerCase().startsWith("content-type:") && (!isStopped.get()));

                    // while((is.read())!='\n');//da valutare se il readLine ha gia tolto i \r\n
                    while (!isStopped.get()) {
                        int len = 0, c;
                        while (((is.read()) != '\r' && (is.read()) != '\n') && (!isStopped.get())) ;
                        int bs;
                        while (((bs = is.read()) != -1) && (!isStopped.get())) {
                            if (bs == 'C' && is.read() == 'o' && is.read() == 'n' && is.read() == 't' &&
                                    is.read() == 'e' && is.read() == 'n' && is.read() == 't' && is.read() == '-' && is.read() == 'L' &&
                                    is.read() == 'e' && is.read() == 'n' && is.read() == 'g' && is.read() == 't' && is.read() == 'h') {

                                while (((c = is.read()) != '\r') && (!isStopped.get())) {

                                    if ((c >= '0') && (c <= '9')) {
                                        len = (len * 10) + c - 48;
                                    }
                                }
                                break;
                            }
                        }
                        imageBytes = new byte[len];
                        while ((is.read() != '\n') && (!isStopped.get())) ;
                        while ((is.read() != '\r') && (!isStopped.get())) ;
                        while ((is.read() != '\n') && (!isStopped.get())) ;
                        if (is.read(imageBytes, 0, len) != -1) {
                            Log.i("ASYNCK", "asy" + imageBytes.length);
                        }

                    }
                }
            }
        } catch (IOException e) {
            Log.i("Error streamTask", e.getMessage());
        }
    return imageBytes;}
}
