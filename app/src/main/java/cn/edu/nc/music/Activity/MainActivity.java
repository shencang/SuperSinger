package cn.edu.nc.music.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import cn.edu.nc.music.R;
import cn.edu.nc.music.Fragment.FragmentChallenge;
import cn.edu.nc.music.Fragment.FragmentPaihang;
import cn.edu.nc.music.Fragment.FragmentYueguan;
import cn.edu.nc.music.permission.PermissionHelper;
import cn.edu.nc.music.utils.CopyAssToSD;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private ImageView main_search;
    private FrameLayout singers;
    private FragmentYueguan fragmentyueguan;
    private FragmentPaihang fragmentPaihang;
    private FragmentChallenge fragmentChallenge;
    private FrameLayout yueguan;
    private FrameLayout paihang;
    private FrameLayout tiaozhan;
    private LinearLayout navHeaderLl;
    private ImageView headerImg;
    private PermissionHelper mPermissionHelper;
    private ImageView button1;


    //音乐播放功能
    public String LrcName="在多年以后.lrc";
    public String TempLrc="";
    public String lrcPath="/storage/emulated/0/在多年以后.lrc";
    public String Newlrc="";
    public String musicPath="/storage/emulated/0/在多年以后.mp3";
    public  String[] contentNum;
    public MediaPlayer mPlayer;
    private Timer mTimer;
    ///
    private static final int REQUESET_PERMISSIONS_CODE = 1;

    //需要申请权限的集合
    private List<String> mPermissionList = new ArrayList<>();

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO

    };

    private String[] permStr = {
            "需要录音权限",
            "需要读取内存卡权限"

    };
    ///
    private static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yueguan = findViewById(R.id.yueguan);
        paihang = findViewById(R.id.paihang);
        tiaozhan = findViewById(R.id.tiaozhan);
        main_search = findViewById(R.id.main_search);
        singers = findViewById(R.id.singer);
        button1=findViewById(R.id.music_playorpause);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    button1.setBackgroundResource(R.drawable.ic_pause);
                    beginLrcPlay(musicPath);
                    i=10;
                }
                if(mPlayer.isPlaying()){
                    mPlayer.pause();
                    button1.setBackgroundResource(
                            R.drawable.ic_main_play
                    );
                }else {
                    mPlayer.start();
                    button1.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });
        //-------------------------------------------------//
        //动态权限
        //初始化并发起权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            doBackup();
        }

        //-------------------------------------------------//
        main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, search.class);
                startActivity(intent);
            }
        });

        singers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayPage.class);
                if (mPlayer.isPlaying()==true)
                {mPlayer.pause();startActivity(intent);}

                startActivity(intent);
            }
        });
       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);
        initDate();
        Clickyg();
        //侧滑
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeaderLl = (LinearLayout) navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerImg = navHeaderLl.findViewById(R.id.header_img);
        Glide.with(this).load(R.mipmap.slide_user).into(headerImg);
        Glide.with(this).load(R.mipmap.slide_back).centerCrop().into(new ViewTarget<View, GlideDrawable>
                (navHeaderLl) {

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });
        CopyAssToSD.getInstance(MainActivity.this).copyAssetsToSD("","");

    }


    //碎片
    public void initDate() {
        yueguan.setOnClickListener((View.OnClickListener) this);
        paihang.setOnClickListener((View.OnClickListener) this);
        tiaozhan.setOnClickListener((View.OnClickListener) this);
    }

    public void Clickyg() {
        fragmentyueguan = new FragmentYueguan();
        android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_framecontent, fragmentyueguan);
        fragmentTransaction.commit();
        yueguan.setSelected(true);
    }

    public void Clickph() {
        fragmentPaihang = new FragmentPaihang();
        android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_framecontent, fragmentPaihang);
        fragmentTransaction.commit();
        yueguan.setSelected(true);
    }

    public void Clicktz() {
        fragmentChallenge = new FragmentChallenge();
        android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_framecontent, fragmentChallenge);
        fragmentTransaction.commit();
        yueguan.setSelected(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.local_music) {
            Intent intent = new Intent(MainActivity.this, Local_music_avtivity.class);
            startActivity(intent);
        } else if (id == R.id.dowland) {
            Intent intent = new Intent(MainActivity.this, dowland_activity.class);
            startActivity(intent);
        } else if (id == R.id.nearly_play) {
            Intent intent = new Intent(MainActivity.this, nearly_play_activity.class);
            startActivity(intent);
        } else if (id == R.id.I_like) {
            Intent intent = new Intent(MainActivity.this, I_like_activity.class);
            startActivity(intent);
        } else if (id == R.id.Messsage) {
            Intent intent = new Intent(MainActivity.this, Messsage_activity.class);
            startActivity(intent);
        } else if (id == R.id.challenge) {
            Intent intent = new Intent(MainActivity.this, search.class);
            startActivity(intent);
        } else if (id == R.id.info) {
            Intent intent = new Intent(MainActivity.this, Info_activity.class);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, Setting_activity.class);
            startActivity(intent);
        }

        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yueguan:
                Clickyg();
                break;
            case R.id.paihang:
                Clickph();
                break;
            case R.id.tiaozhan:
                Clicktz();
                break;

        }
    }
    ///
    private void checkPermissions() {
        mPermissionList.removeAll(mPermissionList);
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        if (mPermissionList.isEmpty()) { //未授予的权限为空，表示权限都授予了
            //可以直接执行相关操作
            doBackup();
        } else {
            //申请权限
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, REQUESET_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUESET_PERMISSIONS_CODE) {

            boolean isAllGranted = true; //权限是否全部授予

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) { //拒绝
                    isAllGranted = false;
                }
            }

            //权限全部授予了，执行操作
            if (isAllGranted) {
                doBackup();
            }
        }
    }



    //歌曲播放
    public void beginLrcPlay(String musicPath){

        mPlayer = new MediaPlayer();
        try {
            //	mPlayer.setDataSource(getAssets().openFd("test.mp3").getFileDescriptor());
            mPlayer.setDataSource(musicPath);
            //准备播放歌曲监听
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //准备完毕
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            //歌曲播放完毕监听
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stopLrcPlay();
                }
            });
            //准备播放歌曲
            mPlayer.prepare();
            //开始播放歌曲
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopLrcPlay(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void doBackup() {
        // 本文主旨是讲解如果动态申请权限, 具体备份代码不再展示, 就假装备份一下
        Toast.makeText(this, "正在备份通讯录...", Toast.LENGTH_SHORT).show();
    }
}

