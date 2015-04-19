package solutions.alterego.dronix.droidcar.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;
import solutions.alterego.dronix.droidcar.App;
import solutions.alterego.dronix.droidcar.MainActivity;
import solutions.alterego.dronix.droidcar.NavigationDrawerFragment;
import solutions.alterego.dronix.droidcar.R;
import solutions.alterego.dronix.droidcar.api.CommandManager;
import solutions.alterego.dronix.droidcar.api.models.Directions;
import solutions.alterego.dronix.droidcar.api.models.Server;
import solutions.alterego.dronix.droidcar.interfaces.IFragmentToActivity;
import solutions.alterego.dronix.droidcar.utils.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {

    @InjectView(R.id.server_ip)
    EditText mServerIp;

    @InjectView(R.id.server_port)
    EditText mServerPort;

    @InjectView(R.id.server_port_camera)
    EditText mServerPortCamera;

    @InjectView(R.id.save_config_button)
    Button mSaveConfig;

    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App.component(getActivity()).inject(this);
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.inject(this, view);

        Observable<OnTextChangeEvent> ip =
                WidgetObservable.text(mServerIp).filter(onTextChangeEvent -> onTextChangeEvent.text().length() >= 0);
        Observable<OnTextChangeEvent> port =
                WidgetObservable.text(mServerPort).filter(onTextChangeEvent -> onTextChangeEvent.text().length() >= 0);
        Observable<OnTextChangeEvent> portCam =
                WidgetObservable.text(mServerPortCamera).filter(onTextChangeEvent -> onTextChangeEvent.text().length() >= 0);

        List<Observable<OnTextChangeEvent>> observables = new ArrayList<>();
        observables.add(ip);
        observables.add(port);
        observables.add(portCam);

        Observable.combineLatest(observables, args -> {
            for (Object arg : args) {
                if (arg instanceof OnTextChangeEvent) {
                    if (((OnTextChangeEvent) arg).text().length() <= 0) return false;
                }
            }
            return true;
        }).subscribe(mSaveConfig::setEnabled);
        return view;
    }

    @OnClick(R.id.save_config_button)
    void saveConfig() {
        Observable.just(SharedPreferencesUtils.WriteConfig(mSharedPreferences, new Server(mServerIp.getText().toString(),
                mServerPortCamera.getText().toString(), mServerPortCamera.getText().toString()))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .filter(aVoid -> getActivity() != null)
                .map(aVoid -> (IFragmentToActivity)getActivity())
                .subscribe(IFragmentToActivity::onSaveConfigFinished);
    }

}
