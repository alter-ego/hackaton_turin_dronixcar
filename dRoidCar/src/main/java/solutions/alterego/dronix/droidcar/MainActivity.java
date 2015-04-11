package solutions.alterego.dronix.droidcar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import solutions.alterego.dronix.droidcar.api.CommandManager;
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
        Intent recVoiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recVoiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        recVoiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "invia comando");
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
            if (directions != null){
                Toast.makeText(getApplicationContext(), directions.name(), Toast.LENGTH_LONG).show();
                mCommandManager.goTo(directions);
            }

        }
    }
}
