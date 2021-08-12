package com.github.simplet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.simplet.utils.LocalStorage;

public class SettingsActivity extends AppCompatActivity implements AdapterView
        .OnItemSelectedListener {

    private final String[] conversionOptions = new String[]{"Fahrenheit", "Celcius", "Kelvin"};
    private final String[] apiModeOptions = new String[]{"Private", "Public"};
    private Button saveSettings;
    private Spinner conversionSpinner, apiModeSpinner;
    private EditText username, password, refresh, apiUrl, raspPiHash;
    // Temporary storing units of temperature \ API mode
    private String temp_units, temp_api_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.github.simplet.R.layout.activity_settings);

        Toolbar myToolbar = findViewById(com.github.simplet.R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(com.github.simplet.R.color
                .colorPrimary));
        setSupportActionBar(myToolbar);

        LocalStorage.setContext(this);

        this.conversionSpinner = findViewById(com.github.simplet.R.id.conversion_input);
        this.apiModeSpinner = findViewById(com.github.simplet.R.id.api_mode_input);
        this.saveSettings = findViewById(com.github.simplet.R.id.saveSettings);
        this.username = findViewById(com.github.simplet.R.id.username_input);
        this.password = findViewById(com.github.simplet.R.id.password_input);
        this.refresh = findViewById(com.github.simplet.R.id.refresh_input);
        this.apiUrl = findViewById(com.github.simplet.R.id.api_url_input);
        this.raspPiHash = findViewById(com.github.simplet.R.id.rasp_pi_hash_input);

        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_dropdown_item, this.conversionOptions);
        this.conversionSpinner.setAdapter(adapterUnits);
        this.conversionSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterAPIMode = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_dropdown_item, this.apiModeOptions);
        this.apiModeSpinner.setAdapter(adapterAPIMode);
        this.apiModeSpinner.setOnItemSelectedListener(this);

        this.getSettings();

        this.saveSettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                LocalStorage.setString(com.github.simplet.R.string.username, SettingsActivity
                        .this.getUsername());
                LocalStorage.setString(com.github.simplet.R.string.password, SettingsActivity
                        .this.getPassword());
                LocalStorage.setInt(com.github.simplet.R.integer.refresh, SettingsActivity.this
                        .getRefresh());
                LocalStorage.setString(com.github.simplet.R.string.api_url, SettingsActivity.this
                        .getApiUrl());
                LocalStorage.setString(com.github.simplet.R.string.rasp_pi_hash, SettingsActivity
                        .this.getRaspPiHash());
                LocalStorage.setString(com.github.simplet.R.string.unit, SettingsActivity.this
                        .temp_units);
                LocalStorage.setString(com.github.simplet.R.string.api_mode, SettingsActivity
                        .this.temp_api_mode);
                finish();
            }
        });

    }

    private void getSettings() {
        this.setUsername(LocalStorage.getString(com.github.simplet.R.string.username, ""));
        this.setPassword(LocalStorage.getString(com.github.simplet.R.string.password, ""));
        this.setApiUrl(LocalStorage.getString(com.github.simplet.R.string.api_url,
                "http:127.0.1.1/"));
        this.setRaspPiHash(LocalStorage.getString(com.github.simplet.R.string.rasp_pi_hash, ""));
        this.setRefresh(LocalStorage.getInt(com.github.simplet.R.integer.refresh, 5));
        this.setUnits(LocalStorage.getString(com.github.simplet.R.string.unit, "fahrenheit"));
        this.setAPIMode(LocalStorage.getString(com.github.simplet.R.string.api_mode, "Public"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.github.simplet.R.menu.header_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case com.github.simplet.R.id.back_button:
                //myIntent.putExtra("key", value); //Optional parameters
                SettingsActivity.this.finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == com.github.simplet.R.id.conversion_input) {
            this.temp_units = this.conversionOptions[position];

        } else if (parent.getId() == com.github.simplet.R.id.api_mode_input) {
            this.temp_api_mode = this.apiModeOptions[position];

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String getUsername() {
        return this.username.getText().toString();
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getPassword() {
        return this.password.getText().toString();
    }

    public void setPassword(String password) {
        this.password.setText(password);
    }

    // TODO: optimize later
    public Integer getRefresh() {
        return Integer.parseInt(this.refresh.getText().toString());
    }

    public void setRefresh(Integer seconds) {
        this.refresh.setText(seconds.toString());
    }

    public String getApiUrl() {
        return this.apiUrl.getText().toString();
    }

    public void setApiUrl(String api_url) {
        this.apiUrl.setText(api_url);
    }

    public String getRaspPiHash() {
        return this.raspPiHash.getText().toString();
    }

    public void setRaspPiHash(String rasp_hash) {
        this.raspPiHash.setText(rasp_hash);
    }

    public void setUnits(String units) {
        int index = 0;

        units = units.toLowerCase();
        switch (units) {

            case "fahrenheit":
                index = 0;
                break;
            case "celcius":
                index = 1;
                break;
            case "kelvin":
                index = 2;
                break;
        }

        this.conversionSpinner.setSelection(index);
    }

    public void setAPIMode(String mode) {
        int index = 0;

        mode = mode.toLowerCase();
        switch (mode) {

            case "private":
                index = 0;
                break;
            case "public":
                index = 1;
                break;
        }

        this.apiModeSpinner.setSelection(index);
    }
}
