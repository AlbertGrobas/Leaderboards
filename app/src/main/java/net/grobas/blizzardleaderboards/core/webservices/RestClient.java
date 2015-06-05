package net.grobas.blizzardleaderboards.core.webservices;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

public class RestClient {

    private BlizzardApiClient apiClient;
    private String host;
    private Gson realmGson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(RealmObject.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    public RestClient() {}

    public RestClient(String host) {
        this.host = host;
        rebuildClient();
    }

    private void rebuildClient() {

        RestAdapter adapter = new RestAdapter.Builder()
            .setEndpoint("http://"+host)
            .setConverter(new GsonConverter(realmGson))
            .setLogLevel(RestAdapter.LogLevel.HEADERS)
            .setLog(new AndroidLog("wow-api"))
            .build();
        apiClient = adapter.create(BlizzardApiClient.class);
    }

    public BlizzardApiClient getApiClient() {
        return apiClient;
    }

    public void setHost(String newHost) {
        if(host == null || !newHost.contentEquals(host)) {
            host = newHost;
            rebuildClient();
        }
    }
}
