package net.grobas.blizzardleaderboards.core.webservices;

import net.grobas.blizzardleaderboards.core.database.model.RealmLeaderboard;
import net.grobas.blizzardleaderboards.core.database.model.RealmCharacterProfile;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface BlizzardApiClient {

    @GET("/api/wow/leaderboard/{bracket}")
    void leaderboardAsync(@Path("bracket") String bracket, @Query("locale") String locale,
                          Callback<RealmLeaderboard> response);

    @GET("/api/wow/leaderboard/{bracket}")
    RealmLeaderboard leaderboard(@Path("bracket") String bracket, @Query("locale") String locale);

    @GET("/api/wow/leaderboard/{bracket}")
    Observable<RealmLeaderboard> leaderboardObservable(@Path("bracket") String bracket, @Query("locale") String locale);

    @GET("/api/wow/character/{realm}/{name}")
    void characterProfileAsync(@Path("realm") String realm, @Path("name") String name, @Query("locale") String locale,
                        Callback<RealmCharacterProfile> response);

    @GET("/api/wow/character/{realm}/{name}")
    RealmCharacterProfile characterProfile(@Path("realm") String realm, @Path("name") String name, @Query("locale") String locale);

    @GET("/api/wow/character/{realm}/{name}")
    Observable<RealmCharacterProfile> characterProfileObservable(@Path("realm") String realm, @Path("name") String name, @Query("locale") String locale);

}