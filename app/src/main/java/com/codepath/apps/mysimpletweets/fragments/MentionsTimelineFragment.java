package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vmiha on 11/2/16.
 */

public class MentionsTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline(false);
    }

    //send API req to populate timeline json and populate the listview
    private void populateTimeline(final boolean isRefresh) {

        Long tId = null;
        if(getTweetCount() > 0 && !isRefresh) {
            tId = (getTweet(getTweetCount() - 1)).getUid();
        }

        client.getMentionsTimeline(tId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                if(isRefresh) {
                    stopRefresh();
                    clearTweets();
                }
                addAll(Tweet.fromJsonArray(response));
                //Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                populateTimeline(isRefresh);
            }
        });
    }
}
