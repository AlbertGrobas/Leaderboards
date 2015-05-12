package net.grobas.blizzardleaderboards.app.domain;

import java.util.List;

public class Leaderboard {

    private List<Row> rows;
    private String bracket;
    private String host;

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
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
