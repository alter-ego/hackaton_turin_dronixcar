package solutions.alterego.dronix.droidcar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import solutions.alterego.dronix.droidcar.api.CommandManager;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Directions;


public class MainActivity extends ActionBarActivity {

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
    CommandManager mCommandManager;

    @OnClick(R.id.up_arrow)
    void goToUp() {
        mCommandManager.goTo(Directions.UP);
        YoYo.with(Techniques.BounceInUp).duration(500).playOn(mUpArrow);
    }

    @OnClick(R.id.down_arrow)
    void goToDown() {
        mCommandManager.goTo(Directions.DOWN);
        YoYo.with(Techniques.BounceInDown).duration(500).playOn(mDownArrow);
    }

    @OnClick(R.id.right_arrow)
    void goToRight() {
        mCommandManager.goTo(Directions.RIGHT);
        YoYo.with(Techniques.BounceInLeft).duration(500).playOn(mRightArrow);
    }

    @OnClick(R.id.left_arrow)
    void goToLeft() {
        mCommandManager.goTo(Directions.LEFT);
        YoYo.with(Techniques.BounceInRight).duration(500).playOn(mLeftArrow);
    }

    @OnClick(R.id.car)
    void brake() {
        mCommandManager.brake(new Brake(Brake.FAST));
        YoYo.with(Techniques.Tada).duration(500).playOn(mCar);
    }

    @OnClick(R.id.voice_recognition_btn)
    void startVoiceRecognition() {

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
}
