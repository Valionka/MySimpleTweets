package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.codepath.apps.mysimpletweets.R.id.ivProfileImage;
import static com.codepath.apps.mysimpletweets.R.id.tvBody;
import static com.codepath.apps.mysimpletweets.R.id.tvTimeStamp;

/**
 * Created by vmiha on 10/28/16.
 */

//taking tweet objects and convert to views
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private ProfileImageClickListener listener;

    public void setProfileImageClickListener(ProfileImageClickListener listener) {
        this.listener = listener;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
        this.listener = null;
    }

    private class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvTimeStamp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(tvBody);
            viewHolder.tvTimeStamp = (TextView) convertView.findViewById(tvTimeStamp);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onProfileImageClick(tweet);
                }
            }
        });
        Picasso.with(getContext()).load(tweet.getUser().getProfileUrl()).into(viewHolder.ivProfileImage);
        viewHolder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        return convertView;

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

         return relativeDate;
    }

    public interface ProfileImageClickListener {
        public void onProfileImageClick(Tweet tweet);
    }


}
