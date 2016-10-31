package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;


public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;

    ImageView ivProfileImage;
    TextView tvUserName;
    EditText tvBody;
    TextView charsLeft;

    Button tweetButton;

    private final int MAX_CHARS = 140;
    private int remainingChars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        remainingChars = MAX_CHARS;

        client = TwitterApplication.getRestClient();
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvName);
        tvBody = (EditText) findViewById(R.id.tvBody);
        tweetButton = (Button) findViewById(R.id.tweet);
        tweetButton.setClickable(false);
        tweetButton.setAlpha(.5f);

        charsLeft = (TextView) findViewById(R.id.charsLeft);
        charsLeft.setText(String.valueOf(MAX_CHARS));

        tvBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0 && s.length() < MAX_CHARS) {
                    tweetButton.setAlpha(1f);
                    tweetButton.setClickable(true);
                } else {
                    tweetButton.setAlpha(.2f);
                    tweetButton.setClickable(false);
                }

                remainingChars = MAX_CHARS - s.length();
                charsLeft.setText(String.valueOf(remainingChars));
                if(remainingChars < 0){
                    charsLeft.setTextColor(Color.RED);
                } else {
                    charsLeft.setTextColor(Color.BLACK);
                }
                //Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });


        getUserInformation();
    }

    private void getUserInformation() {
        client.getUserCredentials(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                User user = User.fromJson(response);
                tvUserName.setText(user.getName());
                ivProfileImage.setImageResource(android.R.color.transparent);
                Picasso.with(getContext()).load(user.getProfileUrl()).into(ivProfileImage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void onTweet(View view){

        if(tvBody.getText().toString().length() != 0) {
            client.postTweet(tvBody.getText().toString(), new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Tweet tweet = Tweet.fromJSON(response);
                    Intent data = new Intent();
                    data.putExtra("tweet", tweet);
                    setResult(RESULT_OK, data);

                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });

        }
    }

    public void onCancel(View view){
        finish();
    }

}
