package com.example.qi.biubiunews.models;

/**
 * Created by qi on 17-4-26.
 */

public class Comment {


    /**
     * content : sdffghgdgfds
     * id : 2
     * news_id : 13807
     * time : Wed, 10 May 2017 23:51:05 GMT
     * user : {"about_me":"","confirmed":true,"followed_count":1,"follower_count":0,"icon":"http://192.168.253.2:5000/static/images/user_icon/8JPEG_20170510_091033.jpg","id":8,"last_seen":"Wed, 26 Apr 2017 12:33:37 GMT","location":"","member_since":"Wed, 26 Apr 2017 12:33:37 GMT","name":null,"news_count":1}
     */

    private String content;
    private int id;
    private int news_id;
    private String time;
    private User user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", id=" + id +
                ", news_id=" + news_id +
                ", time='" + time + '\'' +
                ", user=" + user +
                '}';
    }
}
