package solutions.alterego.dronix.droidcar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import solutions.alterego.dronix.droidcar.R;

public class AboutFragment extends Fragment {

    @InjectView(R.id.about_recycler_view)
    RecyclerView mAboutRecView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.inject(this, view);

        List<String> aboutList = Arrays.asList(getResources().getStringArray(R.array.abouts));
        AboutAdapter aboutAdapter = new AboutAdapter(aboutList);

        mAboutRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAboutRecView.setHasFixedSize(true);
        mAboutRecView.setAdapter(aboutAdapter);
        return view;
    }

    public static class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> {

        private List<String> mAboutList;

        public AboutAdapter(List<String> abouts) {
            this.mAboutList = abouts;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {

            View root;

            @InjectView(R.id.info_text)
            TextView infoText;

            public ViewHolder(View view) {
                super(view);
                root = view;
                ButterKnife.inject(this, view);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_about_fragment, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        public void onBindViewHolder(final ViewHolder holder, final int i) {
            String name = mAboutList.get(i);
            holder.infoText.setText(name);
        }

        @Override
        public int getItemCount() {
            return mAboutList.size();
        }
    }
}
