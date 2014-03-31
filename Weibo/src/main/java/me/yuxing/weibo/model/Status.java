package me.yuxing.weibo.model;

/**
 * Created by yuxing on 13-10-25.
 */
public class Status {
    public String created_at;
    public long id;
    public long mid;
    public User user;
    public String text;
    public String source;
    public boolean favorited;
    public boolean truncated;
    public String thumbnail_pic;
    public String bmiddle_pic;
    public String original_pic;
    public Status retweeted_status;
}
