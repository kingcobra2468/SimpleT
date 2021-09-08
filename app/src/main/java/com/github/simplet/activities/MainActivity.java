package com.github.simplet.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.simplet.R;
import com.github.simplet.adapters.RpistAdapter;
import com.github.simplet.models.rpist.RpistViewModel;
import com.github.simplet.network.rpist.RpistClient;
import com.github.simplet.network.rpist.RpistClientFactory;
import com.github.simplet.network.rpist.RpistTempCallback;
import com.github.simplet.utils.RpistNode;
import com.github.simplet.utils.TemperatureScale;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Main activity responsible for displaying temperature readings.
 */
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final List<RpistNode> mRpistList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RpistAdapter rpistAdapter;
    private RpistViewModel rpistViewModel;
    private SharedPreferences preferences;
    private RpistClientFactory clientFactory;
    private RpistClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLifecycle().addObserver(new RpistRefreshObserver());

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        // setup toolbar
        Toolbar myToolbar = findViewById(com.github.simplet.R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // setup recycleview and listeners
        recyclerView = findViewById(R.id.rpist_recycle_view);
        rpistAdapter = new RpistAdapter(this, mRpistList);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rpistAdapter);

        rpistViewModel = new ViewModelProvider(this).get(RpistViewModel.class);
        rpistViewModel.getRpists().observe(this,
                rpistNodes -> rpistAdapter.setRpistList(rpistNodes));

        // initialize rpist client
        clientFactory = new RpistClientFactory();
        client = clientFactory.createClient(
                preferences.getString("mode", "node"),
                preferences.getString("rpist_hostname", "http://127.0.0.1"),
                Integer.parseInt(preferences.getString("rpist_port", "8080")),
                TemperatureScale.valueOf(preferences.getString("scale", "CELSIUS").toUpperCase())
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case com.github.simplet.R.id.settings_icon:
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(myIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "rpist_hostname":
            case "rpist_port":
            case "auth_secret":
                client.setBaseUrl(preferences.getString("rpist_hostname", "http://127.0.0.1"),
                        Integer.parseInt(preferences.getString("rpist_port", "8080")));
                client.resetConnection();

                break;
            case "mode":
                client = clientFactory.createClient(
                        preferences.getString("mode", "node"),
                        preferences.getString("rpist_hostname", "http://127.0.0.1"),
                        Integer.parseInt(preferences.getString("rpist_port", "8080")),
                        TemperatureScale.valueOf(preferences.getString("scale", "CELSIUS").toUpperCase())
                );
        }
    }

    private class RpistRefreshObserver implements LifecycleObserver {
        private HandlerThread ht;
        private ExecutorService executor;
        private Handler uiHandler, rpistHandler;

        /**
         * Creates the background thread executor for running background threads.
         */
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void create() {
            executor = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });
        }

        /**
         * Start background thread and periodically get temperature readings.
         */
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void start() {
            ht = new HandlerThread("temperatureRefresh");
            ht.start();

            uiHandler = new Handler();
            rpistHandler = new Handler(ht.getLooper());

            executor.execute(new RpistRefreshRunnable(uiHandler, rpistHandler));
        }

        /**
         * Stop temperature refresh to save system resources.
         */
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void stop() {
            ht.quitSafely();
            uiHandler.removeCallbacksAndMessages(null);
            rpistHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Runnable for periodically fetching latest data from rpist.
     */
    private class RpistRefreshRunnable implements Runnable {
        private final Handler uiHandler;
        private final Handler rpistHandler;

        /**
         * Instantiates a new Rpist refresh runnable.
         *
         * @param uiHandler    the ui thread handler
         * @param rpistHandler the rpist background thread handler
         */
        public RpistRefreshRunnable(Handler uiHandler, Handler rpistHandler) {
            this.uiHandler = uiHandler;
            this.rpistHandler = rpistHandler;
        }

        @Override
        public void run() {
            // attempt to reconnect to rpist if connection reset
            if (client.isConnectionReset()) {
                Log.i("RPIST", "Attempting to connect");

                try {
                    // try to connect to the rpist
                    client.connect(preferences.getString("auth_secret", ""))
                            .fetchRpistId();
                    Log.i("RPIST", "Connection success");
                } catch (Exception e) {
                    Log.e("RPIST", e.toString());
                    uiHandler.post(() -> {
                        Context context = getApplicationContext();
                        CharSequence text = "Failed to connect to RPIST node or base";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    });
                }
            }
            // if connection is not active, then it is likely that the rpist is down. Stop polling
            // to save battery life.
            if (!client.isConnected()) {
                Log.i("RPIST", "Connection failure");
                return;
            }

            client.getCelsius(new RpistTempCallback() {

                @Override
                public void onSuccess() {
                    uiHandler.post(() -> {
                        // update the view model with the new rpist node values
                        rpistViewModel.getRpists().setValue(client.getRpistNodes());
                    });
                }

                @Override
                public void onError(String code, String message) {
                    uiHandler.post(() -> {
                        Context context = getApplicationContext();
                        CharSequence text = "Error occurred when trying to get newest " +
                                "measurements";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    });
                }
            });

            // periodically fetch the temperature again
            rpistHandler.postDelayed(this, Integer.parseInt(preferences.getString("refresh_rate",
                    "5000")));
        }
    }
}
