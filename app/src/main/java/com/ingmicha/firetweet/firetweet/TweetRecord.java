package com.ingmicha.firetweet.firetweet;

/**
 * Created by Ingmicha on 03/11/2016.
 */
public class TweetRecord {


    private String recordId;
    private String tweet;
    private String date;

    public TweetRecord(){

    }

    public TweetRecord(String recordId, String tweet, String date) {
        this.recordId = recordId;
        this.tweet = tweet;
        this.date = date;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
