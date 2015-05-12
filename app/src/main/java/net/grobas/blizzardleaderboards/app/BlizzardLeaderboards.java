package net.grobas.blizzardleaderboards.app;

import android.app.Application;

import net.grobas.blizzardleaderboards.core.LeaderboardDataService;

public class BlizzardLeaderboards extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LeaderboardDataService.getInstance().setContext(getApplicationContext());
    }
}
