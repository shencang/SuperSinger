package cn.edu.nc.music.Activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.nc.music.R;
//import cn.edu.nc.music.TempActivity;
import cn.edu.nc.music.Fragment.FragmentPlayCi;
import cn.edu.nc.music.utils.FileUtils;
import cn.edu.nc.music.utils.TimeMethod;
import cn.edu.nc.music.view.ILrcBuilder;
import cn.edu.nc.music.view.ILrcView;
import cn.edu.nc.music.view.ILrcViewListener;
import cn.edu.nc.music.view.impl.DefaultLrcBuilder;
import cn.edu.nc.music.view.impl.LrcRow;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.SystemClock.sleep;

public class PlayPage extends AppCompatActivity implements View.OnClickListener {

    private FragmentPlayCi fragmentPlayCi;
    private ImageView playback;
    private ImageView play;
    private ImageView playplay;
//---------------------------------------------//
    public String LrcName="在多年以后.lrc";
    public String TempLrc="";
    public String lrcPath="/storage/emulated/0/在多年以后.lrc";
    public String Newlrc="";
    public String musicPath="/storage/emulated/0/在多年以后.mp3";
    public  String[] contentNum;
    public MediaPlayer mPlayer;
    ///storage/emulated/0/20181007-213345.lrc"
    private String savefilePath = "/storage/emulated/0/";//歌词文件存放位置
    public final static String TAG = "PlayPage";


    public static String pathFinal;

    public String rows2;
    //从指定目录下读取歌词文件内容
    String lrc = ReafLrcFile("/storage/emulated/0/"+ LrcName);
    //解析歌词构造器
    ILrcBuilder builder = new DefaultLrcBuilder();
    //解析歌词返回LrcRow集合
    List<LrcRow> rows3 = builder.getLrcRows(lrc);


    //自定义LrcView，用来展示歌词
    public ILrcView   mLrcView;
    //更新歌词的频率，每秒更新一次
    private int mPalyTimerDuration = 1000;
    //更新歌词的定时器
    private Timer mTimer;
    //更新歌词的定时任务
    private TimerTask mTask;
    //------------------------------------------//
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_page);
           mLrcView=(ILrcView)findViewById(R.id.lrcView);//自定义view的指向
        playback = findViewById(R.id.playback);
        playplay = findViewById(R.id.previous_song);
        play = findViewById(R.id.playplay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PlayPage.this).setTitle("请输入").setIcon(
                        android.R.drawable.ic_dialog_info).setView(
                        new EditText(PlayPage.this)).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ssssssssssssssssssssssssssss",rows2);
                                Newlrc=sendRequestWithOKHttp("真实",rows2);
                                lrcPath=savefilePath;
                                String needSaveLrc="";
                                Log.d("nihao",Newlrc.toString());
                                try {
                                    sleep(12000);
                                }catch (Exception E){
                                    E.printStackTrace();
                                }
                                char [] templrc =Newlrc.toCharArray();
                                Log.d("nihao",Newlrc.toString());
                                for (int i=2;i<templrc.length-2;i++){
                                    needSaveLrc=needSaveLrc+templrc[i];
                                }
                                saveLrc(rows3,needSaveLrc.split("', '"));
                                String lrc =  LrcName;
                                System.out.println("--lrc---"+lrc);
                                pathFinal = lrc;
                                System.out.println("------"+pathFinal);
                                Intent intent = new Intent(PlayPage.this, PlayPageNew.class);
                                    onPause();
                                    mPlayer.stop();
                                    Thread.dumpStack();
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        });
        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
                PlayPage.this.finish();
            }
        });
        fragmentPlayCi = new FragmentPlayCi();
        android.support.v4.app.FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_framecontent2, fragmentPlayCi);
        fragmentTransaction.commit();

        final Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_backxia);

        findViewById(R.id.content).getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        applyBlur();
                        return true;
                    }
                });
        //-------------------------//
        //从指定目录下读取歌词文件内容
        String lrc = ReafLrcFile(lrcPath);
        //解析歌词构造器
        ILrcBuilder builder = new DefaultLrcBuilder();
        //解析歌词返回LrcRow集合
        List<LrcRow> rows = builder.getLrcRows(lrc);


        getContentNum(rows);
        rows2=getContentNum(rows);
        String needSaveLrc="";
        try {

            char [] templrc =TempLrc.toCharArray();
            for (int i=2;i<templrc.length-2;i++){
                needSaveLrc=needSaveLrc+templrc[i];

            }
        }catch (Exception es) {
            es.printStackTrace();
        }
        saveLrc(rows,getContent(rows));
        //将得到的歌词集合传给mLrcView用来展示
        mLrcView.setLrc(rows);


        //开始播放歌曲并同步展示歌词
        beginLrcPlay(musicPath);

        //设置自定义的LrcView上下拖动歌词时监听
        mLrcView.setListener(new ILrcViewListener() {
            //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
            public void onLrcSeeked(int newPosition, LrcRow row) {
                if (mPlayer != null) {
                    Log.d(TAG, "onLrcSeeked:" + row.time);
                    mPlayer.seekTo((int) row.time);
                }
            }
        });
    }
//高斯模糊
    private void applyBlur() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);

        Bitmap bmp1 = view.getDrawingCache();
        int height = getOtherHeight();

        Bitmap bmp2 = Bitmap.createBitmap(bmp1, 0, height,bmp1.getWidth(), bmp1.getHeight() - height);
        blur(bmp2, findViewById(R.id.content));
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;
        float radius = 20;

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);


        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        Log.i("jerome", "blur time:" + (System.currentTimeMillis() - startMs));
    }

    /**
     * 获取系统状态栏和软件标题栏，部分软件没有标题栏，看自己软件的配置；
     * @return
     */
    private int getOtherHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;
        return statusBarHeight + titleBarHeight;
    }
    //--------------------------------歌词显示----------------------------//
    /**
     * 从assets目录下读取歌词文件内容
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String result="";
            while((line = bufReader.readLine()) != null){
                if(line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String ReafLrcFile(String path ){
        File file= new File(path);
        String result="";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";

            while((line = bufferedReader.readLine()) != null){
                if(line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            System.out.print("----------------result---111111---"+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("----------------result---2222222---"+result);
        return "";
    }




    /**
     * 开始播放歌曲并同步展示歌词
     */
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
                    if(mTimer == null){
                        mTimer = new Timer();
                        mTask = new PlayPage.LrcTask();
                        mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
                    }
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

    /**
     * 停止展示歌曲
     */
    public void stopLrcPlay(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
    /**
     * 返回记录歌词的每句字数集合
     */
    public String getContentNum(List<LrcRow> rows){
        contentNum =new String[rows.size()];
        for(int i=0;i<rows.size();i++){
            contentNum[i]=Integer.toString(rows.get(i).content.length());


        }
        for(int i=0;i<rows.size();i++){
            Log.d(TAG, contentNum[i]+"  "+rows.get(i).strTime+"\n");

        }
        StringBuffer str5 = new StringBuffer();
        for (String s : contentNum) {
            str5.append(s+',');
        }
        str5.deleteCharAt(str5.length()-1);
        Log.d("Message",str5.toString());
        return   str5.toString();

    }
    /**
     * 返回记录歌词的每句歌词
     */
    public String[] getContent(List<LrcRow> rows){
        String[] content =new String[rows.size()];
        for(int i=0;i<rows.size();i++){
            content[i]=rows.get(i).content;



        }
        return   content;

    }
    /**
     *  保存接收的歌词文件
     */
    private void saveLrc(List<LrcRow> rows, String[] newLrc){
        String mMinute1 = TimeMethod.getTime();
        String recFilePath = FileUtils.recAudioPath(savefilePath);
        String name= mMinute1+".lrc";
        LrcName=name;
        for (int n=0;n<rows.size();n++){
            Log.d("NEW``````````````````````````````",rows.get(n).strTime+newLrc[n] );
            newLrc[n]="["+rows.get(n).strTime+"]"+newLrc[n];
        }


        //http://180.76.117.51:8000/?char=%E5%8D%8E%E9%A3%9E&length=3%2C12%2C6%2C7%2C9%2C15%2C8%2C15%2C4%2C14%2C14
        try {

            //如果手机已插入sd卡,且app具有读写sd卡的权限
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                name = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + name;
                LrcName=name;
                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(name);
                for (int i=0;i<newLrc.length;i++){
                    output.write((newLrc[i]+"\n").getBytes());
                }

                //将String字符串以字节流的形式写入到输出流中
                output.close();
                //关闭输出流
            } //else Toast.makeText(cont, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
        }catch (Exception S){

        }

        // return lrcFile;
    }
    /**
     *HttpURLConnection
     * 从服务器获取歌词
     */
    private String sendRequestWithOKHttp(final String importWord, final String content){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    String strWord= java.net.URLEncoder.encode(importWord,"UTF-8");
                    String strCon= java.net.URLEncoder.encode(content,"UTF-8");

//                    RequestBody requestBody= new FormBody.Builder()
//                            .add("char","试试")
//                            .add("longth","%5B1%2C2%2C3%2C4%2C5%2C6%2C7%2C8%2C9%2C10%5D")
//                            .build();

                    Request request = new Request.Builder()
                            .url("http://180.76.117.51:8848/?char="+strWord+"&length="+strCon)
                            // .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("传输错误 "+ response);


                    String responseData = response.body().string();
                    showResponse(responseData);
                    Newlrc=responseData;


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();
        return  Newlrc;

    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            //   @Override
            public void run() {

                //responseText.setText(response);
            }
        });
    }


    /**
     * 歌词格式转换.从一串变多串
     */
    public String[] getStrFormList(String list){
        return list.split(",");

    }




    /**
     * 展示歌曲的定时任务
     */
    class LrcTask extends TimerTask {
        @Override
        public void run() {
            //获取歌曲播放的位置
            final long timePassed = mPlayer.getCurrentPosition();
            PlayPage.this.runOnUiThread(new Runnable() {
                public void run() {
                    //滚动歌词
                    mLrcView.seekLrcToTime(timePassed);
                }
            });

        }
    };
    //----------------------------------////
    @Override
    public void onClick(View v) {

    }
}