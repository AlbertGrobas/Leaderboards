package net.grobas.blizzardleaderboards.app.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.grobas.blizzardleaderboards.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
