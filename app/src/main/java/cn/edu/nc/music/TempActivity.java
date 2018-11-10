package cn.edu.nc.music;
//方法可用缺乏布局文件支持。需要进行分配
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

public class TempActivity extends Activity {

    public String lrcPath="/sdcard/test.lrc";
    public String Newlrc="";
    public String musicPath="/sdcard/小苹果.mp3";
    public  int[] contentNum;
    private String savefilePath = "/sdcard/lrc";//歌词文件存放位置
	public final static String TAG = "MainActivity";

    //自定义LrcView，用来展示歌词
	ILrcView mLrcView;
    //更新歌词的频率，每秒更新一次
    private int mPalyTimerDuration = 1000;
    //更新歌词的定时器
    private Timer mTimer;
    //更新歌词的定时任务
    private TimerTask mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取自定义的LrcView
        setContentView(R.layout.activity_main);
     //   mLrcView=(ILrcView)findViewById(R.id.lrcView);自定义view的指向

        //从assets目录下读取歌词文件内容
       // String lrc = getFromAssets("test.lrc");
        //从指定目录下读取歌词文件内容
        String lrc = ReafLrcFile(lrcPath);
        //解析歌词构造器
        ILrcBuilder builder = new DefaultLrcBuilder();
        //解析歌词返回LrcRow集合
        List<LrcRow> rows = builder.getLrcRows(lrc);

        getContentNum(rows);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    	if (mPlayer != null) {
    		mPlayer.stop();
    	}
    }

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
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line="";
            String result="";
            while((line = bufferedReader.readLine()) != null){
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


    MediaPlayer mPlayer;

    /**
     * 开始播放歌曲并同步展示歌词
     */
    public void beginLrcPlay(String musicPath){

    	mPlayer = new MediaPlayer();
    	try {
    	//	mPlayer.setDataSource(getAssets().openFd("test.mp3").getFileDescriptor());
            mPlayer.setDataSource(musicPath);
    		//准备播放歌曲监听
            mPlayer.setOnPreparedListener(new OnPreparedListener() {
                //准备完毕
				public void onPrepared(MediaPlayer mp) {
					mp.start();
			        if(mTimer == null){
			        	mTimer = new Timer();
			        	mTask = new LrcTask();
			        	mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
			        }
				}
			});
            //歌曲播放完毕监听
    		mPlayer.setOnCompletionListener(new OnCompletionListener() {
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
    public int[] getContentNum(List<LrcRow> rows){
        contentNum =new int[rows.size()];
        for(int i=0;i<rows.size();i++){
            contentNum[i]=rows.get(i).content.length();


        }
        for(int i=0;i<rows.size();i++){
            Log.d(TAG, Integer.toString(contentNum[i])+"  "+rows.get(i).strTime+"\n");

        }
        return   contentNum;

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

        for (int n=0;n<rows.size();n++){
            newLrc[n]=rows.get(n).strTime+newLrc[n];
        }


        //http://180.76.117.51:8000/?char=%E5%8D%8E%E9%A3%9E&length=3%2C12%2C6%2C7%2C9%2C15%2C8%2C15%2C4%2C14%2C14
try {

        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            name = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + name;

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
                            .url("http://180.76.117.51:8000/?char="+strWord+"&length="+strCon)
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
            TempActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //滚动歌词
                    mLrcView.seekLrcToTime(timePassed);
                }
            });

        }
    };


}
