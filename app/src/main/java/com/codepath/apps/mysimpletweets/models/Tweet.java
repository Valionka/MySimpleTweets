package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vmiha on 10/28/16.
 *
 *
 *
 */

public class Tweet implements Serializable {

    private String body;
    private User user;
    private String createdAt;
    private long uid; // unique id for tweet

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }


    public static ArrayList<Tweet> fromJsonArray (JSONArray jsonArray) {
        ArrayList<Tweet> result = new ArrayList<>();
        for(int i=0; i < jsonArray.length(); i++) {
            try {
                Tweet tweet = fromJSON(jsonArray.getJSONObject(i));
                result.add(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;// if one fails keep parsing the rest of the tweets
            }
        }

        return result;
    }

}
