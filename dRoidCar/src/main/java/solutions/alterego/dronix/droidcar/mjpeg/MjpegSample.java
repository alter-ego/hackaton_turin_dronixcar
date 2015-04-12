package solutions.alterego.dronix.droidcar.mjpeg;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MjpegSample extends Activity {

    private MjpegView mv;
    private static final int MENU_QUIT = 1;

    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {    
    menu.add(0, MENU_QUIT, 0, "Quit");
    return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {    
        switch (item.getItemId()) {
            case MENU_QUIT:
                finish();
                return true;    
            }    
        return false;
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //sample public cam
        String URL = "http://192.168.43.87:8081";

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        mv = new MjpegView(this);
        setContentView(mv);

        getRead(URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MjpegInputStream>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MjpegInputStream mjpegInputStream) {
                        mv.setSource(mjpegInputStream);
                        mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
                        mv.showFps(false);
                    }
                });
    }

    private Observable<MjpegInputStream> getRead(String URL) {
        return Observable.create(new Observable.OnSubscribe<MjpegInputStream>() {
            @Override
            public void call(Subscriber<? super MjpegInputStream> subscriber) {
                subscriber.onNext(MjpegInputStream.read(URL));
            }
        });
    }

    public void onPause() {
        super.onPause();
        mv.stopPlayback();
    }
}