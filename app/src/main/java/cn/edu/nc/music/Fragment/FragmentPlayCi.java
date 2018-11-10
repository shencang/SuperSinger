package cn.edu.nc.music.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nc.music.R;

public class FragmentPlayCi extends android.support.v4.app.Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_play_ci, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
