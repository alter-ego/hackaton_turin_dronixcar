package solutions.alterego.dronix.droidcar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import solutions.alterego.dronix.droidcar.api.CommandManager;
import solutions.alterego.dronix.droidcar.api.MotionManager;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Directions;

public class FullScreenActivity extends Activity {
    private SensorManager mSensorManager;
    private Display mDisplay;

    @InjectView(R.id.up_arrow)
    ImageView mUpArrow;

    @InjectView(R.id.down_arrow)
    ImageView mDownArrow;

    @InjectView(R.id.car)
    ImageView mCar;

    @Inject
    MotionManager mMotionManager;

    @Inject
    CommandManager mCommandManager;

    @InjectView(R.id.motion)
    ImageView mMotionImage;

    @Inject
    SharedPreferences mSharedPreferences;

    @OnClick(R.id.car)
    void brake() {
        mCommandManager.brake(new Brake(Brake.FAST)).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.Tada).duration(500).playOn(mCar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component(this).inject(this);

        setContentView(R.layout.full_screen_activity);
        ButterKnife.inject(this);

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());
        }

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensorManager.registerListener(linearAccelerationListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);


        mMotionManager.getBytes3(mSharedPreferences)
                .filter(bitmap -> bitmap != null).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Drawable drawable = mMotionImage.getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                            Bitmap bitmap2 = bitmapDrawable.getBitmap();
                            bitmap2.recycle();
                        }
                        mMotionImage.setImageBitmap(bitmap);
                        mMotionImage.invalidate();
                    }
                }
        );
        mDisplay = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
    }
    @OnClick(R.id.up_arrow)
    void goToUp() {
        mCommandManager.goTo(Directions.UP).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.FadeIn).duration(300).playOn(mUpArrow);
    }

    @OnClick(R.id.down_arrow)
    void goToDown() {
        mCommandManager.goTo(Directions.DOWN).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.FadeIn).duration(300).playOn(mDownArrow);
    }

    void goToRight() {
        mCommandManager.goTo(Directions.RIGHT).subscribeOn(Schedulers.io()).subscribe();
    }

    void goToLeft() {
        mCommandManager.goTo(Directions.LEFT).subscribeOn(Schedulers.io()).subscribe();
    }

    void goToReset() {
        mCommandManager.goTo(Directions.RESET).subscribeOn(Schedulers.io()).subscribe();
    }

    private static final int THRESHHOLD = 2;
    private static final float RANGE = 3f;
    private static final float RANGE_CENTER = 0.5f;
    /**reset 0, left 1, right 2*/
    private int currentPosition;
    private float mLastX;
    private SensorEventListener linearAccelerationListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values.clone();
            float x = values[0];
            float y = values[0];
            float z = values[0];

            double acceleration = (x * x)
                    + (y * y)
                    + (z * z);

            if (acceleration > THRESHHOLD) {
                switch (mDisplay.getRotation()) {
                    case Surface.ROTATION_0:
                        x = event.values[0];
                        break;
                    case Surface.ROTATION_90:
                        x = -event.values[1];
                        break;
                    case Surface.ROTATION_180:
                        x = -event.values[0];
                        break;
                    case Surface.ROTATION_270:
                        x = event.values[1];
                        break;
                }
                if (x > RANGE && currentPosition != 1) {
                    goToLeft();
                    currentPosition = 1;
                    mLastX = x;
                } else if (x < -RANGE && currentPosition != 2) {
                    goToRight();
                    currentPosition = 2;
                    mLastX = x;
                } else if ((x < RANGE_CENTER && x > - RANGE_CENTER) && currentPosition != 0) {
                    goToReset();
                    currentPosition = 0;
                }


            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(linearAccelerationListener);

    }
}
