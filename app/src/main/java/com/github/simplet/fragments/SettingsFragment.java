package com.github.simplet.fragments;

import android.os.Bundle;
import android.text.InputType;

import com.github.simplet.R;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        EditTextPreference weeklyGoalPref = findPreference("refresh_rate");
        if (weeklyGoalPref != null) {
            weeklyGoalPref.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        }

    }
}
