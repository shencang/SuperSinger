package cn.edu.nc.music;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.edu.nc.music.Fragment.FragmentYueguan;

public class ShuxainFragmentActivity extends Fragment implements View.OnClickListener  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shuxain_fragment, container, false);
        return view;
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout shuaxinsingers = getActivity().findViewById(R.id.singers);
        shuaxinsingers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentYueguan fragmentYueguan = new FragmentYueguan();
                FragmentManager fragmentmanager =  getFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();
                transaction.replace(R.id.main_framecontent,fragmentYueguan);
                transaction.commit();
            }
        });

    }
}

