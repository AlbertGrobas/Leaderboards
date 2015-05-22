package net.grobas.blizzardleaderboards.core;

import android.content.Context;

import net.grobas.blizzardleaderboards.app.domain.Leaderboard;
import net.grobas.blizzardleaderboards.app.domain.Profile;
import net.grobas.blizzardleaderboards.app.domain.Row;
import net.grobas.blizzardleaderboards.core.database.ObservableDAO;
import net.grobas.blizzardleaderboards.core.database.model.RealmLeaderboard;
import net.grobas.blizzardleaderboards.core.database.model.RealmRow;
import net.grobas.blizzardleaderboards.core.webservices.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.functions.Func1;

public class LeaderboardDataService {

    private final static LeaderboardDataService INSTANCE = new LeaderboardDataService();

    private String mHost;
    private ObservableDAO mDAO;
    private RestClient mClient = new RestClient();

    private LeaderboardDataService() {}

    public static LeaderboardDataService getInstance() {
        return INSTANCE;
    }

    public void setContext(Context context) {
        mDAO = new ObservableDAO(context);
    }

    public void setHost(String host) {
        mHost = host;
        mClient.setHost(host);
    }

    public Observable<Leaderboard> getLeaderboard(final String bracket, final boolean forceUpdate) {
        final Observable<RealmLeaderboard> apiObservable = mClient.getApiClient().leaderboardObservable(bracket, Locale.getDefault().toString());
        final Observable<RealmLeaderboard> dbObservable = mDAO.readLeaderboard(mHost, bracket);

        return dbObservable.exists(new Func1<RealmLeaderboard, Boolean>() {
            @Override
            @DebugLog
            public Boolean call(RealmLeaderboard leaderboard) {
                return (leaderboard != null);
            }
        }).flatMap(new Func1<Boolean, Observable<RealmLeaderboard>>() {
            @Override
            @DebugLog
            public Observable<RealmLeaderboard> call(Boolean inDB) {
                return (inDB && !forceUpdate) ? dbObservable : apiObservable;
            }
        }).flatMap(new Func1<RealmLeaderboard, Observable<RealmLeaderboard>>() {
            @Override
            @DebugLog
            public Observable<RealmLeaderboard> call(RealmLeaderboard leaderboard) {
                if (leaderboard.getHost() == null) {
                    leaderboard.setBracket(bracket);
                    leaderboard.setHost(mHost);
                }
                return mDAO.writeLeaderboard(leaderboard, forceUpdate);
            }
        }).map(new Func1<RealmLeaderboard, Leaderboard>() {
            @Override
            @DebugLog
            public Leaderboard call(RealmLeaderboard leaderboard) {
                return realmToLeaderboard(leaderboard);
            }
        });
    }

    public Observable<Profile> getCharacterProfile(final String realmName, final String name) {
        return mClient.getApiClient().characterProfileObservable(realmName, name, Locale.getDefault().toString());
    }

    private static Leaderboard realmToLeaderboard(RealmLeaderboard realmLeaderboard) {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setBracket(realmLeaderboard.getBracket());
        leaderboard.setHost(realmLeaderboard.getHost());

        List<Row> list = new ArrayList<>(realmLeaderboard.getRows().size());
        for (RealmRow realmRow : realmLeaderboard.getRows()) {
            list.add(realmToRow(realmRow));
        }

        leaderboard.setRows(list);
        return leaderboard;
    }

    private static Row realmToRow(RealmRow realmRow) {
        Row row = new Row();
        row.setClassId(realmRow.getClassId());
        row.setFactionId(realmRow.getFactionId());
        row.setGenderId(realmRow.getGenderId());
        row.setName(realmRow.getName());
        //Well blizzard, let's fix this...
        int raceId = realmRow.getRaceId();
        if (raceId == 26 || raceId == 25 || raceId == 24) //pandaren
            raceId = 13;
        if (raceId == 22) //worgen
            raceId = 12;

        row.setRaceId(raceId);
        row.setRanking(realmRow.getRanking());
        row.setRating(realmRow.getRating());
        row.setRealmName(realmRow.getRealmName());
        row.setRealmId(realmRow.getRealmId());
        row.setRealmSlug(realmRow.getRealmSlug());
        row.setWeeklyWins(realmRow.getWeeklyWins());
        row.setWeeklyLosses(realmRow.getWeeklyLosses());
        row.setSpecId(realmRow.getSpecId());
        row.setSeasonWins(realmRow.getSeasonWins());
        row.setSeasonLosses(realmRow.getSeasonLosses());
        return row;
    }

}
