package com.ingmicha.firetweet.firetweet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.models.Tweet;

import org.w3c.dom.Text;

public class RecordActivity extends AppCompatActivity {

    private String mUser_id = null;

    private DatabaseReference mDatabaseRecord;

    private RecyclerView mTweetRecord;

    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mUser_id = getIntent().getExtras().getString("user_id");

        mDatabaseRecord = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser_id);
        mDatabaseRecord.keepSynced(true);

        mTweetRecord = (RecyclerView) findViewById(R.id.tweets_records);
        mTweetRecord.setHasFixedSize(true);
        mTweetRecord.setLayoutManager(new LinearLayoutManager(this));

        mBackButton = (Button) findViewById(R.id.back_search);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mSearchIntent = new Intent(RecordActivity.this, SearchTweetsActivity.class);
                mSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mSearchIntent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<TweetRecord, TweetViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TweetRecord, TweetViewHolder>(

                TweetRecord.class,
                R.layout.record_row,
                TweetViewHolder.class,
                mDatabaseRecord
        ) {
            @Override
            protected void populateViewHolder(TweetViewHolder viewHolder, TweetRecord model, int position) {

                final String record_key = getRef(position).getKey();


                viewHolder.setTweet(model.getTweet());
                viewHolder.setTweetDate(model.getDate());
                viewHolder.mDeleteRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabaseRecord.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                mDatabaseRecord.child(record_key).removeValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        };
        mTweetRecord.setAdapter(firebaseRecyclerAdapter);
    }

    private static class TweetViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView mDeleteRecord;

        public TweetViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mDeleteRecord = (TextView) mView.findViewById(R.id.delete_record);

        }

        public void setTweet(String tweet) {
            TextView tweet_record = (TextView) mView.findViewById(R.id.tweet_record);
            tweet_record.setText(tweet);
        }

        public void setTweetDate(String tweetDate) {
            TextView tweet_date = (TextView) mView.findViewById(R.id.tweet_date);
            tweet_date.setText(tweetDate);
        }

    }

}
