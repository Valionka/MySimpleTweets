package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by vmiha on 10/28/16.
 */

public class User implements Serializable{

    private String name;
    private long id;
    private String screenName;
    private String profileUrl;
    private String following;
    private String tagLine;

    public String getTagLine() {
        return tagLine;
    }

    public String getFollowers() {
        return followers;
    }

    public String getFollowing() {
        return following;
    }

    private String followers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public static User fromJson(JSONObject json) {
        User user = new User();

        try {
            user.name = json.getString("name");
            user.id = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileUrl = json.getString("profile_image_url");
            user.followers = json.getString("followers_count");
            user.following = json.getString("friends_count");
            user.tagLine = json.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
