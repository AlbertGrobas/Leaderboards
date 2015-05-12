package net.grobas.blizzardleaderboards.core.database;

import android.content.Context;

import net.grobas.blizzardleaderboards.core.database.model.RealmLeaderboard;
import net.grobas.blizzardleaderboards.core.database.rx.RealmObservable;

import io.realm.Realm;
import rx.Observable;
import rx.functions.Func1;

public class ObservableDAO {

    private static String COLUMN_BRACKET = "bracket";
    private static String COLUMN_HOST = "host";

    private Context context;

    public ObservableDAO(Context context) {
        this.context = context;
    }

    public Observable<RealmLeaderboard> readLeaderboard(final String host, final String bracket) {
        return RealmObservable.object(context, new Func1<Realm, RealmLeaderboard>() {
            @Override
            public RealmLeaderboard call(Realm realm) {
                return realm.where(RealmLeaderboard.class).equalTo(COLUMN_HOST, host).
                        equalTo(COLUMN_BRACKET, bracket).findFirst();
            }
        });
    }

    public Observable<RealmLeaderboard> writeLeaderboard(final RealmLeaderboard leaderboard) {
        return RealmObservable.object(context, new Func1<Realm, RealmLeaderboard>() {
            @Override
            public RealmLeaderboard call(Realm realm) {
                realm.copyToRealm(leaderboard);
                return leaderboard;
            }
        });
    }

}
