package net.grobas.blizzardleaderboards.core.database;

import android.content.Context;

import net.grobas.blizzardleaderboards.core.database.model.RealmLeaderboard;
import net.grobas.blizzardleaderboards.core.database.model.RealmCharacterProfile;
import net.grobas.blizzardleaderboards.core.database.rx.RealmObservable;

import io.realm.Realm;
import rx.Observable;
import rx.functions.Func1;

public class ObservableDAO {

    private static final String COLUMN_BRACKET = "bracket";
    private static final String COLUMN_HOST = "host";
    private static final String COLUMN_REALM = "realmName";
    private static final String COLUMN_NAME = "name";

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

    public Observable<RealmLeaderboard> writeLeaderboard(final RealmLeaderboard leaderboard, final boolean forceUpdate) {
        return RealmObservable.object(context, new Func1<Realm, RealmLeaderboard>() {
            @Override
            public RealmLeaderboard call(Realm realm) {
                RealmLeaderboard lb = realm.where(RealmLeaderboard.class).equalTo(COLUMN_HOST, leaderboard.getHost()).
                        equalTo(COLUMN_BRACKET, leaderboard.getBracket()).findFirst();
                if(lb != null && forceUpdate)
                    lb.removeFromRealm();
                realm.copyToRealm(leaderboard);
                return leaderboard;
            }
        });
    }

    public Observable<RealmCharacterProfile> readCharacterProfile(final String realmName, final String name) {
        return RealmObservable.object(context, new Func1<Realm, RealmCharacterProfile>() {
            @Override
            public RealmCharacterProfile call(Realm realm) {
                return realm.where(RealmCharacterProfile.class).equalTo(COLUMN_REALM, realmName).
                        equalTo(COLUMN_NAME, name).findFirst();
            }
        });
    }

    public Observable<RealmCharacterProfile> writeCharacterProfile(final RealmCharacterProfile profile, final boolean forceUpdate) {
        return RealmObservable.object(context, new Func1<Realm, RealmCharacterProfile>() {
            @Override
            public RealmCharacterProfile call(Realm realm) {
                RealmCharacterProfile pf = realm.where(RealmCharacterProfile.class).equalTo(COLUMN_REALM, profile.getRealmName()).
                        equalTo(COLUMN_NAME, profile.getName()).findFirst();
                if(pf != null && forceUpdate)
                    pf.removeFromRealm();
                realm.copyToRealm(profile);
                return profile;
            }
        });
    }

}
