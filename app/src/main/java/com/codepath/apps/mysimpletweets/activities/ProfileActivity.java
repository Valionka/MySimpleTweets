package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class ProfileActivity extends AppCompatActivity implements TweetsListFragment.ProfileClickListener {

    TwitterClient client;
    User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(getIntent().getExtras() == null || (User) getIntent().getExtras().get("user") == null) {
            client = TwitterApplication.getRestClient();
            client.getUserCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);
                    //my current user account
                    // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    // toolbar.setTitle(user.getScreenName());
                    populateProfileHeader(user);
                    populateTimline(savedInstanceState, user);
                }

            });
        } else {
            user = (User) getIntent().getExtras().get("user");
            populateProfileHeader(user);
            populateTimline(savedInstanceState, user);
        }

    }

    private void populateTimline(Bundle savedInstanceState, User user) {
        // get the screen name from the activity that launches this
        String screenName = user.getScreenName();

        if(savedInstanceState == null) {
            // create the user timeline fragment
            UserTimelineFragment  fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            // display the user timline fragment (dynamic way)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }


    private void populateProfileHeader(User user) {

        TextView tvName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getScreenName());
        tvTagLine.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowers() + " Followers");
        tvFollowing.setText(user.getFollowing() + " Following");
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfileUrl()).into(ivProfileImage);
    }

    @Override
    public void onProfileClick(User user) {
        //do nothing as all already in profile view
    }
}

