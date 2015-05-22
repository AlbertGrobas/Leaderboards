package net.grobas.blizzardleaderboards.app.ui;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import net.grobas.blizzardleaderboards.R;

import io.realm.Realm;
import io.realm.exceptions.RealmException;
import timber.log.Timber;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference examplePreference = findPreference("clear_cache");
        examplePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Realm.deleteRealmFile(getActivity());
                    Timber.i("Cache cleared");
                } catch (RuntimeException e) {
                    Timber.e(e, "Error on delete DB");
                }
                return true;
            }
        });
    }

}
