package solutions.alterego.dronix.droidcar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
import solutions.alterego.dronix.droidcar.utils.VoicePatternUtils;


public class MainActivity extends ActionBarActivity {

    protected static final int REQUEST_OK = 1;

    @InjectView(R.id.car)
    ImageView mCar;

    @InjectView(R.id.up_arrow)
    ImageView mUpArrow;

    @InjectView(R.id.down_arrow)
    ImageView mDownArrow;

    @InjectView(R.id.right_arrow)
    ImageView mRightArrow;

    @InjectView(R.id.left_arrow)
    ImageView mLeftArrow;

    @InjectView(R.id.voice_recognition_btn)
    Button mVoiceRecBtn;

    @Inject
    MotionManager mMotionManager;

    @Inject
    CommandManager mCommandManager;

    @InjectView(R.id.motion)
    ImageView mMotionImage;

    @OnClick(R.id.up_arrow)
    void goToUp() {
        mCommandManager.goTo(Directions.UP).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.BounceInUp).duration(500).playOn(mUpArrow);
    }

    @OnClick(R.id.down_arrow)
    void goToDown() {
        mCommandManager.goTo(Directions.DOWN).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.BounceInDown).duration(500).playOn(mDownArrow);
    }

    @OnClick(R.id.right_arrow)
    void goToRight() {
        mCommandManager.goTo(Directions.RIGHT).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.BounceInLeft).duration(500).playOn(mRightArrow);
    }

    @OnClick(R.id.left_arrow)
    void goToLeft() {
        mCommandManager.goTo(Directions.LEFT).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.BounceInRight).duration(500).playOn(mLeftArrow);
    }

    @OnClick(R.id.car)
    void brake() {
        mCommandManager.brake(new Brake(Brake.FAST)).subscribeOn(Schedulers.io()).subscribe();
        YoYo.with(Techniques.Tada).duration(500).playOn(mCar);
    }

    @OnClick(R.id.voice_recognition_btn)
    void startVoiceRecognition() {
        Intent recVoiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recVoiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        recVoiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.desc_dialog_voice_recognition));
        recVoiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recVoiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ITALIAN);
        try {
            startActivityForResult(recVoiceIntent, REQUEST_OK);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component(this).inject(this);

        setContentView(R.layout.activity_main);
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
        URL url = null;
        try {
            url = new URL(getString(R.string.api_endpoint));
        } catch (MalformedURLException e) {

        }

        if (url != null) {
            mMotionManager.getBytes2(url)
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OK && resultCode == RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Directions directions = VoicePatternUtils.RecognitionDirection(thingsYouSaid);

            AndroidSchedulers.mainThread().createWorker().schedule(() -> {
                if (directions != null) {
                    switch (directions) {
                        case DOWN:
                            goToDown();
                            break;
                        case LEFT:
                            goToLeft();
                            break;
                        case RIGHT:
                            goToRight();
                            break;
                        case UP:
                            goToUp();
                            break;
                    }
                } else {
                    Brake brake = VoicePatternUtils.RecognitionCommand(thingsYouSaid);
                    if (brake != null) {
                        brake();
                    }
                }
            }, 300, TimeUnit.MILLISECONDS);
        }
    }
}
