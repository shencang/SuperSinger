package cn.edu.nc.music.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.edu.nc.music.R;
import cn.edu.nc.music.ShuxainFragmentActivity;

import static android.support.constraint.Constraints.TAG;

public class FragmentYueguan extends android.support.v4.app.Fragment implements View.OnClickListener {
    private LinearLayout song;
    private LinearLayout singer;
    /*private FragmentYueguan fragmentYueguan = new FragmentYueguan();*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_yueguan, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "-----------------------------------可以点击按钮了-----------------------------------");
        LinearLayout Lsong = getActivity().findViewById(R.id.song);
        Lsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShuxainFragmentActivity shuxainFragmentActivity = new ShuxainFragmentActivity();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_framecontent, shuxainFragmentActivity);
                transaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

