package net.grobas.blizzardleaderboards.core.database.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmLeaderboard extends RealmObject {

    private RealmList<RealmRow> rows;
    private String bracket;
    private String host;

    public RealmList<RealmRow> getRows() {
        return rows;
    }

    public void setRows(RealmList<RealmRow> mList) {
        this.rows = mList;
    }

    public void setBracket(String bracket) {
        this.bracket = bracket;
    }

    public String getBracket() {
        return bracket;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}

