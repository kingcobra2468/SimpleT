package com.github.simplet.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.simplet.R;
import com.github.simplet.adapters.RpistAdapter;
import com.github.simplet.network.APIRequest;
import com.github.simplet.utils.LocalStorage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //private LocalStorage localStorage;
    private List<Integer> mRpistList = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21));
    private RecyclerView mRecyclerView;
    private RpistAdapter mRpistAdapter;
    // private AsyncTask temp_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(com.github.simplet.R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(myToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rpist_recycle_view);
        mRpistAdapter = new RpistAdapter(this, mRpistList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mRpistAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.header_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case com.github.simplet.R.id.action_favorite:
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

      //  temp_refresh = new MyTask().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

   //     temp_refresh.cancel(true);
    }
    /*
    private class MyTask extends AsyncTask<String, String, String> {
        private APIRequest api_request = new APIRequest();
        private String username, password, apiUrl, units, raspHash, apiMode;
        private int refreshSec;

        private void getLatestData() {
            this.username = LocalStorage.getString(com.github.simplet.R.string.username, "");
            this.password = LocalStorage.getString(com.github.simplet.R.string.password, "");
            this.apiUrl = LocalStorage.getString(com.github.simplet.R.string.api_url,
                    "http:127.0.1.1/");
            this.units = LocalStorage.getString(com.github.simplet.R.string.unit, "celcius");
            this.refreshSec = LocalStorage.getInt(com.github.simplet.R.integer.refresh, 5);
            this.raspHash = LocalStorage.getString(com.github.simplet.R.string.rasp_pi_hash, "");
            this.apiMode = LocalStorage.getString(com.github.simplet.R.string.api_mode, "Public");

            if (this.api_request.getBaseUrl() != this.apiUrl) {
                this.api_request.setUrl(this.apiUrl);
            }

            //Update each time or do a check. Check might be slower
            this.api_request.setUsername(this.username);
            this.api_request.setPassword(this.password);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String apiCall = "";
            try {

                MyTask.this.getLatestData();

                while (!isCancelled()) {

                    if (this.apiMode.equalsIgnoreCase("public")) {
                        apiCall = "temperature/" + this.units.toLowerCase();
                    } else if (this.apiMode.equalsIgnoreCase("private")) {
                        apiCall = "temp/get-temperature/" + this.raspHash + "/" + this.units
                                .toLowerCase();
                    }

                    try {
                        JSONObject responseData = (JSONObject) this.api_request.sendRequest
                                ("GET", apiCall, null, false);
                        Double temperature = responseData.getDouble("temperature");
                        String units = responseData.getString("unit");
                        publishProgress(temperature.toString() + " " + units);
                        Thread.sleep(refreshSec * 1000);
                    } catch (Exception e) {

                        if (e.getMessage().equalsIgnoreCase("")) {
                            publishProgress("REQUEST ERROR");
                        } else {
                            publishProgress(e.getMessage());
                        }
                        Thread.sleep(5000);

                    }

                }
            } catch (Exception e) {
                publishProgress("ERROR");

            }

            return "done";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            myTextView.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            myTextView.setText(result);
        }
    }*/
}
