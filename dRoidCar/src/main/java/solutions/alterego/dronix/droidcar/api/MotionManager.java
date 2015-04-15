package solutions.alterego.dronix.droidcar.api;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Subscriber;
import solutions.alterego.dronix.droidcar.utils.MjpegInputStream;

public class MotionManager {

    private AtomicBoolean isStopped;

    public MotionManager() {
        isStopped = new AtomicBoolean(false);
    }

    public Bitmap decodeBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Observable<Bitmap> getBitmap(byte[] imageBytes){
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = decodeBitmap(imageBytes);
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        });
    }


    public Observable<Bitmap> getBytes2(URL url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    byte[] imageBytes = null;
                    Socket socket = new Socket(url.getHost(), 8081);
                    InputStream is = socket.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    String tmp = "";

                    do {
                        tmp = in.readLine();
                    }
                    while (!tmp.startsWith("Content-Type:") && (!isStopped.get()));
                    int idx = tmp.indexOf("boundary");
                    String boundary = tmp.substring(idx + 9);
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
                            Bitmap bitmap = decodeBitmap(imageBytes);
                            subscriber.onNext(bitmap);
                            Thread.sleep(100);
                        }
                    }
                    socket.close();
                } catch (IOException e) {
                    subscriber.onError(e);
                } catch (InterruptedException e) {
                   subscriber.onError(e);
                }
                subscriber.onCompleted();

            }
        });
    }


    public Observable<Bitmap> getBytes(URL url){
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                byte[] imageBytes = null;

                HttpHost host = new HttpHost(url.getHost(), 8081, url.getProtocol());
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
                                while (((is.read()) != '\r' && (is.read()) != '\n') && (!isStopped.get())) {
                                    ;
                                }
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
                                while ((is.read() != '\n') && (!isStopped.get()));
                                while ((is.read() != '\r') && (!isStopped.get()));
                                while ((is.read() != '\n') && (!isStopped.get()));
                                if (is.read(imageBytes, 0, len) != -1) {
                                    Log.i("ASYNCK", "asy" + imageBytes.length);

                                    subscriber.onNext(decodeBitmap(imageBytes));
                                }

                            }
                        }
                    }
                } catch (IOException e) {
                    Log.i("Error streamTask", e.getMessage());
                    subscriber.onError(e);
                }

                subscriber.onCompleted();

            }
        });
    }

        public Observable<Bitmap> getBytes3(URL url){
            return Observable.create(new Observable.OnSubscribe<Bitmap>() {
                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {
                    MjpegInputStream mjpegInputStream = MjpegInputStream.read("http://" +url.getHost()+ ":" + 8081);
                    if (mjpegInputStream == null)
                        subscriber.onError(new Throwable("mjpegInputStream == null"));
                    else {
                        while (!isStopped.get()) {
                            try {
                                subscriber.onNext(mjpegInputStream.readMjpegFrame());
                            } catch (IOException e) {
                                subscriber.onError(e);
                            }
                        }
                    }
                    subscriber.onCompleted();

                }
            });
        }

    public void stop(){
        isStopped.set(true);
    }
}
