package solutions.alterego.dronix.droidcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Directions;
import solutions.alterego.dronix.droidcar.fragment.AboutFragment;
import solutions.alterego.dronix.droidcar.fragment.ArrowFragment;
import solutions.alterego.dronix.droidcar.fragment.SettingsFragment;
import solutions.alterego.dronix.droidcar.interfaces.IFragmentToActivity;
import solutions.alterego.dronix.droidcar.utils.SharedPreferencesUtils;
import solutions.alterego.dronix.droidcar.utils.VoicePatternUtils;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, IFragmentToActivity{
    private enum ITEM_SELECTED{SETTINGS, ARROW, ABOUT}
    private ITEM_SELECTED mItemSelected;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawer;

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

        setSupportActionBar(mToolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawer,
                mToolbar);

        if(SharedPreferencesUtils.ReadConfig(PreferenceManager.getDefaultSharedPreferences(this)).ip.equals(""))
            mItemSelected = ITEM_SELECTED.SETTINGS;
        else
            onSaveConfigFinished();



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
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 1:
                if (mItemSelected != ITEM_SELECTED.ARROW) {
                    replaceFragmentAndInit(new ArrowFragment(), ITEM_SELECTED.ARROW.name());
                    mItemSelected = ITEM_SELECTED.ARROW;
                }
                break;
            case 2:
                if (mItemSelected != ITEM_SELECTED.ABOUT) {
                    replaceFragmentAndInit(new AboutFragment(), ITEM_SELECTED.ABOUT.name());
                    mItemSelected = ITEM_SELECTED.ABOUT;
                }
                break;
            default:
                if (mItemSelected != ITEM_SELECTED.SETTINGS) {
                    replaceFragmentAndInit(new SettingsFragment(), ITEM_SELECTED.SETTINGS.name());
                    mItemSelected = ITEM_SELECTED.SETTINGS;
                }
        }
    }

    @Override
    public void onNavigationDrawerClosed() {

    }

    private void replaceFragmentAndInit(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (tag != null)
            ft.replace(R.id.container, fragment, tag);
        else
            ft.replace(R.id.container, fragment);

        ft.commit();
    }

    @Override
    public void onSaveConfigFinished() {
        mNavigationDrawerFragment.selectItem(1);
        onNavigationDrawerItemSelected(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
