package com.github.simplet.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.simplet.R;
import com.github.simplet.adapters.RpistAdapter;
import com.github.simplet.network.rpist.RpistNodeClient;
import com.github.simplet.network.rpist.RpistTempCallback;
import com.github.simplet.utils.RpistNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final List<RpistNode> mRpistList = new ArrayList<>();/* = new ArrayList<>(Arrays.asList(
            new RpistNode(70, TemperatureScale.CELSIUS),
            new RpistNode(20, TemperatureScale.CELSIUS),
            new RpistNode(40, TemperatureScale.FAHRENHEIT)
    ));*/
    private RecyclerView mRecyclerView;
    private RpistAdapter mRpistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(com.github.simplet.R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(myToolbar);

        mRecyclerView = findViewById(R.id.rpist_recycle_view);
        mRpistAdapter = new RpistAdapter(this, mRpistList);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRpistAdapter);

        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        HandlerThread ht = new HandlerThread("temperatureRefresh");
        ht.start();

        Handler uihandler = new Handler();
        Handler handler = new Handler(ht.getLooper());
        RpistNodeClient client = new RpistNodeClient("http://10.0.1.184:8080/");

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect("tester")
                            .fetchRpistId();
                    client.getCelsius(new RpistTempCallback() {
                        @Override
                        public void onSuccess(float temperature) {
                            mRpistList.clear();
                            mRpistList.addAll(client.getRpistNodes());

                            mRpistAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(String code, String message) {
                            uihandler.post(() -> {
                                Context context = getApplicationContext();
                                CharSequence text = "Error occurred when trying to get newest " +
                                        "measurements";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            });
                        }
                    });
                } catch (IOException e) {
                    uihandler.post(() -> {
                        Context context = getApplicationContext();
                        CharSequence text = "Failed to connect to RPIST node or base";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    });
                }

                handler.postDelayed(this, 5000);
            }
        });
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
                MainActivity.this.startActivity(myIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
