package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmiha on 11/2/16.
 */

public abstract class TweetsListFragment extends Fragment {

    private ProfileClickListener listener;
    private TweetsArrayAdapter aTweets;
    private SwipeRefreshLayout swipeContainer;
    private ListView lvTweets;
    private List<Tweet> tweets;
    private EndlessScrollListener scrollListener;


    public interface ProfileClickListener {
        public void onProfileClick(User user);
    }

    public abstract void populateTimeline(final boolean isRefresh);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileClickListener) {
            listener = (ProfileClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement TweetsListFragment.ProfileClickListener");
        }
    }

    //creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        aTweets.setProfileImageClickListener(new TweetsArrayAdapter.ProfileImageClickListener(){

            @Override
            public void onProfileImageClick(User user) {
                if(listener != null) {
                    listener.onProfileClick(user);
                }
            }
        });
    }

    // infaltion logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        // Lookup and set the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(true);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(false);
                return true;
            }
        };
        lvTweets.setOnScrollListener(scrollListener);
        return v;
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void insert(Tweet tweet, int index) {
        aTweets.insert(tweet, index);
    }

    public void clearTweets() {
        aTweets.clear();
    }

    public int  getTweetCount () {
        return aTweets.getCount();
    }

    public Tweet getTweet(int position) {
        return aTweets.getItem(position);
    }

    public void stopRefresh() {
        swipeContainer.setRefreshing(false);
    }

    public void resetScrollListener () {
        scrollListener.resetState();
    }
}
