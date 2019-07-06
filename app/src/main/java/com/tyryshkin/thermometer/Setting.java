package com.tyryshkin.thermometer;

import android.content.Context;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Setting extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button saveSettings;
    private Spinner conversionSpinner, apiModeSpinner;
    private EditText username, password, refresh, apiUrl, raspPiHash;
    private LocalStorage localStorage;


    // Temporary storing units of temperature \ API mode
    private String temp_units, temp_api_mode;

    private final String[] conversionOptions = new String[]{"Fahrenheit", "Celcius", "Kelvin"};
    private final String[] apiModeOptions = new String[]{"Private", "Public"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(myToolbar);

        this.localStorage.setContext(this);

        this.conversionSpinner = findViewById(R.id.conversion_input);
        this.apiModeSpinner =  findViewById(R.id.api_mode_input);
        this.saveSettings = (Button)findViewById(R.id.saveSettings);
        this.username = (EditText)findViewById(R.id.username_input);
        this.password = (EditText)findViewById(R.id.password_input);
        this.refresh = (EditText)findViewById(R.id.refresh_input);
        this.apiUrl = (EditText)findViewById(R.id.api_url_input);
        this.raspPiHash = (EditText)findViewById(R.id.rasp_pi_hash_input);

        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.conversionOptions);
        this.conversionSpinner.setAdapter(adapterUnits);
        this.conversionSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterAPIMode = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.apiModeOptions);
        this.apiModeSpinner.setAdapter(adapterAPIMode);
        this.apiModeSpinner.setOnItemSelectedListener(this);


        this.getSettings();

        this.saveSettings.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v)
            {

                Setting.this.localStorage.setString(R.string.username, Setting.this.getUsername());
                Setting.this.localStorage.setString(R.string.password, Setting.this.getPassword());
                Setting.this.localStorage.setInt(R.integer.refresh, Setting.this.getRefresh());
                Setting.this.localStorage.setString(R.string.api_url, Setting.this.getApiUrl());
                Setting.this.localStorage.setString(R.string.rasp_pi_hash, Setting.this.getRaspPiHash());
                Setting.this.localStorage.setString(R.string.unit, Setting.this.temp_units);
                Setting.this.localStorage.setString(R.string.api_mode, Setting.this.temp_api_mode);
                finish();
            }
        });

    }

    private void getSettings() {

        this.setUsername(Setting.this.localStorage.getString(R.string.username, ""));
        this.setPassword(Setting.this.localStorage.getString(R.string.password, ""));
        this.setApiUrl(Setting.this.localStorage.getString(R.string.api_url, "http:127.0.1.1/"));
        this.setRaspPiHash(Setting.this.localStorage.getString(R.string.rasp_pi_hash, ""));
        this.setRefresh(Setting.this.localStorage.getInt(R.integer.refresh, 5));
        this.setUnits(Setting.this.localStorage.getString(R.string.unit, "fahrenheit"));
        this.setAPIMode(Setting.this.localStorage.getString(R.string.api_mode, "Public"));
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.header_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.back_button:
                //myIntent.putExtra("key", value); //Optional parameters
                Setting.this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), this.conversionOptions[position], Toast.LENGTH_LONG).show();

        if(parent.getId() == R.id.conversion_input)
        {
            this.temp_units = this.conversionOptions[position];

        }
        else if(parent.getId() == R.id.api_mode_input)
        {
            this.temp_api_mode = this.apiModeOptions[position];

        }

    }

    @Override
    public void onNothingSelected (AdapterView<?> parent) {
        // leave alone
    }

    public String getUsername() {

        return this.username.getText().toString();
    }

    public String getPassword() {

        return this.password.getText().toString();
    }

    // TODO: optimize later
    public Integer getRefresh() {

        return Integer.parseInt(this.refresh.getText().toString());
    }

    public String getApiUrl() {

        return this.apiUrl.getText().toString();
    }

    public String getRaspPiHash() {

        return this.raspPiHash.getText().toString();
    }

    public void setUsername(String username) {

        this.username.setText(username);
    }

    public void setPassword(String password) {

        this.password.setText(password);
    }

    public void setRefresh(Integer seconds) {

        this.refresh.setText(seconds.toString());
    }

    public void setApiUrl(String api_url) {

        this.apiUrl.setText(api_url);
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
