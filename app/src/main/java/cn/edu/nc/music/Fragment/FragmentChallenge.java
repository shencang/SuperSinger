package cn.edu.nc.music.Fragment;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nc.music.Activity.MainActivity;
import cn.edu.nc.music.Adapter.MyAdapter;
import cn.edu.nc.music.Bean.Song;
import cn.edu.nc.music.MedioUse.MusicUtils;
import cn.edu.nc.music.MedioUse.RequestPermissions;
import cn.edu.nc.music.R;

public class FragmentChallenge extends android.support.v4.app.Fragment implements View.OnClickListener {


    private ListView mListView;
    private List<Song> list;
    private MyAdapter adapter;
    private ImageView musicplay;
    private ImageView musicnextsong;
    private ImageView musicpresong;
    private int songIndex = -1;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<Song> mSongList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_fragment_challenge, container, false);
        musicplay =view.findViewById(R.id.music_playorpause);
        musicnextsong = view.findViewById(R.id.next_song);
        musicpresong = view.findViewById(R.id.previous_song);
        /*动态申请权限*/
        //ss
        RequestPermissions.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                new RequestPermissions.OnPermissionsRequestListener() {
                    @Override
                    public void onGranted() {
                        // 同意权限后加载音乐列表并初始化事件
                        initView(view);
                    }

                    @Override
                    public void onDenied(List<String> deniedList) {
                        Toast.makeText(getActivity(),"拒绝权限将无法获取歌曲目录",Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
    /*播放音乐、上一首、下一首按钮*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_playorpause:

                break;
            case R.id.next_song:

                break;
            case R.id.previous_song:
                break;
        }
    }
    /*扫描本地歌曲，显示在挑战界面*/
    private void initView(View view) {
        mListView = (ListView)view.findViewById(R.id.main_listview);
        list = new ArrayList<>();
        //把扫描到的音乐赋值给list
        list = MusicUtils.getMusicData(this.getContext());
        adapter = new MyAdapter((MainActivity) this.getContext(),list);
        mListView.setAdapter(adapter);
    }
    /*播放音乐*/
    public void playmusic(Song song){

    }
    /*播放下一首*/
    public void playNextSong(){
    }
}
