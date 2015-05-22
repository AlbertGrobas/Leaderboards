package net.grobas.blizzardleaderboards.app;

import android.app.Application;

import net.grobas.blizzardleaderboards.BuildConfig;
import net.grobas.blizzardleaderboards.core.LeaderboardDataService;

import timber.log.Timber;

public class BlizzardLeaderboards extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LeaderboardDataService.getInstance().setContext(getApplicationContext());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //TODO
        }
    }
}