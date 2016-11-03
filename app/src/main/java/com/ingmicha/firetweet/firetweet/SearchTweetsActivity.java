package com.ingmicha.firetweet.firetweet;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.Calendar;


public class SearchTweetsActivity extends ListActivity {

    private static final String TAG = "SearchTweetsActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabaseRecord;

    private Button mSignOut;
    private Button mRecords;
    private Button mSearch;

    private TextView mKeyWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tweets);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent mainIntent = new Intent(SearchTweetsActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }

            }
        };

        mKeyWord = (TextView) findViewById(R.id.key_word);

        mSignOut = (Button) findViewById(R.id.sign_out_button);
        mRecords = (Button) findViewById(R.id.Historial_button);
        mSearch = (Button) findViewById(R.id.search_key);

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });

        mRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRecord();
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = mKeyWord.getText().toString();
                if (!TextUtils.isEmpty(word)) {

                    SaveRecord(word);
                    new TweetsTask().execute(word);

                } else {
                    Toast.makeText(SearchTweetsActivity.this, "Ingrese una palabra ...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    public class TweetsTask extends AsyncTask<String, String, TweetTimelineListAdapter> {

        @Override
        protected TweetTimelineListAdapter doInBackground(String... params) {
            SearchTimeline searchTimeline = new SearchTimeline.Builder()
                    .maxItemsPerRequest(4)
                    .query(params[0])
                    .build();
            TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(SearchTweetsActivity.this)
                    .setTimeline(searchTimeline)
                    .build();
            return adapter;
        }

        @Override
        protected void onPostExecute(TweetTimelineListAdapter tweetTimelineListAdapter) {
            super.onPostExecute(tweetTimelineListAdapter);

            setListAdapter(tweetTimelineListAdapter);
        }
    }

    public void SaveRecord(String word) {

        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);

        mDatabaseRecord = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        DatabaseReference neww = mDatabaseRecord.push();
        neww.child("tweet").setValue(word);
        neww.child("date").setValue(date);
    }

    public void ShowRecord() {

        Intent mRecordIntent = new Intent(SearchTweetsActivity.this, RecordActivity.class);
        mRecordIntent.putExtra("user_id", mUser.getUid());
        mRecordIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mRecordIntent);

    }

    public void SignOut() {

        mAuth.signOut();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
