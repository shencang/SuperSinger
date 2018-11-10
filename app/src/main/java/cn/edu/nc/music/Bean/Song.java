package cn.edu.nc.music.Bean;

/**
 * Created by pc123 on 2018/10/9.
 */

public class Song {
    private String fileName;
    private String title;
    private int duration1;
    private String singer1;
    private String album;
    private String year;
    private String type;
    private String size1;
    private String fileUrl;
    /**
     * 歌手
     */
    public String singer;
    /**
     * 歌曲名
     */
    public String song;
    /**
     * 歌曲的地址
     */
    public String path;
    /**
     * 歌曲长度
     */
    public int duration;
    /**
     * 歌曲的大小
     */
    public long size;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}
