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
import com.github.simplet.network.rpist.RpistNodeClient;
import com.github.simplet.network.rpist.RpistTempCallback;
import com.github.simplet.utils.RpistNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final List<RpistNode> mRpistList = new ArrayList<>();/* = new ArrayList<>(Arrays.asList(
            new RpistNode(70, TemperatureScale.CELSIUS),
            new RpistNode(20, TemperatureScale.CELSIUS),
            new RpistNode(40, TemperatureScale.FAHRENHEIT)
    ));*/
    private RecyclerView mRecyclerView;
    private RpistAdapter mRpistAdapter;
        private RpistRefreshObserver refreshObserver;
    private RpistViewModel rpistViewModel;
    private RpistNodeClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new RpistRefreshObserver());
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

        //rpistViewModel = new ViewModelProvider(this).get(RpistViewModel.class);
        //rpistViewModel.getRpists().observe(this,
        //        rpistNodes -> mRpistAdapter.setRpistList(rpistNodes));

        client = new RpistNodeClient("http://10.0.1.184:8080/");

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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "address":
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class RpistViewModel extends ViewModel {
        private MutableLiveData<List<RpistNode>> rpistsLiveData;

        public RpistViewModel() {

            rpistsLiveData = new MutableLiveData<>();
        }

        public MutableLiveData<List<RpistNode>> getRpists(){
            return rpistsLiveData;
        }
    }

    private class RpistRefreshObserver implements LifecycleObserver {
        private Lifecycle lifecycle;
        private ExecutorService executor;
        private Handler uiHandler, rpistHandler;
        HandlerThread ht;

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void create() {
            executor = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void start() {
            ht = new HandlerThread("temperatureRefresh");
            ht.start();

            uiHandler = new Handler();
            rpistHandler = new Handler(ht.getLooper());

            executor.execute(new RpistRefreshRunnable(uiHandler, rpistHandler));
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void stop() {
            ht.quitSafely();
            uiHandler.removeCallbacksAndMessages(null);
            rpistHandler.removeCallbacksAndMessages(null);
        }
    }

    private class RpistRefreshRunnable implements Runnable {
        private Handler uiHandler, rpistHandler;

        public RpistRefreshRunnable(Handler uiHandler, Handler rpistHandler) {
            this.uiHandler = uiHandler;
            this.rpistHandler = rpistHandler;
        }
        @Override
        public void run() {
            try {
                client.connect("tester")
                        .fetchRpistId();
                client.getCelsius(new RpistTempCallback() {
                    @Override
                    public void onSuccess(float temperature) {
                        //rpistViewModel.getRpists().setValue(client.getRpistNodes());

                        mRpistAdapter.setRpistList(client.getRpistNodes());
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
            } catch (IOException e) {
                uiHandler.post(() -> {
                    Context context = getApplicationContext();
                    CharSequence text = "Failed to connect to RPIST node or base";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                });
            }

            rpistHandler.postDelayed(this, 5000);
        }
    }
}
