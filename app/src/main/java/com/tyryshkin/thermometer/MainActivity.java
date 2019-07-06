package com.tyryshkin.thermometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.MalformedURLException;

// TODO: add support so it stops and resume when page is not on top of stack

public class MainActivity extends AppCompatActivity {

    private TextView myTextView;
    private LocalStorage localStorage;
    private AsyncTask temp_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.localStorage.setContext(this);

        myTextView = (TextView)findViewById(R.id.temp);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(myToolbar);
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

            case R.id.action_favorite:
                Intent myIntent = new Intent(MainActivity.this, Setting.class);
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
        temp_refresh = new MyTask().execute();
    }

    @Override
    protected void onPause() {

        super.onPause();
        temp_refresh.cancel(true);
    }

    private class MyTask extends AsyncTask<String, String, String> {

        private APIRequest api_request = new APIRequest();
        private String username, password, apiUrl, units, raspHash, apiMode;
        private int refreshSec;

        private void getLatestData() throws MalformedURLException {

            System.out.println("abczy " + this.apiUrl);
            this.username = MainActivity.this.localStorage.getString(R.string.username, "");
            this.password = MainActivity.this.localStorage.getString(R.string.password, "");
            this.apiUrl = MainActivity.this.localStorage.getString(R.string.api_url, "http:127.0.1.1/");
            this.units = MainActivity.this.localStorage.getString(R.string.unit, "celcius");
            this.refreshSec = MainActivity.this.localStorage.getInt(R.integer.refresh, 5);
            this.raspHash = MainActivity.this.localStorage.getString(R.string.rasp_pi_hash, "");
            this.apiMode = MainActivity.this.localStorage.getString(R.string.api_mode, "Public");

            if (this.api_request.getBaseUrl() != this.apiUrl){
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

                while (! isCancelled()) {

                    if (this.apiMode.equalsIgnoreCase("public")) {
                        apiCall = "temperature/" + this.units.toLowerCase();
                    }
                    else if (this.apiMode.equalsIgnoreCase("private")) {
                        apiCall = "temp/get-temperature/" + this.raspHash + "/" + this.units.toLowerCase();
                        System.out.println("abczyE " + this.apiUrl + apiCall);
                    }

                    try {
                        JSONObject responseData = (JSONObject) this.api_request.sendRequest("GET", apiCall, null, false);
                        Double temperature = responseData.getDouble("temperature");
                        String units = responseData.getString("unit");
                        publishProgress(temperature.toString() + " " + units);
                        Thread.sleep(refreshSec * 1000);
                    }
                    catch (Exception e) {

                        if (e.getMessage().equalsIgnoreCase("")) {
                            publishProgress("REQUEST ERROR");
                        }
                        else {
                            publishProgress(e.getMessage());
                        }
                        Thread.sleep(5000);

                    }

                }
            }
            catch (Exception e) {
                publishProgress("ERROR");

            }

            return  "done";
        }
        @Override
        protected void onProgressUpdate(String... values){
            myTextView.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            myTextView.setText(result);
        }
    }

}
