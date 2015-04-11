package solutions.alterego.dronix.droidcar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import solutions.alterego.dronix.droidcar.api.CommandManager;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Directions;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.view)
    ImageView mView;

    @InjectView(R.id.up_arrow)
    ImageView mUpArrow;

    @InjectView(R.id.down_arrow)
    ImageView mDownArrow;

    @InjectView(R.id.right_arrow)
    ImageView mRightArrow;

    @InjectView(R.id.left_arrow)
    ImageView mLeftArrow;

    @Inject
    CommandManager mCommandManager;

    @OnClick(R.id.up_arrow)
    void goToUp() {
        mCommandManager.goTo(Directions.UP);
    }

    @OnClick(R.id.down_arrow)
    void goToDown() {
        mCommandManager.goTo(Directions.DOWN);
    }

    @OnClick(R.id.right_arrow)
    void goToRight() {
        mCommandManager.goTo(Directions.RIGHT);
    }

    @OnClick(R.id.left_arrow)
    void goToLeft() {
        mCommandManager.goTo(Directions.LEFT);
    }

    @OnClick(R.id.view)
    void brake() {
        mCommandManager.brake(new Brake(Brake.FAST));
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
}
